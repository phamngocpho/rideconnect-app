package com.rideconnect.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.rideconnect.R
import com.rideconnect.domain.usecase.driver.UpdateLocationUseCase
import com.rideconnect.util.Resource
import com.rideconnect.util.constants.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationUpdateService : Service() {
    private val TAG = "LocationUpdateService"

    @Inject
    lateinit var updateLocationUseCase: UpdateLocationUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Khởi tạo LocationUpdateService")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RideConnect Driver")
            .setContentText("Sharing your location...")
            .setSmallIcon(R.drawable.ride_illustration)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "onCreate: Đã khởi tạo foreground service")

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val point = Point.fromLngLat(location.longitude, location.latitude)
                    Log.d(TAG, "onLocationResult: Nhận vị trí mới - lat=${location.latitude}, lng=${location.longitude}")

                    serviceScope.launch {
                        updateLocationUseCase(point).onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    Log.d(TAG, "Cập nhật vị trí thành công: ${result.data}")
                                }
                                is Resource.Error -> {
                                    Log.e(TAG, "Cập nhật vị trí thất bại: ${result.message}")
                                }
                                is Resource.Loading -> {
                                    Log.d(TAG, "Đang cập nhật vị trí...")
                                }

                                is Resource.Idle -> TODO()
                            }
                        }.launchIn(this)
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Bắt đầu cập nhật vị trí")
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            AppConstants.LOCATION_UPDATE_INTERVAL
        ).build()

        Log.d(TAG, "startLocationUpdates: Cài đặt cập nhật vị trí mỗi ${AppConstants.LOCATION_UPDATE_INTERVAL}ms")

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Log.d(TAG, "startLocationUpdates: Đã đăng ký nhận cập nhật vị trí")
        } catch (e: SecurityException) {
            Log.e(TAG, "startLocationUpdates: Lỗi quyền truy cập vị trí", e)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Dừng cập nhật vị trí")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Location Updates",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d(TAG, "createNotificationChannel: Đã tạo notification channel")
    }

    companion object {
        private const val CHANNEL_ID = "location_channel"
        private const val NOTIFICATION_ID = 1
    }
}
