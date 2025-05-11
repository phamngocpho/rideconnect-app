package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.rideconnect.data.remote.dto.response.location.DriverInfo
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
    viewModel: SearchingDriverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var mapView by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(Unit) {
        viewModel.findNearbyDrivers(pickupLocation, requestedVehicleType)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SearchingMapComponent(
            modifier = Modifier.fillMaxSize(),
            searchRadius = 1000.0,
            centerLocation = Point.fromLngLat(pickupLocation.longitude, pickupLocation.latitude),
            onMapViewCreated = { mapView = it }
        )

        // Back button
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

        // Content Display
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SearchingPulseAnimation()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Đang tìm tài xế...", fontSize = 20.sp)
                }
            } else if (uiState.driverInfo != null) {
                DriverFoundContent(
                    driverInfo = uiState.driverInfo!!,
                    onStartTrip = {
                        // Navigate to the next screen
                        navController.navigate(
                            "${Screen.TRIP_CONFIRMATION.route}/${pickupLocation.latitude}/${pickupLocation.longitude}/${destinationLocation.latitude}/${destinationLocation.longitude}/${requestedVehicleType}"
                        )
                    }
                )
            } else if (uiState.error != null) {
                Text("Error: ${uiState.error}")
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
        initialValue = 0.8f, // Tăng giá trị ban đầu để vòng tròn to hơn
        targetValue = 10f,    // Tăng giá trị đích để vòng tròn to hơn khi giãn ra
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
                .size(30.dp) // Kích thước cơ bản của vòng tròn
                .scale(pulseScale)
                .background(
                    // Màu xanh nhạt
                    color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun DriverFoundContent(
    driverInfo: DriverInfo,
    onStartTrip: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đã tìm thấy tài xế!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("ID: ${driverInfo.id}")
        Text("Số xe: ${driverInfo.plateNumber}")
        Text("Loại xe: ${driverInfo.vehicleType}")
        Text("Khoảng cách: ${driverInfo.distance.toInt()} mét")
        Text("Thời gian đến dự kiến: ${driverInfo.estimatedArrivalTime ?: "Chưa có"} phút")
        driverInfo.name?.let { Text("Tên: $it") } ?: Text("Tên: Không có")
        driverInfo.rating?.let { Text("Đánh giá: $it") } ?: Text("Đánh giá: Chưa có")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onStartTrip) {
            Text("Bắt đầu chuyến đi")
        }
    }
}
