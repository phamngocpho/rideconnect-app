package com.rideconnect.presentation.navigation

import android.net.Uri
import com.rideconnect.R
import com.rideconnect.domain.model.location.Location

sealed class Screen(val route: String) {
    data object StartApp : Screen("start_app_user")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object CustomerDashboard : Screen("customer_dashboard")
    data object DriverDashboard : Screen("driver_dashboard")
    data object SearchLocation : Screen("search_location")
    data object ConfirmLocation : Screen("confirm_location")
    data object Home : NavigationItem("home", "Home", R.drawable.home_icon)
    data object Services : NavigationItem("services", "Services", R.drawable.services_icon)
    data object Activity : NavigationItem("activity", "Activity", R.drawable.activity_icon)
    data object Profile : NavigationItem("profile", "Profile", R.drawable.person_icon)
    data object DriverHome : Screen("driver_home")
    data object DriverActivity : Screen("driver_activity")
    data object DriverProfile : Screen("driver_profile")
    data object Scanner : Screen("scanner")
    data object DriverRegister : Screen("driver_register")

    data object VehicleSelection : Screen("vehicle_selection_screen/{sourceLocationJson}/{destinationLocationJson}") {
        fun createRoute(sourceLocation: Location, destinationLocation: Location?): String {
            val sourceJson = Uri.encode(sourceLocation.toJson())
            val destJson = destinationLocation?.let { Uri.encode(it.toJson()) } ?: "null"
            return "vehicle_selection_screen/$sourceJson/$destJson"
        }
    }

    data object SearchingDriver : Screen("searching_driver/{sourceLatitude}/{sourceLongitude}/{destLatitude}/{destLongitude}/{vehicleType}")
    {
        fun createRoute(pickup: Location, destination: Location, vehicleType: String): String {
            return "searching_driver/${pickup.latitude}/${pickup.longitude}/${destination.latitude}/${destination.longitude}/${vehicleType}"
        }
    }
    object TRIP_CONFIRMATION : Screen("trip_confirmation/{latitude}/{longitude}/{radiusInKm}/{vehicleType}") {
        //  Add helper function to build the route with arguments
        fun createRoute(latitude: Double, longitude: Double, radiusInKm: Double?, vehicleType: String): String {
            return "trip_confirmation/$latitude/$longitude/${radiusInKm}/$vehicleType"
        }
    }
}