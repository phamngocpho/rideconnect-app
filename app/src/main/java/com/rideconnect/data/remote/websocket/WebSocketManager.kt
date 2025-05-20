package com.rideconnect.data.remote.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rideconnect.data.remote.dto.response.notification.NotificationsResponse
import com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val webSocket: RideConnectWebSocket,
    private val gson: Gson
) {
    private val TAG = "WebSocketManager"

    // Trạng thái kết nối
    enum class ConnectionStatus {
        CONNECTED, CONNECTING, DISCONNECTED
    }

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    // Flow cho các loại thông báo
    private val _notificationFlow = MutableSharedFlow<NotificationsResponse>()
    val notificationFlow = _notificationFlow.asSharedFlow()

    private val _tripUpdatesFlow = MutableSharedFlow<TripDetailsResponse>()
    val tripUpdatesFlow = _tripUpdatesFlow.asSharedFlow()

    private val _newTripRequestFlow = MutableSharedFlow<TripDetailsResponse>(
        replay = 1,
        extraBufferCapacity = 10
    )
    val newTripRequestFlow = _newTripRequestFlow.asSharedFlow()

    private val _tripAcceptedFlow = MutableSharedFlow<TripDetailsResponse>()
    val tripAcceptedFlow = _tripAcceptedFlow.asSharedFlow()

    private val _tripStartedFlow = MutableSharedFlow<TripDetailsResponse>()
    val tripStartedFlow = _tripStartedFlow.asSharedFlow()

    private val _tripCompletedFlow = MutableSharedFlow<TripDetailsResponse>()
    val tripCompletedFlow = _tripCompletedFlow.asSharedFlow()

    private val _tripCancelledFlow = MutableSharedFlow<TripDetailsResponse>()
    val tripCancelledFlow = _tripCancelledFlow.asSharedFlow()

    private val _driverLocationFlow = MutableSharedFlow<TripDetailsResponse.DriverLocation>()
    val driverLocationFlow = _driverLocationFlow.asSharedFlow()

    // Định nghĩa cấu trúc tin nhắn WebSocket
    data class WebSocketMessage(
        val type: String,
        val data: JsonElement
    )

    // Các loại tin nhắn WebSocket
    object WebSocketMessageType {
        const val TRIP_REQUEST = "trip_request"
        const val TRIP_STATUS_UPDATE = "trip_status_update"
        const val LOCATION_UPDATE = "location_update"
        const val NOTIFICATION = "notification"
    }

    // Cơ chế reconnect
    private var reconnectAttempt = 0
    private val maxReconnectAttempts = 5
    private val reconnectHandler = Handler(Looper.getMainLooper())
    private var baseUrlCache: String? = null

    fun connect(baseUrl: String) {
        // Lưu URL để dùng cho reconnect
        baseUrlCache = baseUrl

        // Kiểm tra trạng thái kết nối hiện tại
        if (_connectionStatus.value == ConnectionStatus.CONNECTING ||
            _connectionStatus.value == ConnectionStatus.CONNECTED) {
            Log.d(TAG, "WebSocket đã kết nối hoặc đang kết nối")
            return
        }

        _connectionStatus.value = ConnectionStatus.CONNECTING
        Log.d(TAG, "Bắt đầu kết nối WebSocket đến $baseUrl")

        webSocket.connect(baseUrl, object : WebSocketListener() {
            override fun onConnectionEstablished() {
                Log.d(TAG, "WebSocket connection established")
                _connectionStatus.value = ConnectionStatus.CONNECTED
                // Reset reconnect attempt khi kết nối thành công
                reconnectAttempt = 0
            }

            override fun onMessageReceived(message: String) {
                try {
                    Log.d(TAG, "WebSocketListener: WebSocket message received: $message")
                    val webSocketMessage = gson.fromJson(message, WebSocketMessage::class.java)

                    when (webSocketMessage.type) {
                        WebSocketMessageType.NOTIFICATION -> {
                            val notification = gson.fromJson(webSocketMessage.data, NotificationsResponse::class.java)
                            _notificationFlow.tryEmit(notification)
                        }

                        WebSocketMessageType.TRIP_REQUEST -> {
                            val tripDetails = gson.fromJson(webSocketMessage.data, TripDetailsResponse::class.java)
                            Log.d(TAG, "Nhận yêu cầu chuyến đi mới: ${tripDetails.tripId}")
                            _newTripRequestFlow.tryEmit(tripDetails)
                        }

                        WebSocketMessageType.TRIP_STATUS_UPDATE -> {
                            val tripDetails = gson.fromJson(webSocketMessage.data, TripDetailsResponse::class.java)
                            Log.d(TAG, "Nhận cập nhật trạng thái chuyến đi: ${tripDetails.tripId}, trạng thái: ${tripDetails.status}")

                            // Phân loại theo trạng thái trip
                            when (tripDetails.status.lowercase()) {
                                "pending" -> _newTripRequestFlow.tryEmit(tripDetails)
                                "accepted" -> _tripAcceptedFlow.tryEmit(tripDetails)
                                "started" -> _tripStartedFlow.tryEmit(tripDetails)
                                "completed" -> _tripCompletedFlow.tryEmit(tripDetails)
                                "cancelled" -> _tripCancelledFlow.tryEmit(tripDetails)
                            }

                            // Emit cập nhật trạng thái chung
                            _tripUpdatesFlow.tryEmit(tripDetails)
                        }

                        WebSocketMessageType.LOCATION_UPDATE -> {
                            val driverLocation = gson.fromJson(webSocketMessage.data, TripDetailsResponse.DriverLocation::class.java)
                            _driverLocationFlow.tryEmit(driverLocation)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing WebSocket message: ${e.message}", e)
                }
            }

            override fun onConnectionClosed() {
                Log.d(TAG, "WebSocket connection closed")
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
                // Thử kết nối lại khi kết nối bị đóng
                scheduleReconnect()
            }

            override fun onConnectionFailed(throwable: Throwable) {
                Log.e(TAG, "WebSocket connection failed: ${throwable.message}")
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
                scheduleReconnect()
            }
        })
    }

    private fun scheduleReconnect() {
        if (reconnectAttempt >= maxReconnectAttempts) {
            Log.e(TAG, "Đã vượt quá số lần thử kết nối lại tối đa")
            reconnectAttempt = 0
            return
        }

        val delayMillis = calculateBackoffDelay(reconnectAttempt)
        reconnectAttempt++

        Log.d(TAG, "Lên lịch kết nối lại sau $delayMillis ms (lần thử $reconnectAttempt)")
        reconnectHandler.postDelayed({
            baseUrlCache?.let { url ->
                connect(url)
            } ?: run {
                Log.e(TAG, "Không thể kết nối lại do không có URL")
            }
        }, delayMillis)
    }

    private fun calculateBackoffDelay(attempt: Int): Long {
        // Exponential backoff: 1s, 2s, 4s, 8s, 16s
        return 1000L * (1L shl attempt)
    }

    fun disconnect() {
        webSocket.disconnect()
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        reconnectHandler.removeCallbacksAndMessages(null)
        reconnectAttempt = 0
    }

    // Phương thức để kiểm tra kết nối và kết nối lại nếu cần
    fun checkAndReconnectIfNeeded() {
        if (_connectionStatus.value != ConnectionStatus.CONNECTED && baseUrlCache != null) {
            Log.d(TAG, "Kiểm tra và kết nối lại WebSocket")
            connect(baseUrlCache!!)
        }
    }
}
