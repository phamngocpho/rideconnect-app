package com.rideconnect

import android.app.Application
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.navigation.NavGraph
import com.rideconnect.presentation.theme.RideConnectApplicationTheme
import androidx.compose.material3.Typography
import com.rideconnect.presentation.theme.Typography


class MainActivity : ComponentActivity() {

    // Khai báo các quyền cần thiết trực tiếp thay vì sử dụng PermissionUtils
    private val REQUIRED_LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Khai báo requestPermissionLauncher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // Tất cả quyền đã được cấp
            initLocationFeatures()
        } else {
            // Một số quyền bị từ chối
            Toast.makeText(
                this,
                "Ứng dụng cần quyền vị trí để hoạt động đúng",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Kiểm tra và yêu cầu quyền vị trí nếu cần
        if (!hasLocationPermission()) {
            requestPermissionLauncher.launch(REQUIRED_LOCATION_PERMISSIONS)
        } else {
            initLocationFeatures()
        }

        setContent {
            RideConnectApplicationTheme {
                RideConnectAppUI()
            }
        }
    }

    // Thay thế PermissionUtils.hasLocationPermission với phương thức local
    private fun hasLocationPermission(): Boolean {
        return REQUIRED_LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initLocationFeatures() {
        // Khởi tạo các tính năng liên quan đến vị trí
        // Chỉ là placeholder, không có code backend thực tế
    }
}

@Preview(showBackground = true)
@Composable
fun RideConnectAppPreview() {
    RideConnectApplicationTheme {
        RideConnectAppUI()
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
        typography = Typography,
        content = content
    )
}

@Composable
fun RideConnectAppUI() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}
