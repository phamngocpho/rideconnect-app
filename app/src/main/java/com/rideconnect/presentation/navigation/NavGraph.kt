package com.rideconnect.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rideconnect.domain.model.location.Location
import com.rideconnect.presentation.components.AppBottomNavigationBar
import com.rideconnect.presentation.screens.auth.start.StartApp
import com.rideconnect.presentation.screens.auth.login.LoginScreen
import com.rideconnect.presentation.screens.auth.register.RegisterScreen
import com.rideconnect.presentation.screens.customer.booking.SearchingDriverScreen
import com.rideconnect.presentation.screens.customer.booking.VehicleSelectionScreen
import com.rideconnect.presentation.screens.customer.dashboard.CustomerDashboardScreen
import com.rideconnect.presentation.screens.customer.location.ConfirmLocationScreen
import com.rideconnect.presentation.screens.customer.location.SearchLocationScreen
import com.rideconnect.presentation.screens.customer.profile.CustomerProfileScreen
import com.rideconnect.presentation.screens.customer.service.CustomerServiceScreen
import com.rideconnect.presentation.screens.customer.trip.CurrentTripScreen
import com.rideconnect.presentation.screens.customer.trip.HistoryTripScreen
import com.rideconnect.presentation.screens.driver.dashboard.DriverDashboardScreen
import com.rideconnect.presentation.screens.driver.profile.DriverProfileScreen
import com.rideconnect.presentation.screens.driver.trips.ActiveTripScreeen


