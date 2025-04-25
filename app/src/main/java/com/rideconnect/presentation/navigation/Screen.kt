package com.rideconnect.presentation.navigation


sealed class Screen(val route: String) {
    object CustomerDashboard : Screen("customer_dashboard")
    object CustomerProfile : Screen("customer_profile")
    object DestinationSearch : Screen("destination_search")
    object Map : Screen("map")
    object SavedLocations : Screen("saved_locations")
    object AddNewLocation : Screen("add_new_location")
    object PopularLocations : Screen("popular_locations")
    object Pickup : Screen("pickup") // Thêm route mới cho PickupScreen
}
