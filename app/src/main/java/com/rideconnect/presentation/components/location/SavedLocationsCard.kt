package com.rideconnect.presentation.components.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.location.SavedLocation

@Composable
fun SavedLocationsCard(
    savedLocations: List<SavedLocation>,
    onLocationSelected: (SavedLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Địa điểm đã lưu",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (savedLocations.isEmpty()) {
                // Hiển thị khi không có địa điểm đã lưu
                ListItem(
                    headlineContent = { Text("Chưa có địa điểm nào được lưu") },
                    supportingContent = { Text("Các địa điểm bạn lưu sẽ hiển thị ở đây") }
                )
            } else {
                // Hiển thị danh sách địa điểm đã lưu
                savedLocations.forEachIndexed { index, location ->
                    ListItem(
                        headlineContent = { Text(location.name) },
                        supportingContent = { Text(location.address) },
                        leadingContent = {
                            Icon(
                                imageVector = when (location.type) {
                                    SavedLocation.Type.HOME -> Icons.Default.Home
                                    SavedLocation.Type.WORK -> Icons.Default.Business
                                    else -> Icons.Default.LocationOn
                                },
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable { onLocationSelected(location) }
                    )

                    if (index < savedLocations.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
