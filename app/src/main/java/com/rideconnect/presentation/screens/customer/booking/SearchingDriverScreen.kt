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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.rideconnect.domain.model.location.Location
import com.rideconnect.presentation.components.maps.SearchingMapComponent

@Composable
fun SearchingDriverScreen(
    pickupLocation: Location,
    destinationLocation: Location,
    onBackClick: () -> Unit,
    viewModel: VehicleSelectionViewModel = hiltViewModel()
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }

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

        // Animation ở giữa màn hình
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SearchingPulseAnimation()
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
