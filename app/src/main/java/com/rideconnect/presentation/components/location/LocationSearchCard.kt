package com.rideconnect.presentation.components.location

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LocationSearchCard(
    sourceQuery: String,
    onSourceQueryChange: (String) -> Unit,
    destinationQuery: String,
    onDestinationQueryChange: (String) -> Unit,
    isSourceLoading: Boolean,
    isCurrentLocation: Boolean,
    onClearSourceClick: () -> Unit,
    onClearDestinationClick: () -> Unit,
    onMyLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Source location input
            LocationSearchBar(
                query = sourceQuery,
                onQueryChange = onSourceQueryChange,
                placeholder = "Nhập điểm đón",
                isLoading = isSourceLoading,
                isCurrentLocation = isCurrentLocation,
                onClearClick = onClearSourceClick,
                onMyLocationClick = onMyLocationClick,
                indicatorColor = Color.Blue
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Destination input
            LocationSearchBar(
                query = destinationQuery,
                onQueryChange = onDestinationQueryChange,
                placeholder = "Nhập địa điểm đến",
                onClearClick = onClearDestinationClick,
                indicatorColor = Color.Red
            )
        }
    }
}
