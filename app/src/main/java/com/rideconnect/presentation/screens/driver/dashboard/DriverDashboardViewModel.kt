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

    // Trạng thái kết nối WebSocket
    private val _webSocketStatus = MutableStateFlow(WebSocketManager.ConnectionStatus.DISCONNECTED)
    val webSocketStatus: StateFlow<WebSocketManager.ConnectionStatus> = _webSocketStatus.asStateFlow()

    // Trạng thái cho chuyến đi mới
    private val _newTripRequest = MutableStateFlow<Trip?>(null)
    val newTripRequest: StateFlow<Trip?> = _newTripRequest.asStateFlow()

    // Trạng thái hiển thị dialog
    private val _showTripRequestDialog = MutableStateFlow(false)
    val showTripRequestDialog: StateFlow<Boolean> = _showTripRequestDialog.asStateFlow()

    // Context để sử dụng khi cần khởi động service
    private var appContext: Context? = null

    init {
        // Theo dõi trạng thái kết nối WebSocket
        viewModelScope.launch {
            webSocketManager.connectionStatus.collect { status ->
                _webSocketStatus.value = status
                Log.d("DriverDashboardVM", "Trạng thái WebSocket: $status")

                // Nếu đang online mà WebSocket bị ngắt kết nối, thử kết nối lại
                if (status == WebSocketManager.ConnectionStatus.DISCONNECTED && _isOnline.value) {
                    Log.d("DriverDashboardVM", "WebSocket bị ngắt kết nối trong khi đang online, thử kết nối lại")
                    webSocketManager.checkAndReconnectIfNeeded()
                }
            }
        }

        // Lắng nghe các yêu cầu chuyến đi mới từ WebSocket
        viewModelScope.launch {
            webSocketManager.newTripRequestFlow.collect { tripResponse ->
                handleNewTripRequest(tripResponse)
            }
        }
    }

    private fun handleNewTripRequest(tripResponse: com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse) {
        viewModelScope.launch {
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

            try {
                // Luôn lấy thông tin chuyến đi mới nhất từ repository
                val currentTrip = tripRepository.currentTrip.value
                Log.d("DriverDashboardVM", "Chuyến đi hiện tại: ${currentTrip?.id}, Trạng thái: ${currentTrip?.status}")

                // Kiểm tra xem có thể hiển thị dialog hay không
                if (currentTrip == null || currentTrip.isCompleted() || currentTrip.isCancelled()) {
                    Log.d("DriverDashboardVM", "Đủ điều kiện hiển thị dialog")

                    try {
                        val tripDetails = tripRepository.getTripDetails(tripResponse.tripId)
                        Log.d("DriverDashboardVM", "Kết quả lấy chi tiết chuyến đi: ${tripDetails.data != null}")

                        if (tripDetails.data != null) {
                            _newTripRequest.value = tripDetails.data
                            _showTripRequestDialog.value = true
                            Log.d("DriverDashboardVM", "Đã thiết lập hiển thị dialog: ${_showTripRequestDialog.value}")
                        } else {
                            Log.e("DriverDashboardVM", "Không thể lấy chi tiết chuyến đi, dữ liệu null")
                        }
                    } catch (e: Exception) {
                        Log.e("DriverDashboardVM", "Lỗi khi lấy chi tiết chuyến đi: ${e.message}", e)
                    }
                } else {
                    Log.d("DriverDashboardVM", "Không hiển thị dialog do đang có chuyến đi")
                }
            } catch (e: Exception) {
                Log.e("DriverDashboardVM", "Lỗi khi xử lý yêu cầu chuyến đi: ${e.message}", e)
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

            // Đảm bảo WebSocket được kết nối khi chuyển sang online
            if (_webSocketStatus.value != WebSocketManager.ConnectionStatus.CONNECTED) {
                Log.d("DriverDashboardVM", "Kết nối lại WebSocket khi chuyển sang online")
                webSocketManager.checkAndReconnectIfNeeded()
            }
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

    // Các phương thức khác giữ nguyên
    // ...

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

        // Không ngắt kết nối WebSocket khi ViewModel bị hủy để đảm bảo nhận thông báo liên tục
        // webSocketManager.disconnect()
    }
    fun acceptTripAndNavigate(navController: NavController) {
        viewModelScope.launch {
            try {
                _newTripRequest.value?.let { trip ->
                    Log.d("DriverDashboardVM", "Chấp nhận chuyến đi: ${trip.id}")

                    val result = tripRepository.updateTripStatus(
                        tripId = trip.id,
                        updateTripStatusRequest = UpdateTripStatusRequest(
                            status = "accepted"
                        )
                    )

                    if (result.data != null) {
                        Log.d("DriverDashboardVM", "Chấp nhận chuyến đi thành công, chuyển hướng đến màn hình điều hướng")

                        // Điều hướng đến màn hình điều hướng
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

                        // Đóng dialog
                        _showTripRequestDialog.value = false

                        // Không reset _newTripRequest để có thể truy cập thông tin chuyến đi sau khi đã chấp nhận
                        // _newTripRequest.value = null
                    } else {
                        Log.e("DriverDashboardVM", "Chấp nhận chuyến đi thất bại: ${result.message}")
                        // Hiển thị thông báo lỗi nếu cần
                    }
                } ?: run {
                    Log.e("DriverDashboardVM", "Không thể chấp nhận chuyến đi vì thông tin chuyến đi null")
                }
            } catch (e: Exception) {
                Log.e("DriverDashboardVM", "Lỗi khi chấp nhận chuyến đi: ${e.message}", e)
            }
        }
    }
    fun rejectTrip() {
        viewModelScope.launch {
            try {
                _newTripRequest.value?.let { trip ->
                    Log.d("DriverDashboardVM", "Từ chối chuyến đi: ${trip.id}")

                    val result = tripRepository.updateTripStatus(
                        tripId = trip.id,
                        updateTripStatusRequest = UpdateTripStatusRequest(
                            status = "rejected",
                            cancellationReason = "Driver rejected the trip"
                        )
                    )

                    if (result.data != null) {
                        Log.d("DriverDashboardVM", "Từ chối chuyến đi thành công")
                    } else {
                        Log.e("DriverDashboardVM", "Từ chối chuyến đi thất bại: ${result.message}")
                    }

                    // Đóng dialog và reset thông tin chuyến đi
                    _showTripRequestDialog.value = false
                    _newTripRequest.value = null
                } ?: run {
                    Log.e("DriverDashboardVM", "Không thể từ chối chuyến đi vì thông tin chuyến đi null")
                }
            } catch (e: Exception) {
                Log.e("DriverDashboardVM", "Lỗi khi từ chối chuyến đi: ${e.message}", e)
                // Vẫn đóng dialog trong trường hợp lỗi
                _showTripRequestDialog.value = false
                _newTripRequest.value = null
            }
        }
    }
}
