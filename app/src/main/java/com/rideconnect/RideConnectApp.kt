package com.rideconnect

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mapbox.common.MapboxOptions
import com.rideconnect.presentation.components.AppBottomNavigationBar
import com.rideconnect.util.map.MapBoxConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RideConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = MapBoxConfig.getMapBoxAccessToken(this)
    }
}
