package com.rideconnect.presentation.screens.driver.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.rideconnect.presentation.components.navigation.NavigationComponent

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
@Composable
fun DriverNavigationScreen(
    navController: NavController,
    originLatitude: Double,
    originLongitude: Double,
    destinationLatitude: Double,
    destinationLongitude: Double,
    tripId: String,
    viewModel: DriverNavigationViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) {
        viewModel.setTripId(tripId)
    }
    // Sửa lại cách collect state flow
    val updateState = viewModel.updateState.collectAsState().value
    val context = LocalContext.current

    // Hiển thị loading hoặc thông báo lỗi
    when (updateState) {
        is UpdateTripState.Loading -> {
            // Hiển thị loading indicator
        }
        is UpdateTripState.Error -> {
            LaunchedEffect(updateState) {
                Toast.makeText(context, "Lỗi: ${updateState.message}", Toast.LENGTH_LONG).show()
            }
        }
        else -> {} // Không làm gì
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationComponent(
            originLatitude = originLatitude,
            originLongitude = originLongitude,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            onTripCompleted = {
                Log.d("DriverNavigationScreen", "Trip completion requested")
                viewModel.completeTripAndNavigateBack(navController)
            }
        )
    }
}
