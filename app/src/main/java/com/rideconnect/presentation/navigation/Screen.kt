package com.rideconnect.presentation.navigation

import android.net.Uri
import com.rideconnect.domain.model.location.Location

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object CustomerDashboard : Screen("customer_dashboard")
    data object DriverDashboard : Screen("driver_dashboard")
    data object SearchLocation : Screen("search_location")
    data object ConfirmLocation : Screen("confirm_location")

    data object VehicleSelection : Screen("vehicle_selection_screen/{sourceLocationJson}/{destinationLocationJson}") {
        fun createRoute(sourceLocation: Location, destinationLocation: Location?): String {
            val sourceJson = Uri.encode(sourceLocation.toJson())
            val destJson = destinationLocation?.let { Uri.encode(it.toJson()) } ?: "null"
            return "vehicle_selection_screen/$sourceJson/$destJson"
        }
    }
}