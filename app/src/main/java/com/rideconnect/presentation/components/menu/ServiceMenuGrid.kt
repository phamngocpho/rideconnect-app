package com.rideconnect.presentation.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServiceMenuGrid(
    services: List<ServiceItem>,
    onServiceSelected: (ServiceItem) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 4,
    maxItems: Int? = null,
    scrollEnabled: Boolean = true // Thêm tham số để kiểm soát scrolling
) {
    var showAllItems by remember { mutableStateOf(false) }

    val displayedServices = if (maxItems != null && !showAllItems && services.size > maxItems) {
        services.take(maxItems - 1)
    } else {
        services
    }

    // Tính số hàng cần thiết
    val itemCount = displayedServices.size + (if (maxItems != null && services.size > maxItems && !showAllItems) 1 else 0)
    val rows = (itemCount + columns - 1) / columns // Làm tròn lên

    // Chiều cao của mỗi item (dựa trên ServiceMenuItem)
    val itemHeight = 96.dp // 56.dp (icon) + 4.dp (spacer) + ~36.dp (text 2 dòng và padding)

    // Tính tổng chiều cao của grid
    val gridHeight = itemHeight * rows + PaddingValues(8.dp).calculateTopPadding() + PaddingValues(8.dp).calculateBottomPadding()

    // Sử dụng Box với height cố định để bọc LazyVerticalGrid
    Box(
        modifier = modifier.height(gridHeight)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = scrollEnabled, // Vô hiệu hóa scrolling khi nằm trong LazyColumn
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayedServices) { service ->
                ServiceMenuItem(
                    title = service.title,
                    icon = service.icon,
                    onClick = { onServiceSelected(service) }
                )
            }

            if (maxItems != null && services.size > maxItems && !showAllItems) {
                item {
                    ServiceMenuItem(
                        title = "Xem thêm",
                        icon = Icons.Default.MoreHoriz,
                        onClick = { showAllItems = true }
                    )
                }
            }
        }
    }
}
