package com.rideconnect.presentation.screens.customer.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.rideconnect.domain.model.location.Location
import com.rideconnect.presentation.components.location.LocationInfoCard
import com.rideconnect.presentation.components.maps.MapComponent
import com.rideconnect.presentation.ui.theme.RosemaryFont
import com.rideconnect.util.map.MapBoxConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmLocationScreen(
    viewModel: ConfirmLocationViewModel = hiltViewModel(),
    sourceLocation: Location?,
    destinationLocation: Location?,
    onBackClick: () -> Unit,
    onConfirmLocation: (source: Location, destination: Location?) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mapboxAccessToken = remember { MapBoxConfig.getMapBoxAccessToken(context) }

    val sourcePoint = remember(sourceLocation) {
        sourceLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        }
    }

    // Tạo và cấu hình MapViewportState
    val mapViewportState = rememberMapViewportState().apply {
        sourcePoint?.let { point ->
            setCameraOptions {
                center(point)
                zoom(15.0)
            }
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Xác nhận điểm đón",
                        fontFamily = RosemaryFont
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sourceLocation?.let { source ->
                        onConfirmLocation(source, destinationLocation)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = "Vị trí hiện tại"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sử dụng component Map đã tách
            MapComponent(
                mapViewportState = mapViewportState,
                showSourceMarker = sourceLocation != null,
                modifier = Modifier.fillMaxSize()
            )

            // Sử dụng component LocationInfoCard đã tách
            LocationInfoCard(
                sourceLocation = sourceLocation,
                destinationLocation = destinationLocation,
                onConfirmClick = {
                    sourceLocation?.let { source ->
                        onConfirmLocation(source, destinationLocation)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}
