package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.presentation.components.booking.*
import com.rideconnect.presentation.components.common.LoadingStateContent
import com.rideconnect.presentation.components.maps.RouteMapComponent

@Composable
fun VehicleSelectionScreen(
    pickupLocation: Location,
    destinationLocation: Location,
    onBackClick: () -> Unit,
    onBookingConfirmed: (vehicleType: String, vehicleId: String, paymentMethodId: String) -> Unit,
    viewModel: VehicleSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val isLoadingRoute by viewModel.isLoadingRoute.collectAsState()
    var mapView by remember { mutableStateOf<MapView?>(null) }

    val pickupPoint = remember(pickupLocation) {
        Point.fromLngLat(pickupLocation.longitude, pickupLocation.latitude)
    }
    val scope = rememberCoroutineScope()

    Log.d("VehicleSelectScreen", "Recomposing with pickup=$pickupLocation, dest=$destinationLocation")

    LaunchedEffect(pickupLocation, destinationLocation) {
        Log.d("VehicleSelectScreen", "LaunchedEffect: pickup=$pickupLocation, dest=$destinationLocation")
        viewModel.setPickupAndDestination(pickupLocation, destinationLocation)
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            Log.e("VehicleSelectScreen", "Error: $error")
            viewModel.clearError()
        }
    }

    val mapViewportState = remember {
        MapViewportState().apply {
            setCameraOptions {
                center(
                    Point.fromLngLat(
                        pickupLocation.longitude,
                        pickupLocation.latitude
                    )
                )
                zoom(14.0)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Map container
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
            ) {
                LoadingStateContent(
                    isLoading = isLoadingRoute,
                    error = null,
                    onRetry = { },
                    content = {
                        RouteMapComponent(
                            modifier = Modifier.fillMaxSize(),
                            routePoints = routePoints,
                            routeColor = "#4A90E2",
                            routeWidth = 8.0,
                            pickupLocation = pickupPoint,
                            onMapViewCreated = { mapView = it }
                        )
                    }
                )
            }

            // Vehicle selection panel
            Surface(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Route info header
                    LocationRouteHeader(
                        sourceLocation = pickupLocation,
                        destinationLocation = destinationLocation,
                        visible = false,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Vehicle type filter
                    VehicleTypeFilter(
                        selectedType = uiState.selectedVehicleType,
                        onTypeSelected = { vehicleType ->
                            Log.d("VehicleSelectScreen", "VehicleTypeFilter selected: $vehicleType")
                            viewModel.filterByVehicleType(vehicleType)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    // Vehicle list
                    Log.d("VehicleSelectScreen", "Available vehicles size: ${uiState.availableVehicles.size}")
                    VehicleList(
                        vehicles = uiState.availableVehicles,
                        isLoading = uiState.isLoading,
                        onVehicleSelected = { vehicle ->
                            Log.d("VehicleSelectScreen", "Vehicle selected: ${vehicle.id}")
                            viewModel.selectVehicle(vehicle)
                        },
                        selectedVehicleId = uiState.selectedVehicle?.id,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) { vehicle: Vehicle ->
                        Column {
                            Text("Fare: ${vehicle.price}")
                            Text("Wait Time: ${vehicle.estimatedPickupTime} mins")
                        }
                    }

                    // Bottom options row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RidePreferenceCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            onClick = { /* Show additional options */ }
                        )

                        uiState.selectedPaymentMethod?.let { selectedMethod ->
                            PaymentMethodSelector(
                                availablePaymentMethods = uiState.paymentMethods,
                                selectedMethod = selectedMethod,
                                onPaymentMethodSelected = { paymentMethod ->
                                    Log.d("VehicleSelectScreen", "PaymentMethod selected: ${paymentMethod.id}")
                                    viewModel.selectPaymentMethod(paymentMethod.id)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )
                        }
                    }

                    // Book trip button
                    BookTripButton(
                        selectedVehicle = uiState.selectedVehicle,
                        onBookRide = {
                            uiState.selectedVehicle?.let { vehicle ->
                                uiState.selectedPaymentMethod?.let { paymentMethod ->
                                    Log.d("VehicleSelectScreen", "Booking confirmed: vehicleType=${vehicle.type}, vehicleId=${vehicle.id}, paymentMethodId=${paymentMethod.id}")
                                    onBookingConfirmed(
                                        vehicle.type.toString(),
                                        vehicle.id,
                                        paymentMethod.id
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(56.dp)
                    )
                }
            }
        }

        // Back button overlay
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Quay lại",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RidePreferenceCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeOff,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Bạn cần được yên tĩnh?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}