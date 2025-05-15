package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.usecase.trip.CancelTripUseCase
import com.rideconnect.domain.usecase.trip.CreateTripUseCase
import com.rideconnect.domain.usecase.trip.GetTripDetailsUseCase
import com.rideconnect.util.Resource

data class SearchingDriverUiState(
    val tripDetails: Trip? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val searchingState: SearchingState = SearchingState.SEARCHING,
    val elapsedTimeInSeconds: Int = 0

)

enum class SearchingState {
    SEARCHING,           // Đang tìm kiếm tài xế
    DRIVER_FOUND,        // Đã tìm thấy tài xế và đang chờ phản hồi
    DRIVER_ACCEPTED,     // Tài xế đã chấp nhận chuyến đi
    DRIVER_ARRIVING,     // Tài xế đang đến đón
    DRIVER_ARRIVED,
    COMPLETED,
    IN_PROGRESS,
    NO_DRIVERS_AVAILABLE // Không tìm thấy tài xế nào
}

@HiltViewModel
class SearchingDriverViewModel @Inject constructor(
    private val createTripUseCase: CreateTripUseCase,
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val cancelTripUseCase: CancelTripUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchingDriverUiState())
    val uiState: StateFlow<SearchingDriverUiState> = _uiState.asStateFlow()

    private var retryCount = 0
    private val maxRetries = 5 // Giới hạn số lần thử lại khi không có tài xế
    private var pollingJob: Job? = null
    private val pollingInterval = 3000L // Kiểm tra trạng thái mỗi 3 giây

    fun requestTrip(
        pickupLocation: Location,
        destinationLocation: Location,
        requestedVehicleType: String
    ) {
        stopPolling() // Dừng polling hiện tại (nếu có)

        viewModelScope.launch {
            if (_uiState.value.isLoading) return@launch

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                searchingState = SearchingState.SEARCHING  // Luôn bắt đầu với trạng thái SEARCHING
            )

            try {
                Log.d("SearchingDriverVM", "Requesting trip - Pickup: (${pickupLocation.latitude}, ${pickupLocation.longitude}), Destination: (${destinationLocation.latitude}, ${destinationLocation.longitude}), VehicleType: $requestedVehicleType")

                val request = CreateTripRequest(
                    pickupAddress = pickupLocation.address ?: "",
                    pickupLatitude = pickupLocation.latitude,
                    pickupLongitude = pickupLocation.longitude,
                    dropoffAddress = destinationLocation.address ?: "",
                    dropoffLatitude = destinationLocation.latitude,
                    dropoffLongitude = destinationLocation.longitude,
                    vehicleType = requestedVehicleType
                )

                when (val result = createTripUseCase(request)) {
                    is Resource.Success -> {
                        Log.d("SearchingDriverVM", "Trip created successfully: tripId=${result.data?.id}")
                        _uiState.value = _uiState.value.copy(
                            tripDetails = result.data,
                            isLoading = true,
                            searchingState = SearchingState.SEARCHING
                        )

                        // Bắt đầu polling để theo dõi trạng thái chuyến đi
                        val tripId = result.data?.id
                        if (tripId?.isNotEmpty() == true) {
                            startPollingTripStatus(tripId)
                        } else {
                            Log.e("SearchingDriverVM", "Invalid trip ID received")
                            _uiState.value = _uiState.value.copy(
                                error = "Không nhận được mã chuyến đi hợp lệ",
                                isLoading = false,
                                // Vẫn giữ trạng thái SEARCHING để hiển thị animation
                                searchingState = SearchingState.SEARCHING
                            )

                            // Đặt một delay trước khi chuyển sang NO_DRIVERS_AVAILABLE
                            delay(5000) // Đợi 5 giây để người dùng thấy thông báo lỗi
                            _uiState.value = _uiState.value.copy(
                                searchingState = SearchingState.NO_DRIVERS_AVAILABLE
                            )
                        }
                    }
                    is Resource.Error -> {
                        Log.e("SearchingDriverVM", "Error creating trip: ${result.message}")

                        _uiState.value = _uiState.value.copy(
                            error = "Không thể tạo chuyến đi: ${result.message}",
                            isLoading = false,
                            // Vẫn giữ trạng thái SEARCHING để hiển thị animation
                            searchingState = SearchingState.SEARCHING
                        )

                        // Đặt một delay trước khi chuyển sang NO_DRIVERS_AVAILABLE
                        delay(5000) // Đợi 5 giây để người dùng thấy thông báo lỗi
                        _uiState.value = _uiState.value.copy(
                            searchingState = SearchingState.NO_DRIVERS_AVAILABLE
                        )
                    }
                    else -> { /* Xử lý các trạng thái khác */ }
                }
            } catch (e: Exception) {
                Log.e("SearchingDriverVM", "Exception: ${e.message}", e)

                _uiState.value = _uiState.value.copy(
                    error = "Lỗi không xác định: ${e.message}",
                    isLoading = false,
                    // Vẫn giữ trạng thái SEARCHING để hiển thị animation
                    searchingState = SearchingState.SEARCHING
                )

                // Đặt một delay trước khi chuyển sang NO_DRIVERS_AVAILABLE
                delay(10000) // Đợi 5 giây để người dùng thấy thông báo lỗi
                _uiState.value = _uiState.value.copy(
                    searchingState = SearchingState.NO_DRIVERS_AVAILABLE
                )
            }
        }
    }


    private fun startPollingTripStatus(tripId: String) {
        stopPolling() // Đảm bảo dừng polling trước đó (nếu có)

        pollingJob = viewModelScope.launch {
            var noDriverTimeout = 0
            val maxTimeout = 60000 // 60 giây timeout nếu không tìm thấy tài xế
            var elapsedTimeInSeconds = 0

            while (true) {
                try {
                    if (uiState.value.searchingState == SearchingState.SEARCHING) {
                        _uiState.value = _uiState.value.copy(
                            elapsedTimeInSeconds = elapsedTimeInSeconds
                        )
                    }
                    when (val result = getTripDetailsUseCase(tripId)) {
                        is Resource.Success -> {
                            val tripDetails = result.data
                            Log.d("SearchingDriverVM", "Trip status update: ${tripDetails?.status}")

                            // Cập nhật trạng thái UI dựa trên trạng thái chuyến đi
                            when (tripDetails?.status?.name?.lowercase()) {
                                "pending" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = true,
                                        searchingState = SearchingState.SEARCHING
                                    )

                                    // Kiểm tra timeout
                                    noDriverTimeout += pollingInterval.toInt()
                                    if (noDriverTimeout >= maxTimeout) {
                                        // Chỉ cập nhật trạng thái NO_DRIVERS_AVAILABLE sau khi hết thời gian timeout
                                        _uiState.value = _uiState.value.copy(
                                            isLoading = false,
                                            searchingState = SearchingState.NO_DRIVERS_AVAILABLE
                                        )
                                        break // Dừng polling
                                    }
                                }
                                "drive_found" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = true,
                                        searchingState = SearchingState.DRIVER_FOUND
                                    )
                                    // Reset timeout khi tìm thấy tài xế
                                    noDriverTimeout = 0
                                }
                                    "accepted" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = false,
                                        searchingState = SearchingState.DRIVER_ACCEPTED
                                    )
                                    // Reset retry count khi thành công
                                    retryCount = 0
                                }
                                "arriving" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = false,
                                        searchingState = SearchingState.DRIVER_ARRIVING
                                    )
                                }
                                "arrived" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = false,
                                        searchingState = SearchingState.DRIVER_ARRIVED
                                    )
                                }

                                "completed" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = false,
                                        searchingState = SearchingState.COMPLETED
                                    )
                                    // Dừng polling khi chuyến đi đã hoàn thành
                                    break
                                }
                                "in_progress" -> {
                                    _uiState.value = _uiState.value.copy(
                                        tripDetails = tripDetails,
                                        isLoading = false,
                                        searchingState = SearchingState.IN_PROGRESS
                                    )
                                    // Tiếp tục polling khi chuyến đi đang diễn ra
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e("SearchingDriverVM", "Error getting trip details: ${result.message}")
                            // Tiếp tục polling ngay cả khi có lỗi, nhưng ghi log lỗi
                        }
                        is Resource.Loading -> {
                            // Xử lý trạng thái loading nếu cần
                        }

                        is Resource.Idle -> TODO()
                    }
                    elapsedTimeInSeconds = noDriverTimeout / 1000
                    if (uiState.value.searchingState == SearchingState.SEARCHING &&
                        noDriverTimeout >= maxTimeout) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            searchingState = SearchingState.NO_DRIVERS_AVAILABLE
                        )
                        break // Dừng polling
                    }
                } catch (e: Exception) {
                    Log.e("SearchingDriverVM", "Exception during polling: ${e.message}", e)
                    // Tiếp tục polling ngay cả khi có exception
                }

                delay(pollingInterval) // Đợi trước khi kiểm tra lại
                if (uiState.value.searchingState == SearchingState.SEARCHING) {
                    noDriverTimeout += pollingInterval.toInt()
                }
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }




    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
