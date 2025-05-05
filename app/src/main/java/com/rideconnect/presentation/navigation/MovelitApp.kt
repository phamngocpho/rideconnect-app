package com.example.testapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.screens.auth.login.Login
import com.rideconnect.presentation.screens.auth.login.LoginScreen
import com.rideconnect.presentation.screens.driver.dashboard.DrivingLicenseScreen
import com.rideconnect.presentation.screens.driver.dashboard.GovernmentIdScreen
import com.rideconnect.presentation.screens.driver.dashboard.MovelitViewModel
import com.rideconnect.presentation.screens.driver.dashboard.PersonalDetailsScreen
import com.rideconnect.presentation.screens.driver.dashboard.ProfilePictureScreen
import com.rideconnect.presentation.screens.driver.dashboard.SuccessScreen

@Composable
fun MovelitApp() {
    val navController = rememberNavController()
    val viewModel: MovelitViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            LoginScreen(
                viewModel = viewModel,
                onLoginClick = { navController.navigate("login") },
                onCreateAccountClick = { navController.navigate("personal_details")}
            )
        }

        composable("login"){
            Login(
                onForgotClick = {navController.navigate("")},
                onLoginClick = {navController.navigate("")},
                onGoogleClick = {navController.navigate("")},
                onFacebookClick = {navController.navigate("")},
                onAppleClick = {navController.navigate("")},
                onSignUpClick = { navController.navigate("personal_details")},
                viewModel = viewModel
            )
        }

        composable("personal_details") {
            PersonalDetailsScreen(
                viewModel = viewModel,
                onNextClick = { navController.navigate("profile_picture") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("profile_picture") {
            ProfilePictureScreen(
                viewModel = viewModel,
                onNextClick = { navController.navigate("government_id") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("government_id") {
            GovernmentIdScreen(
                viewModel = viewModel,
                onNextClick = { navController.navigate("driving_license") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("driving_license") {
            DrivingLicenseScreen(
                viewModel = viewModel,
                onDoneClick = { navController.navigate("success") },  // Sửa từ onNextClick thành onDoneClick để phù hợp với tham số trong DrivingLicenseScreen
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("success") {
            SuccessScreen(
                onFinishClick = {
                    navController.popBackStack(
                        route = "login",
                        inclusive = false
                    )
                }
            )
        }
    }
}
