package com.rideconnect

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.navigation.RideConnectNavGraph
import com.rideconnect.presentation.ui.theme.RideConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requiredLocationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(
                this,
                "Đã được cấp quyền truy cập vị trí",
                Toast.LENGTH_SHORT
            ).show()
            initLocationFeatures()
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
        enableEdgeToEdge()

        if (!hasLocationPermission()) {
            locationPermissionRequest.launch(requiredLocationPermissions)
        } else {
            initLocationFeatures()
        }

        setContent {
            RideConnectApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val navController = rememberNavController()
                            RideConnectNavGraph(navController = navController)
                        }
                    }
                )
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return requiredLocationPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initLocationFeatures() {
        // Khởi tạo các tính năng liên quan đến vị trí
    }
}

@Composable
fun RideConnectApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFF81C784),
            tertiary = Color(0xFFA5D6A7)
        )
        else -> lightColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFF81C784),
            tertiary = Color(0xFFA5D6A7),
            background = Color.White,
            surface = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = com.rideconnect.presentation.ui.theme.Typography,
        content = content
    )
}