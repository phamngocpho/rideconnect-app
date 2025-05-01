package com.rideconnect.presentation.components.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.vehicle.Vehicle

@Composable
fun VehicleList(
    vehicles: List<Vehicle>,
    isLoading: Boolean,
    onVehicleSelected: (Vehicle) -> Unit,
    selectedVehicleId: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (vehicles.isEmpty()) {
            Text(
                text = "Không có phương tiện nào khả dụng",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(vehicles) { vehicle ->
                    VehicleItem(
                        vehicle = vehicle,
                        isSelected = vehicle.id == selectedVehicleId,
                        onClick = { onVehicleSelected(vehicle) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
