package com.rideconnect

import android.app.Application
import com.mapbox.common.MapboxOptions
import com.rideconnect.util.map.MapBoxConfig
import dagger.hilt.android.HiltAndroidApp
import com.rideconnect.data.remote.websocket.WebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class RideConnectApp : Application() {
    @Inject
    lateinit var webSocketManager: WebSocketManager
    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = MapBoxConfig.getMapBoxAccessToken(this)
        initializeWebSocket()
    }

    private fun initializeWebSocket() {
        CoroutineScope(Dispatchers.IO).launch {
            // Kết nối WebSocket với URL cơ sở của API
            webSocketManager.connect(BuildConfig.API_BASE_URL)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        // Ngắt kết nối WebSocket khi ứng dụng kết thúc
        webSocketManager.disconnect()
    }
}
