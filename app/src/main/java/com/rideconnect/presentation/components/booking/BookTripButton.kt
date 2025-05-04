package com.rideconnect.presentation.components.booking

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.vehicle.Vehicle

@Composable
fun BookTripButton(
    selectedVehicle: Vehicle?,
    onBookRide: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onBookRide,
        enabled = selectedVehicle != null,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = selectedVehicle?.let { "Đặt xe ${it.name}" } ?: "Chọn phương tiện",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
