package com.rideconnect.presentation.components.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.AirportShuttle
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.DirectionsCarFilled
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rideconnect.domain.model.vehicle.VehicleType
import androidx.compose.material3.MaterialTheme

@Composable
fun VehicleIcon(
    vehicleType: VehicleType,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val icon: ImageVector = when (vehicleType) {
        VehicleType.CAR -> Icons.Filled.DirectionsCar
        VehicleType.CAR_PREMIUM -> Icons.Outlined.DirectionsCarFilled
        VehicleType.BIKE -> Icons.Filled.TwoWheeler
        VehicleType.BIKE_PLUS -> Icons.Filled.TwoWheeler
        VehicleType.TAXI -> Icons.Outlined.LocalTaxi
        VehicleType.VAN -> Icons.Outlined.AirportShuttle
    }

    Icon(
        imageVector = icon,
        contentDescription = "Vehicle ${vehicleType.name}",
        modifier = modifier,
        tint = tint
    )
}
