package com.rideconnect.presentation.screens.customer.booking

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.rideconnect.domain.model.location.Location
import com.rideconnect.presentation.components.maps.SearchingMapComponent

@Composable
fun SearchingDriverScreen(
    pickupLocation: Location,
    destinationLocation: Location,
    onBackClick: () -> Unit,
    navController: NavController,
    requestedVehicleType: String,
    viewModel: SearchingDriverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var mapView by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(Unit) {
        viewModel.requestTrip(pickupLocation, destinationLocation, requestedVehicleType)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                SearchingState.DRIVER_ACCEPTED, SearchingState.DRIVER_ARRIVING, SearchingState.DRIVER_ARRIVED -> {
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
                SearchingState.NO_DRIVERS_AVAILABLE -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Không tìm thấy tài xế",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onBackClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Quay lại")
                            }
                        }
                    }
                }
            }

            // Hiển thị lỗi nếu có
            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text("Lỗi: ${uiState.error}")
                }
            }
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
