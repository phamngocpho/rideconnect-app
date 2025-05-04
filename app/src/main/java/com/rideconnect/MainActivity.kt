package com.rideconnect

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.navigation.RideConnectNavGraph
import com.rideconnect.presentation.ui.theme.RideConnectTheme
import dagger.hilt.android.AndroidEntryPoint

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

        // Yêu cầu quyền truy cập vị trí khi ứng dụng khởi động
        requestLocationPermission()

        setContent {
            RideConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    RideConnectNavGraph(navController = navController)
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
