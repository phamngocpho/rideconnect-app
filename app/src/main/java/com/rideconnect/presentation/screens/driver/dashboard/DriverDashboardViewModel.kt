package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest.Location
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.data.remote.websocket.WebSocketManager
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.model.trip.TripStatus
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.presentation.navigation.Screen
import com.rideconnect.service.LocationUpdateService
import com.rideconnect.util.extensions.isCancelled
import com.rideconnect.util.extensions.isCompleted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDashboardViewModel @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val tripRepository: TripRepository
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

    init {
        // Lắng nghe các yêu cầu chuyến đi mới từ WebSocket
        viewModelScope.launch {
            webSocketManager.newTripRequestFlow.collect { tripResponse ->
                Log.d("DriverDashboardVM", "Nhận yêu cầu chuyến đi mới: ${tripResponse.tripId}")
                Log.d("DriverDashboardVM", "Trạng thái online hiện tại: ${_isOnline.value}")

                // Tự động chuyển sang trạng thái online nếu cần
                if (!_isOnline.value) {
                    _isOnline.value = true
                    Log.d("DriverDashboardVM", "Đã tự động chuyển sang trạng thái online")

                    // Khởi động service nếu có context
                    appContext?.let { context ->
                        val serviceIntent = Intent(context, LocationUpdateService::class.java)
                        context.startService(serviceIntent)
                        Log.d("DriverDashboardVM", "Đã khởi động LocationUpdateService")
                    }
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

    fun toggleOnlineStatus(context: Context, isOnline: Boolean) {
        // Lưu context để sử dụng sau này
        if (appContext == null) {
            appContext = context.applicationContext
        }

        val wasOffline = !_isOnline.value
        _isOnline.value = isOnline

        if (isOnline && wasOffline) {
            // Bắt đầu service theo dõi vị trí khi online
            val serviceIntent = Intent(context, LocationUpdateService::class.java)
            context.startService(serviceIntent)
            Log.d("DriverDashboardVM", "Đã khởi động LocationUpdateService từ toggleOnlineStatus")
        } else if (!isOnline) {
            // Dừng service khi offline
            val serviceIntent = Intent(context, LocationUpdateService::class.java)
            context.stopService(serviceIntent)
            Log.d("DriverDashboardVM", "Đã dừng LocationUpdateService")

            // Đóng dialog nếu đang hiển thị
            _showTripRequestDialog.value = false
            _newTripRequest.value = null
        }
    }

    fun acceptTripAndNavigate(navController: NavController) {
        viewModelScope.launch {
            _newTripRequest.value?.let { trip ->
                val result = tripRepository.updateTripStatus(
                    tripId = trip.id,
                    updateTripStatusRequest = UpdateTripStatusRequest(
                        status = "accepted"
                    )
                )

                if (result.data != null) {
                    navController.navigate(
                        Screen.DriverNavigation.createRoute(
                            originLat = trip.pickupLatitude,
                            originLng = trip.pickupLongitude,
                            destLat = trip.dropOffLatitude,
                            destLng = trip.dropOffLongitude,
                            tripId = trip.id
                        )
                    ) {
                        popUpTo(Screen.DriverHome.route)
                    }

                    _showTripRequestDialog.value = false
                }
            }
        }
    }

    private fun getCurrentDriverLocation(): Location? {
        // Implement this to get the driver's current location
        // This might come from a LocationRepository or LocationService
        // For now, returning a placeholder location
        return Location(
            latitude = 10.762622,
            longitude = 106.660172,
            address = "Current Location"
        )
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
