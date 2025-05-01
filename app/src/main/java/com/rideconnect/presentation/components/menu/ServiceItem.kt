package com.rideconnect.presentation.components.menu

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

data class ServiceItem(
    val id: String,
    val title: String,
    val icon: Any, // ImageVector, Int (drawable resource) or String (URL)
    val destinationRoute: String? = null
)
