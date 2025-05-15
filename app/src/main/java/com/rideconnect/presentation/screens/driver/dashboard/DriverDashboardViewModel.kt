package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.data.remote.websocket.WebSocketManager
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.domain.usecase.driver.UpdateDriverStatusUseCase
import com.rideconnect.service.LocationUpdateService
import com.rideconnect.util.Resource
import com.rideconnect.util.extensions.isCancelled
import com.rideconnect.util.extensions.isCompleted
import com.rideconnect.util.preferences.PreferenceKeys
import com.rideconnect.util.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDashboardViewModel @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val tripRepository: TripRepository,
    private val updateDriverStatusUseCase: UpdateDriverStatusUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    // Trạng thái cho chuyến đi mới
    private val _newTripRequest = MutableStateFlow<Trip?>(null)
    val newTripRequest: StateFlow<Trip?> = _newTripRequest.asStateFlow()

    // Trạng thái hiển thị dialog
    private val _showTripRequestDialog = MutableStateFlow(false)
    val showTripRequestDialog: StateFlow<Boolean> = _showTripRequestDialog.asStateFlow()

    // Context để sử dụng khi cần khởi động service
    private var appContext: Context? = null

    // Thêm các state cho việc cập nhật trạng thái tài xế
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _pendingStatus = MutableStateFlow<String?>(null)
    val pendingStatus: StateFlow<String?> = _pendingStatus.asStateFlow()

    init {
        // Khởi tạo trạng thái từ local storage
        viewModelScope.launch {
            preferenceManager.getPreference(PreferenceKeys.DRIVER_ONLINE_STATUS, false).collect { status ->
                _isOnline.value = status
            }
        }

        // Lắng nghe các yêu cầu chuyến đi mới từ WebSocket
        viewModelScope.launch {
            webSocketManager.newTripRequestFlow.collect { tripResponse ->
                Log.d("DriverDashboardVM", "Nhận yêu cầu chuyến đi mới: ${tripResponse.tripId}")
                Log.d("DriverDashboardVM", "Trạng thái online hiện tại: ${_isOnline.value}")

                // Tự động chuyển sang trạng thái online nếu cần
                if (!_isOnline.value) {
                    // Sử dụng hàm toggleOnlineStatus để đảm bảo cập nhật đúng trạng thái
                    appContext?.let { context ->
                        toggleOnlineStatus(context, true)
                    }
                    Log.d("DriverDashboardVM", "Đã tự động chuyển sang trạng thái online")
                }

                // Luôn lấy thông tin chuyến đi mới nhất từ repository
                val currentTrip = tripRepository.currentTrip.value
                Log.d("DriverDashboardVM", "Chuyến đi hiện tại: ${currentTrip?.id}, Trạng thái: ${currentTrip?.status}")

                // Kiểm tra xem có thể hiển thị dialog hay không
                if (currentTrip == null || currentTrip.isCompleted() || currentTrip.isCancelled()) {
                    Log.d("DriverDashboardVM", "Đủ điều kiện hiển thị dialog")

                    try {
                        val tripDetails = tripRepository.getTripDetails(tripResponse.tripId)
                        Log.d("DriverDashboardVM", "Kết quả lấy chi tiết chuyến đi: ${tripDetails.data != null}")

                        _newTripRequest.value = tripDetails.data
                        _showTripRequestDialog.value = true
                        Log.d("DriverDashboardVM", "Đã thiết lập hiển thị dialog: ${_showTripRequestDialog.value}")
                    } catch (e: Exception) {
                        Log.e("DriverDashboardVM", "Lỗi khi lấy chi tiết chuyến đi: ${e.message}", e)
                    }
                } else {
                    Log.d("DriverDashboardVM", "Không hiển thị dialog do đang có chuyến đi")
                }
            }
        }
    }

    fun toggleOnlineStatus(context: Context, online: Boolean) {
        // Lưu context để sử dụng sau này
        if (appContext == null) {
            appContext = context.applicationContext
        }

        if (_isLoading.value) return // Tránh gọi nhiều lần khi đang loading

        // Chuyển đổi Boolean thành String status
        val status = if (online) "online" else "offline"

        viewModelScope.launch {
            _isLoading.value = true
            _pendingStatus.value = status // Lưu trạng thái đang cố gắng chuyển sang

            updateDriverStatusUseCase(status).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _isOnline.value = online
                        preferenceManager.setPreference(PreferenceKeys.DRIVER_ONLINE_STATUS, online)

                        if (online) {
                            val serviceIntent = Intent(context, LocationUpdateService::class.java)
                            context.startForegroundService(serviceIntent)
                            Log.d("DriverDashboardVM", "Đã khởi động LocationUpdateService từ toggleOnlineStatus")
                        } else {
                            context.stopService(Intent(context, LocationUpdateService::class.java))
                            Log.d("DriverDashboardVM", "Đã dừng LocationUpdateService")

                            // Đóng dialog nếu đang hiển thị
                            _showTripRequestDialog.value = false
                            _newTripRequest.value = null
                        }
                        _isLoading.value = false
                        _pendingStatus.value = null // Xóa trạng thái đang chờ
                    }
                    is Resource.Error -> {
                        // Xử lý message lỗi trống
                        val errorMsg = result.message?.takeIf { it.isNotBlank() }
                            ?: "Không thể cập nhật trạng thái. Lỗi máy chủ (500)."

                        _errorMessage.value = errorMsg
                        Log.e("DriverDashboardVM", "Lỗi cập nhật trạng thái: $errorMsg")
                        _isLoading.value = false
                        // Không xóa _pendingStatus để có thể hiển thị nút thử lại
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    else -> {}
                }
            }
        }
    }

    // Thêm hàm thử lại
    fun retryStatusUpdate(context: Context) {
        _pendingStatus.value?.let { status ->
            val online = status == "online"
            toggleOnlineStatus(context, online)
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Hàm hủy thao tác đang chờ
    fun cancelPendingStatusUpdate() {
        _pendingStatus.value = null
        _isLoading.value = false
    }

    fun acceptTrip() {
        viewModelScope.launch {
            _newTripRequest.value?.let { trip ->
                tripRepository.updateTripStatus(
                    tripId = trip.id,
                    updateTripStatusRequest = UpdateTripStatusRequest(
                        status = "accepted"
                    )
                )
                _showTripRequestDialog.value = false
            }
        }
    }

    fun rejectTrip() {
        viewModelScope.launch {
            _newTripRequest.value?.let { trip ->
                tripRepository.updateTripStatus(
                    tripId = trip.id,
                    updateTripStatusRequest = UpdateTripStatusRequest(
                        status = "rejected",
                        cancellationReason = "Driver rejected the trip"
                    )
                )
                _showTripRequestDialog.value = false
                _newTripRequest.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Đảm bảo dừng service khi ViewModel bị hủy
        appContext?.let { context ->
            if (_isOnline.value) {
                val serviceIntent = Intent(context, LocationUpdateService::class.java)
                context.stopService(serviceIntent)
                Log.d("DriverDashboardVM", "Đã dừng LocationUpdateService trong onCleared")
            }
        }
    }
}
