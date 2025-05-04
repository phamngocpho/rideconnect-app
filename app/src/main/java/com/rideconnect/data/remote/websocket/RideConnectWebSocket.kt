package com.rideconnect.data.remote.websocket

import android.util.Log
import com.rideconnect.domain.repository.AuthRepository
import com.rideconnect.util.constants.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideConnectWebSocket @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val authRepository: AuthRepository
) {
    private var webSocket: WebSocket? = null
    private var webSocketListener: WebSocketListener? = null
    private var isConnected = false
    private var baseUrl: String = ""

    fun connect(baseUrl: String, listener: WebSocketListener) {
        if (isConnected) return

        this.baseUrl = baseUrl
        this.webSocketListener = listener

        CoroutineScope(Dispatchers.IO).launch {
            val token = authRepository.getAuthToken().first()
            val userId = authRepository.getUserId().first()

            if (token.isEmpty() || userId.isEmpty()) {
                Log.e("WebSocket", "Cannot connect: missing auth token or user ID")
                return@launch
            }

            val wsUrl = baseUrl.replace("http", "ws") + "ws?token=$token&userId=$userId"

            val request = Request.Builder()
                .url(wsUrl)
                .build()

            webSocket = okHttpClient.newWebSocket(request, listener)
            isConnected = true
        }
    }

    fun reconnect() {
        if (webSocketListener == null || baseUrl.isEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            delay(AppConstants.WS_RECONNECT_INTERVAL)
            disconnect()
            connect(baseUrl, webSocketListener!!)
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
        isConnected = false
    }

    fun sendMessage(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }
}
