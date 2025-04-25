package com.rideconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.drive_app.ui.screens.CustomerServiceScreen
import com.rideconnect.presentation.screens.customer.dashboard.CustomerDashboardScreen
import com.rideconnect.presentation.screens.customer.profile.CustomerProfileScreen
import com.rideconnect.presentation.screens.customer.trip.*

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route
    ) {
        composable(NavigationItem.Home.route) {
            CustomerDashboardScreen()
        }
        composable(NavigationItem.Services.route) {
            CustomerServiceScreen()
        }
        composable(NavigationItem.Activity.route) {
            HistoryTripScreen(navController = navController)
        }
        // Giữ nguyên route cho trip_details
        composable(
            "trip_details/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")
            CurrentTripScreen(tripId = tripId ?: "", navController = navController)
        }

        // Thêm route mới để chuyển từ My Trips sang Ride History
        composable("ride_history") {
            HistoryTripScreen(navController = navController)
        }
        composable(NavigationItem.Profile.route) {
            CustomerProfileScreen()
        }
    }

}

