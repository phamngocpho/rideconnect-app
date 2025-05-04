package com.rideconnect.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier,
    selectedItem: Int = 0,
    onItemSelected: (Int) -> Unit = {}

) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
            label = { Text("Trang chủ") }
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(Icons.Default.History, contentDescription = "Hoạt động") },
            label = { Text("Hoạt động") }
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Khám phá") },
            label = { Text("Khám phá") }
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Thông báo") },
            label = { Text("Thông báo") }
        )
        NavigationBarItem(
            selected = selectedItem == 4,
            onClick = { onItemSelected(4) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Tài khoản") },
            label = { Text("Tài khoản") }
        )
    }
}
