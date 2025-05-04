package com.rideconnect.presentation.components.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RidePreferenceCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Bạn cần được yên tĩnh? Hãy thử chuyến xe yên lặng",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Xem thêm",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
