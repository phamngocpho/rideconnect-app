package com.rideconnect

import android.app.Application
import com.mapbox.common.MapboxOptions
import com.rideconnect.util.map.MapBoxConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RideConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = MapBoxConfig.getMapBoxAccessToken(this)
    }
}
