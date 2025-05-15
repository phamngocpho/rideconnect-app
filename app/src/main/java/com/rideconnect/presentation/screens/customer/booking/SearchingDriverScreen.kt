package com.rideconnect.presentation.screens.customer.booking

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.rideconnect.domain.model.location.Location
import com.rideconnect.presentation.components.maps.SearchingMapComponent
import com.rideconnect.presentation.navigation.Screen

@Composable
fun SearchingDriverScreen(
    pickupLocation: Location,
    destinationLocation: Location,
    onBackClick: () -> Unit,
    navController: NavController,
    requestedVehicleType: String,
    viewModel: SearchingDriverViewModel = hiltViewModel(),
    ratingViewModel: RatingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val ratingUiState by ratingViewModel.uiState.collectAsState()
    var mapView by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(Unit) {
        viewModel.requestTrip(pickupLocation, destinationLocation, requestedVehicleType)
    }

    // Set trip details to rating view model when trip is completed
    LaunchedEffect(uiState.searchingState) {
        if (uiState.searchingState == SearchingState.COMPLETED) {
            uiState.tripDetails?.let { ratingViewModel.setTrip(it) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hiển thị bản đồ khi chưa hoàn thành chuyến đi
        if (uiState.searchingState != SearchingState.COMPLETED) {
            SearchingMapComponent(
                modifier = Modifier.fillMaxSize(),
                searchRadius = 1000.0,
                centerLocation = Point.fromLngLat(pickupLocation.longitude, pickupLocation.latitude),
                onMapViewCreated = { mapView = it }
            )

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Hiển thị thông tin địa chỉ đón và đến
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Điểm đón:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = pickupLocation.address ?: "Không có địa chỉ",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Điểm đến:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = destinationLocation.address ?: "Không có địa chỉ",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.searchingState) {
                SearchingState.SEARCHING -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SearchingPulseAnimation()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Đang tìm tài xế...", fontSize = 20.sp)

                            // Hiển thị thông báo lỗi nếu có
//                            uiState.error?.let { error ->
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Text(
//                                    text = error,
//                                    color = MaterialTheme.colorScheme.error,
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
                        }
                    }
                }
                SearchingState.DRIVER_FOUND -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SearchingPulseAnimation()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Đã tìm thấy tài xế, đang chờ phản hồi...", fontSize = 20.sp)
                        }
                    }
                }
                SearchingState.DRIVER_ACCEPTED, SearchingState.DRIVER_ARRIVING,
                SearchingState.DRIVER_ARRIVED, SearchingState.IN_PROGRESS -> {
                    // Hiển thị giao diện khi tài xế chấp nhận chuyến đi
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        DriverAcceptedScreen(
                            tripDetails = uiState.tripDetails!!,
                            pickupLocation = pickupLocation,
                            dropOffLocation = destinationLocation,
                            onCallDriver = { /* Xử lý khi người dùng gọi tài xế */ },
                            onChatDriver = { /* Xử lý khi người dùng chat với tài xế */ },
                            onCancelTrip = { /* Xử lý khi người dùng hủy chuyến đi */ }
                        )
                    }
                }
                SearchingState.COMPLETED -> {
                    // Hiển thị giao diện đánh giá tài xế
                    RateDriverScreen(
                        trip = uiState.tripDetails!!,
                        onRateSubmit = { rating, feedback ->
                            // Xử lý khi người dùng gửi đánh giá
                            ratingViewModel.submitRating(rating, feedback)
                        },
                        onBackClick = onBackClick
                    )
                    // Hiển thị thông báo khi đánh giá thành công
                    if (ratingUiState.isSuccess) {
                        LaunchedEffect(Unit) {
                            // Điều hướng về màn hình chính (Home) sau khi đánh giá thành công
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                }

                SearchingState.NO_DRIVERS_AVAILABLE -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Icon thông báo
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Tiêu đề thông báo
                                Text(
                                    text = "Không tìm thấy tài xế",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Nội dung thông báo
                                Text(
                                    text = "Vui lòng thử lại sau hoặc chọn phương tiện khác",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Nút quay lại
                                Button(
                                    onClick = onBackClick,
                                    modifier = Modifier.fillMaxWidth(0.7f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Quay lại")
                                }
                            }
                        }
                    }
                }

            }

            // Hiển thị lỗi nếu có
//            if (uiState.error != null) {
//                Snackbar(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                ) {
//                    Text("Lỗi: ${uiState.error}")
//                }
//            }
//
//            // Hiển thị lỗi đánh giá nếu có
//            if (ratingUiState.error != null) {
//                Snackbar(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                ) {
//                    Text("Lỗi: ${ratingUiState.error}")
//                }
//            }
        }
    }
}

@Composable
private fun SearchingPulseAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .scale(pulseScale)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                    shape = CircleShape
                )
        )
    }
}
