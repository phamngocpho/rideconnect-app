package com.rideconnect.presentation.screens.driver.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.profile.component.ProfileInfo
import com.example.profile.component.ProfileMenuItems
import com.example.profile.component.ProfileStats
import com.example.profile.component.TopBar
import com.rideconnect.presentation.components.navigation.DriverBottomNavigation
import com.rideconnect.util.Resource


@Composable
fun DriverProfileScreen(
    viewModel: DriverProfileViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit,
    navController: NavController
) {
    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) }
    ){paddingValues ->
        val logoutState by viewModel.logoutState.collectAsState()
        LaunchedEffect(logoutState) {
            when(logoutState){
                is Resource.Success -> {
                    onLogoutSuccess()
                }
                else -> { }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Top Bar
            TopBar(
                onBackClick = { /* Xử lý khi nhấn nút Back nếu cần */ }
            )

            // Profile Info
            ProfileInfo(
                onEditProfileClick = {
                    navController?.navigate("edit_profile")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            ProfileStats()

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            ProfileMenuItems(
                onLogoutClick ={(viewModel.logout())}
            )

            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

