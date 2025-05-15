package com.rideconnect.presentation.navigation

import android.net.Uri
import com.rideconnect.R
import com.rideconnect.domain.model.location.Location

sealed class Screen(val route: String, val title: String? = null, val iconResId: Int? = null) {
    data object StartApp : Screen("start_app_user")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object CustomerDashboard : Screen("customer_dashboard")
    data object DriverDashboard : Screen("driver_dashboard")
    data object SearchLocation : Screen("search_location")
    data object ConfirmLocation : Screen("confirm_location")
    data object DriverHome : Screen("driver_home")
    data object DriverActivity : Screen("driver_activity")
    data object DriverProfile : Screen("driver_profile")
    data object Scanner : Screen("scanner")
    data object DriverRegister : Screen("driver_register")

    // Gộp NavigationItem vào Screen
    data object Home : Screen("home", "Home", R.drawable.home_icon)
    data object Services : Screen("services", "Services", R.drawable.services_icon)
    data object Activity : Screen("activity", "Activity", R.drawable.activity_icon)
    data object Profile : Screen("profile", "Profile", R.drawable.person_icon)

    data object VehicleSelection : Screen("vehicle_selection_screen/{sourceLocationJson}/{destinationLocationJson}") {
        fun createRoute(sourceLocation: Location, destinationLocation: Location?): String {
            val sourceJson = Uri.encode(sourceLocation.toJson())
            val destJson = destinationLocation?.let { Uri.encode(it.toJson()) } ?: "null"
            return "vehicle_selection_screen/$sourceJson/$destJson"
        }
    }

    data object SearchingDriver : Screen("searching_driver/{sourceLatitude}/{sourceLongitude}/{destLatitude}/{destLongitude}/{vehicleType}?sourceAddress={sourceAddress}&destAddress={destAddress}") {
        fun createRoute(pickup: Location, destination: Location, vehicleType: String): String {
            val sourceAddressEncoded = Uri.encode(pickup.address ?: "")
            val destAddressEncoded = Uri.encode(destination.address ?: "")
            return "searching_driver/${pickup.latitude}/${pickup.longitude}/${destination.latitude}/${destination.longitude}/${vehicleType}?sourceAddress=${sourceAddressEncoded}&destAddress=${destAddressEncoded}"

        }
    }

    data object TRIP_CONFIRMATION : Screen("trip_confirmation/{latitude}/{longitude}/{radiusInKm}/{vehicleType}") {

        fun createRoute(latitude: Double, longitude: Double, radiusInKm: Double?, vehicleType: String): String {
            return "trip_confirmation/$latitude/$longitude/${radiusInKm}/$vehicleType"
        }
    }

    data object DriverNavigation : Screen("driver_navigation/{originLat}/{originLng}/{destLat}/{destLng}/{tripId}") {
        fun createRoute(originLat: Double, originLng: Double, destLat: Double, destLng: Double, tripId: String): String {
            return "driver_navigation/$originLat/$originLng/$destLat/$destLng/$tripId"
        }
    }
}