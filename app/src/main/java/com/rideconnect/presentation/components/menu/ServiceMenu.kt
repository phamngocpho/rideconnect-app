package com.rideconnect.presentation.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServiceMenu(
    services: List<ServiceItem>,
    onServiceSelected: (ServiceItem) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 4,
    scrollEnabled: Boolean = true // Thêm tham số để kiểm soát scrolling
) {
    // Tính số hàng cần thiết
    val rows = (services.size + columns - 1) / columns // Làm tròn lên

    // Chiều cao của mỗi item
    val itemHeight = 96.dp

    // Tính tổng chiều cao của grid
    val gridHeight = itemHeight * rows + PaddingValues(8.dp).calculateTopPadding() + PaddingValues(8.dp).calculateBottomPadding()

    Box(
        modifier = modifier.height(gridHeight)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = scrollEnabled,
            modifier = Modifier.fillMaxSize()
        ) {
            items(services) { service ->
                ServiceMenuItem(
                    title = service.title,
                    icon = service.icon,
                    onClick = { onServiceSelected(service) }
                )
            }
        }
    }
}
