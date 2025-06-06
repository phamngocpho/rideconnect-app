package com.rideconnect.presentation.screens.driver.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rideconnect.presentation.components.driver.LocationIndicator
import com.rideconnect.presentation.components.driver.OfflineContent
import com.rideconnect.presentation.components.driver.RideIllustration
import com.rideconnect.presentation.components.driver.StatusToggle
import com.rideconnect.presentation.components.driver.TripRequestDialog
import com.rideconnect.presentation.components.navigation.DriverBottomNavigation

@Composable
fun DriverDashboardScreen(
    onLogout: () -> Unit,
    navController: NavController,
    viewModel: DriverDashboardViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsState()
    val showTripRequestDialog by viewModel.showTripRequestDialog.collectAsState()
    val newTripRequest by viewModel.newTripRequest.collectAsState()

    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Toggle
                StatusToggle(viewModel = viewModel)

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị thông báo trạng thái hiện tại
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isOnline)
                            "Đang tìm kiếm khách hàng, vui lòng đợi."
                        else
                            "Bạn đang Offline, không thể nhận cuốc xe.",
                        fontSize = 14.sp,
                        color = if (isOnline) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị phần tìm kiếm chỉ khi đang online
                if (isOnline) {
                    // Location Indicator
                    LocationIndicator()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Looking for Rider Text
                    Text(
                        text = "Đang tìm kiếm hành khách",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Illustration
                    RideIllustration()
                } else {
                    // Hiển thị thông báo khi offline
                    OfflineContent()
                }

                Spacer(modifier = Modifier.weight(1f))
            }
            if (showTripRequestDialog && newTripRequest != null) {
                TripRequestDialog(
                    trip = newTripRequest!!,
                    onAccept = { viewModel.acceptTripAndNavigate(navController) },
                    onReject = { viewModel.rejectTrip() }
                )
            }
        }
    }
}