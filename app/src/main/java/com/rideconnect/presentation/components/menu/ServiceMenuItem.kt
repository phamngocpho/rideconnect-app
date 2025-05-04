package com.rideconnect.presentation.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ServiceMenuItem(
    title: String,
    icon: Any,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .padding(4.dp)
        ) {
            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(40.dp)
                    )
                }
                is Int -> {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier.width(72.dp)
        )
    }
}

