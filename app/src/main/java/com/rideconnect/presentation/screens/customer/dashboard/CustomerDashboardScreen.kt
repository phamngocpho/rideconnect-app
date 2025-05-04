package com.rideconnect.presentation.screens.customer.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.presentation.components.dashboard.*
import com.rideconnect.presentation.components.menu.ServiceItem
import com.rideconnect.presentation.components.menu.ServiceMenuGrid
import com.rideconnect.presentation.components.navigation.AppBottomNavigation

@Composable
fun CustomerDashboardScreen(
    viewModel: CustomerDashboardViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = 0,
                onItemSelected = { /* Xử lý khi chọn tab */ }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header với background mây
            item {
                DashboardHeader(
                    userName = "Phạm",
                    temperature = "27°C",
                    clubName = "VinClub"
                )
            }

            // Thanh tìm kiếm "Bạn muốn đi đâu?"
            item {
                SearchBar(
                    text = "Bạn muốn đi đâu?",
                    onClick = { onNavigate("search_location") },
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Lưới dịch vụ
            item {
                val services = listOf(
                    ServiceItem(
                        id = "car",
                        title = "Ô tô",
                        icon = Icons.Default.DirectionsCar
                    ),
                    ServiceItem(
                        id = "bike",
                        title = "Xe máy",
                        icon = Icons.Default.TwoWheeler
                    ),
                    ServiceItem(
                        id = "delivery",
                        title = "Giao hàng",
                        icon = Icons.Default.LocalShipping
                    ),
                    ServiceItem(
                        id = "airport",
                        title = "Ô tô sân bay",
                        icon = Icons.Default.FlightTakeoff
                    ),
                    ServiceItem(
                        id = "vinfast",
                        title = "Mua xe VinFast",
                        icon = Icons.Default.CarRental
                    ),
                    ServiceItem(
                        id = "tour",
                        title = "Xanh Tour",
                        icon = Icons.Default.Tour
                    ),
                    ServiceItem(
                        id = "intercity",
                        title = "Xanh Liên Tỉnh",
                        icon = Icons.Default.Route
                    ),
                    ServiceItem(
                        id = "taxi",
                        title = "Taxi",
                        icon = Icons.Default.LocalTaxi
                    )
                )

                ServiceMenuGrid(
                    services = services,
                    onServiceSelected = { service ->
                        // Xử lý khi người dùng chọn dịch vụ
                        when (service.id) {
                            "car" -> { /* Xử lý khi chọn Ô tô */ }
                            "bike" -> { /* Xử lý khi chọn Xe máy */ }
                            // Xử lý các dịch vụ khác
                        }
                    },
                    columns = 4,
                    scrollEnabled = false // Vô hiệu hóa scrolling của grid khi nằm trong LazyColumn
                )
            }

            // Indicator cho menu
            item {
                PageIndicator(
                    pageCount = 3,
                    currentPage = 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            // Banner quảng cáo
            item {
                PromotionBanner(
                    title = "ĐÀ NẴNG TRONG XANH",
                    promotionValues = listOf("50K", "50K"),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }

            // Xanh Thổ Địa section
            item {
                ListItem(
                    headlineContent = { Text("Xanh Thổ Địa - Top 1 thịnh hành") },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
