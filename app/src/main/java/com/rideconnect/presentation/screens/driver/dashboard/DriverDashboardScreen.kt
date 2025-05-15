package com.rideconnect.presentation.screens.driver.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val isOnline by viewModel.isOnline.collectAsState()
<<<<<<< HEAD
    val showTripRequestDialog by viewModel.showTripRequestDialog.collectAsState()
    val newTripRequest by viewModel.newTripRequest.collectAsState()
=======
    val isLoading by viewModel.isLoading.collectAsState()
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
    val errorMessage by viewModel.errorMessage.collectAsState()
    val pendingStatus by viewModel.pendingStatus.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Hiển thị dialog khi có lỗi và có trạng thái đang chờ
    if (errorMessage != null && pendingStatus != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelPendingStatusUpdate() },
            title = { Text("Lỗi cập nhật trạng thái") },
            text = { Text(errorMessage ?: "Đã xảy ra lỗi khi cập nhật trạng thái.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.clearErrorMessage()
                    viewModel.retryStatusUpdate(context)
                }) {
                    Text("Thử lại")
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.clearErrorMessage()
                    viewModel.cancelPendingStatusUpdate()
                }) {
                    Text("Hủy")
                }
            }
        )
    }
    // Hiển thị snackbar cho các lỗi khác (không có trạng thái đang chờ)
    else if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(
                message = errorMessage ?: "Đã xảy ra lỗi.",
                actionLabel = "Đóng",
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { DriverBottomNavigation(navController) }
    ) { paddingValues ->
        // Nội dung màn hình
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            StatusToggle(viewModel, context)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
<<<<<<< HEAD
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
=======
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ride_illustration),
                            contentDescription = "Offline Illustration",
                            modifier = Modifier.size(200.dp),
                            alpha = 0.5f
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Chuyển sang trạng thái Online để bắt đầu nhận cuốc",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
                }
            }
<<<<<<< HEAD
            if (showTripRequestDialog && newTripRequest != null) {
                TripRequestDialog(
                    trip = newTripRequest!!,
                    onAccept = { viewModel.acceptTrip() },
                    onReject = { viewModel.rejectTrip() }
=======

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun StatusToggle(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val isOnline by viewModel.isOnline.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val pendingStatus by viewModel.pendingStatus.collectAsState() // Thu thập pending status

    // Xác định trạng thái loading cho từng nút
    val isLoadingOffline = isLoading && pendingStatus == "offline"

    val isLoadingOnline = isLoading && pendingStatus == "online"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(40.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Offline
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(if (!isOnline) Color(0xFFE57373) else Color.White)
                .clickable(enabled = !isLoading) { viewModel.toggleOnlineStatus(context, false) },
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingOffline) {
                // Hiển thị loading indicator khi đang chuyển sang offline
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = if (!isOnline) Color.White else Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Offline",
                        tint = if (!isOnline) Color.White else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Offline",
                        color = if (!isOnline) Color.White else Color.Gray,
                        fontWeight = if (!isOnline) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Nút Online
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(if (isOnline) Color(0xFF7BC67E) else Color.White)
                .clickable(enabled = !isLoading) { viewModel.toggleOnlineStatus(context, true) },
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingOnline) {
                // Hiển thị loading indicator khi đang chuyển sang online
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = if (isOnline) Color.White else Color.Gray,
                    strokeWidth = 2.dp
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Online",
                        tint = if (isOnline) Color.White else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Online",
                        color = if (isOnline) Color.White else Color.Gray,
                        fontWeight = if (isOnline) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
