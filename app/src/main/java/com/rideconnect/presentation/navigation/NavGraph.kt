package com.rideconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rideconnect.presentation.screens.customer.location.DestinationSearchScreen
import com.rideconnect.presentation.screens.customer.location.PickupScreen
import com.rideconnect.presentation.screens.customer.location.PopularLocationsScreen
import com.rideconnect.presentation.screens.customer.location.SavedLocationsScreen
import com.rideconnect.presentation.screens.customer.location.SearchResultsScreen
import com.rideconnect.presentation.screens.customer.trip.BookTripScreen
import com.rideconnect.presentation.screens.customer.trip.RideDetailsScreen
import com.rideconnect.presentation.screens.shared.map.MapScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "trip_screen"
    ) {
        // Màn hình đặt chuyến
        composable("trip_screen") {
            BookTripScreen(
                onNavigateToMapScreen = {
                    navController.navigate("map_screen")
                }
            )
        }

        // Màn hình bản đồ
        composable("map_screen") {
            MapScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchClick = {
                    navController.navigate("destination_search")
                },
                onLocationSelected = { location ->
                    // Lưu địa điểm đã chọn và chuyển đến màn hình pickup
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_location", location)
                    navController.navigate("pickup_screen")
                }
            )
        }

        // Màn hình chi tiết chuyến đi
        composable("ride_details_screen") {
            RideDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onBookRideClick = { rideType ->
                    // Xử lý khi người dùng đặt xe (Saver hoặc Motor)
                    // Lưu loại xe đã chọn
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_ride_type", rideType)
                    // Hiện tại chỉ hiển thị giao diện, nên quay lại màn hình chính
                    navController.navigate("trip_screen") {
                        popUpTo("trip_screen") { inclusive = true }
                    }
                },
//                onChangePaymentMethod = {
//                    // Chỉ hiển thị giao diện, không có backend
//                    // Có thể thêm màn hình phương thức thanh toán sau
//                },
//                onApplyCoupon = {
//                    // Chỉ hiển thị giao diện, không có backend
//                    // Có thể thêm màn hình mã giảm giá sau
//                }
            )
        }

        // Màn hình tìm kiếm địa điểm
        composable("destination_search") {
            DestinationSearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSelectLocationClick = {
                    // Chuyển về màn hình bản đồ với địa điểm đã chọn
                    navController.navigate("map_screen") {
                        popUpTo("map_screen") { inclusive = true }
                    }
                },
                onSkipClick = {
                    // Xử lý khi người dùng nhấn Skip
                    navController.popBackStack()
                },
                onSearchTextChanged = { searchText ->
                    if (searchText.isNotEmpty()) {
                        // Nếu có văn bản tìm kiếm, lưu và chuyển đến màn hình kết quả tìm kiếm
                        navController.previousBackStackEntry?.savedStateHandle?.set("search_text", searchText)
                        navController.navigate("search_results")
                    }
                },
                onTabClick = { tabIndex ->
                    when (tabIndex) {
                        0 -> { /* Xử lý tab Recent */ }
                        1 -> { /* Xử lý tab Suggested */ }
                        2 -> {
                            navController.navigate("saved_locations")
                        }
                    }
                },
                onPopularLocationsClick = {
                    // Chuyển đến màn hình địa điểm phổ biến
                    navController.navigate("popular_locations")
                }
            )
        }

        // Màn hình kết quả tìm kiếm
        composable("search_results") {
            SearchResultsScreen(
                searchText = navController.previousBackStackEntry?.savedStateHandle?.get<String>("search_text") ?: "",
                onBackClick = {
                    navController.popBackStack()
                },
                onLocationClick = { location ->
                    // Xử lý khi người dùng chọn một địa điểm
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_location", location)
                    navController.popBackStack()
                }
            )
        }

        // Màn hình hiển thị địa điểm phổ biến
        composable("popular_locations") {
            PopularLocationsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLocationClick = { location ->
                    // Xử lý khi người dùng chọn một địa điểm phổ biến
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_location", location)
                    navController.popBackStack()
                }
            )
        }

        // Màn hình địa điểm đã lưu
        composable("saved_locations") {
            SavedLocationsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLocationClick = { locationName ->
                    // Xử lý khi người dùng chọn một địa điểm đã lưu
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_location", locationName)
                    navController.popBackStack()
                },
                onAddNewClick = {
                    // Chuyển đến màn hình thêm địa điểm mới
                    navController.navigate("add_new_location")
                }
            )
        }

        // Màn hình thêm địa điểm mới
        composable("add_new_location") {
            MapScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchClick = {
                    navController.navigate("destination_search")
                },
                onLocationSelected = { location ->
                    // Lưu địa điểm mới và quay lại màn hình trước đó
                    navController.previousBackStackEntry?.savedStateHandle?.set("new_location", location)
                    navController.popBackStack()
                }
            )
        }

        // Màn hình pickup (điểm đón)
        composable("pickup_screen") {
            PickupScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onConfirmPickup = {
                    // Sau khi xác nhận điểm đón, chuyển đến màn hình chi tiết chuyến đi
                    navController.navigate("ride_details_screen")
                }
            )
        }
        // Trong NavGraph.kt
//        composable("ride_details_screen") {
//            RideDetailsScreen(
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onBookRideClick = { rideType ->
//                    // Xử lý khi người dùng đặt xe
//                    // Có thể truyền tham số nếu cần
//                    navController.navigate("confirmation_screen")
//                }
//            )
//        }

    }
}
