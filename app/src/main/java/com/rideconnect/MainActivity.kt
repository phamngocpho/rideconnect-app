package com.rideconnect

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.navigation.RideConnectNavGraph
import com.rideconnect.presentation.ui.theme.RideConnectTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rideconnect.presentation.components.AppBottomNavigationBar
import com.rideconnect.presentation.navigation.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            Toast.makeText(
                this,
                "Đã được cấp quyền truy cập vị trí",
                Toast.LENGTH_SHORT
            ).show()
            // Không cần gọi refreshCurrentLocation ở đây vì các ViewModel sẽ được khởi tạo lại khi chuyển màn hình
        } else {
            Toast.makeText(
                this,
                "Cần quyền truy cập vị trí để sử dụng đầy đủ tính năng",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()

        setContent {
            RideConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chỉ sử dụng một NavHost duy nhất
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Xác định khi nào hiển thị bottom navigation
                    val shouldShowBottomBar = currentRoute in listOf(
                        Screen.Home.route,
                        Screen.Services.route,
                        Screen.Activity.route,
                        Screen.Profile.route
                    )

                    Scaffold(
                        bottomBar = {
                            if (shouldShowBottomBar) {
                                AppBottomNavigationBar(navController = navController)
                            }
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            RideConnectNavGraph(
                                navController = navController,
                                startDestination = Screen.StartApp.route
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}