@Composable
fun RideConnectNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.StartApp.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.StartApp.route) {
            StartApp(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onCreateAccountClick = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToCustomerHome = {
                    // Điều hướng đến màn hình Customer Dashboard nếu là CUSTOMER
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.StartApp.route) { inclusive = true }
                    }
                },
                onNavigateToDriverHome = {
                    // Điều hướng đến màn hình Driver Dashboard nếu là DRIVER
                    navController.navigate(Screen.DriverHome.route) {
                        popUpTo(Screen.StartApp.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToCustomerDashboard = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToDriverDashboard = {
                    navController.navigate(Screen.DriverHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.CustomerDashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.CustomerDashboard.route) {
            CustomerDashboardScreen(
                onNavigate = { destination ->
                    if (destination == "login") {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else {
                        navController.navigate(destination)
                    }
                }
            )
        }

        composable(Screen.SearchLocation.route) {
            SearchLocationScreen(
                onBackClick = { navController.navigateUp() },
                onLocationSelected = { source, destination ->
                    if (source != null) {
                        // Táº¡o URI cho navigation vá»›i cÃ¡c tham sá»‘
                        val sourceLatitude = source.latitude.toFloat()
                        val sourceLongitude = source.longitude.toFloat()
                        val sourceAddress = Uri.encode(source.address)
                        val sourceName = Uri.encode(source.name)

                        val baseRoute = "${Screen.ConfirmLocation.route}/$sourceLatitude/$sourceLongitude/$sourceAddress/$sourceName"

                        val route = if (destination != null) {
                            val destLatitude = destination.latitude.toFloat()
                            val destLongitude = destination.longitude.toFloat()
                            val destAddress = Uri.encode(destination.address)
                            val destName = Uri.encode(destination.name)
                            "$baseRoute?destinationLatitude=$destLatitude&destinationLongitude=$destLongitude&destinationAddress=$destAddress&destinationName=$destName"
                        } else {
                            baseRoute
                        }

                        navController.navigate(route)
                    }
                }
            )
        }

        composable(
            route = "${Screen.ConfirmLocation.route}/{sourceLatitude}/{sourceLongitude}/{sourceAddress}/{sourceName}?destinationLatitude={destinationLatitude}&destinationLongitude={destinationLongitude}&destinationAddress={destinationAddress}&destinationName={destinationName}",
            arguments = listOf(
                navArgument("sourceLatitude") { type = NavType.FloatType },
                navArgument("sourceLongitude") { type = NavType.FloatType },
                navArgument("sourceAddress") { type = NavType.StringType },
                navArgument("sourceName") { type = NavType.StringType },
                navArgument("destinationLatitude") { type = NavType.FloatType; defaultValue = 0f },
                navArgument("destinationLongitude") { type = NavType.FloatType; defaultValue = 0f },
                navArgument("destinationAddress") { type = NavType.StringType; nullable = true },
                navArgument("destinationName") { type = NavType.StringType; nullable = true },
            )
        ) { backStackEntry ->
            val sourceLatitude = backStackEntry.arguments?.getFloat("sourceLatitude") ?: 0f
            val sourceLongitude = backStackEntry.arguments?.getFloat("sourceLongitude") ?: 0f
            val sourceAddress = backStackEntry.arguments?.getString("sourceAddress") ?: ""
            val sourceName = backStackEntry.arguments?.getString("sourceName") ?: ""

            val destinationLatitude = backStackEntry.arguments?.getFloat("destinationLatitude")
            val destinationLongitude = backStackEntry.arguments?.getFloat("destinationLongitude")
            val destinationAddress = backStackEntry.arguments?.getString("destinationAddress")
            val destinationName = backStackEntry.arguments?.getString("destinationName")

            val sourceLocation = Location(
                latitude = sourceLatitude.toDouble(),
                longitude = sourceLongitude.toDouble(),
                address = sourceAddress,
                name = sourceName
            )

            val destinationLocation = if (destinationLatitude != 0f && destinationLongitude != 0f) {
                Location(
                    latitude = destinationLatitude!!.toDouble(),
                    longitude = destinationLongitude!!.toDouble(),
                    address = destinationAddress ?: "",
                    name = destinationName ?: ""
                )
            } else null

            ConfirmLocationScreen(
                sourceLocation = sourceLocation,
                destinationLocation = destinationLocation,
                onBackClick = { navController.navigateUp() },
                onConfirmLocation = { source, destination ->
                    navController.navigate(Screen.VehicleSelection.createRoute(source, destination))
                }
            )
        }


        composable(
            route = Screen.VehicleSelection.route,
            arguments = listOf(
                navArgument("sourceLocationJson") {
                    type = NavType.StringType
                },
                navArgument("destinationLocationJson") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val sourceLocationJson = backStackEntry.arguments?.getString("sourceLocationJson")?.let { Uri.decode(it) }
            val destinationLocationJson = backStackEntry.arguments?.getString("destinationLocationJson")?.let { Uri.decode(it) }

            val sourceLocation = sourceLocationJson?.let { Location.fromJson(it) }
            val destinationLocation = if (destinationLocationJson != "null") {
                destinationLocationJson?.let { Location.fromJson(it) }
            } else {
                null
            }

            if (sourceLocation != null) {
                VehicleSelectionScreen(
                    pickupLocation = sourceLocation,
                    destinationLocation = destinationLocation ?: sourceLocation,
                    onBackClick = { navController.popBackStack() },
                    onBookingConfirmed = { vehicleType, vehicleId, paymentMethodId ->
                        navController.navigate(
                            Screen.SearchingDriver.createRoute(
                                pickup = sourceLocation,
                                destination = destinationLocation ?: sourceLocation,
                                vehicleType = vehicleType
                            )
                        )
                    },
                    navController = navController
                )
            }
        }

        composable(
            route = Screen.SearchingDriver.route,
            arguments = listOf(
                navArgument("sourceLatitude") { type = NavType.FloatType },
                navArgument("sourceLongitude") { type = NavType.FloatType },
                navArgument("destLatitude") { type = NavType.FloatType },
                navArgument("destLongitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val sourceLatitude = backStackEntry.arguments?.getFloat("sourceLatitude") ?: 0f
            val sourceLongitude = backStackEntry.arguments?.getFloat("sourceLongitude") ?: 0f
            val destLatitude = backStackEntry.arguments?.getFloat("destLatitude") ?: 0f
            val destLongitude = backStackEntry.arguments?.getFloat("destLongitude") ?: 0f
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: "" // Extract vehicleType

            SearchingDriverScreen(
                pickupLocation = Location(
                    latitude = sourceLatitude.toDouble(),
                    longitude = sourceLongitude.toDouble()
                ),
                destinationLocation = Location(
                    latitude = destLatitude.toDouble(),
                    longitude = destLongitude.toDouble()
                ),
                onBackClick = { navController.navigateUp() },
                navController = navController, // Pass navController here
                requestedVehicleType = vehicleType,
            )
        }


        composable(route = Screen.DriverHome.route) {
            DriverDashboardScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable(route = Screen.Home.route) {
            CustomerDashboardScreen(
                onNavigate = { destination ->
                    if (destination == "search_location") {
                        navController.navigate(Screen.SearchLocation.route)
                    } else {
                        navController.navigate(destination)
                    }
                }
            )
        }
        composable(Screen.Services.route) {
            CustomerServiceScreen()
        }
        composable(Screen.Activity.route) {
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
        composable(Screen.Profile.route) {
            CustomerProfileScreen(
                onLogoutSuccess = {
                    navController.navigate(Screen.StartApp.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        // Trong NavGraph
        composable(route = Screen.CustomerDashboard.route) {
            Scaffold(
                bottomBar = {
                    AppBottomNavigationBar(navController = navController)
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CustomerDashboardScreen(
                        onNavigate = { destination ->
                            navController.navigate(destination)
                        }
                    )
                }
            }
        }

//        composable(route = Screen.DriverHome.route) {
//            DriverDashboardScreen(
//                onLogout = {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(navController.graph.id) { inclusive = true }
//                    }
//                }
//            )
//        }
        composable(route = Screen.DriverActivity.route) {
            ActiveTripScreeen(
                navController = navController
            ) // Cần tạo màn hình này
        }
//
        composable(route = Screen.DriverProfile.route) {
            DriverProfileScreen(
                onLogoutSuccess = {
                    navController.navigate(Screen.StartApp.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },// Cần tạo màn hình này
                navController = navController
            )
        }

    }

}
