package com.rideconnect.presentation.components.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun LocationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    isLoading: Boolean = false,
    isCurrentLocation: Boolean = false,
    onClearClick: () -> Unit,
    onMyLocationClick: (() -> Unit)? = null,
    indicatorColor: Color,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(indicatorColor, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = if (isLoading) {
                {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                }
            } else null,
            trailingIcon = {
                if (query.isNotEmpty() && !isCurrentLocation) {
                    IconButton(onClick = onClearClick) {
                        Icon(Icons.Default.Clear, contentDescription = "Xóa")
                    }
                } else if (onMyLocationClick != null) {
                    IconButton(onClick = onMyLocationClick) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Vị trí hiện tại")
                    }
                }
            }
        )
    }
}
