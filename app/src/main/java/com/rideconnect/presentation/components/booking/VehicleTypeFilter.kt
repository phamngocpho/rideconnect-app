package com.rideconnect.presentation.components.booking

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rideconnect.domain.model.vehicle.VehicleType

/**
 * Component hiển thị bộ lọc loại phương tiện dưới dạng các tab có thể cuộn ngang.
 *
 * @param selectedType Loại phương tiện đang được chọn, null nếu hiển thị tất cả
 * @param onTypeSelected Callback được gọi khi người dùng chọn một loại phương tiện
 * @param modifier Modifier để áp dụng cho component
 */
@Composable
fun VehicleTypeFilter(
    selectedType: VehicleType?,
    onTypeSelected: (VehicleType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val vehicleTypes = listOf(
        null to "Tất cả",
        VehicleType.CAR to "Xe hơi",
        VehicleType.CAR_PREMIUM to "Xe sang",
        VehicleType.BIKE to "Xe máy",
        VehicleType.BIKE_PLUS to "Xe máy+",
        VehicleType.TAXI to "Taxi",
        VehicleType.VAN to "Xe 7 chỗ"
    )

    ScrollableTabRow(
        selectedTabIndex = vehicleTypes.indexOfFirst { it.first == selectedType }.takeIf { it >= 0 } ?: 0,
        modifier = modifier
    ) {
        vehicleTypes.forEachIndexed { _, (type, name) ->
            Tab(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                text = { Text(name) }
            )
        }
    }
}
