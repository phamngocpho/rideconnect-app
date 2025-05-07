package com.rideconnect.presentation.navigation

import com.rideconnect.R
import okhttp3.Route

sealed class NavigationItem(val route: String, val title: String, val iconResId: Int) {
    object Home : NavigationItem("home", "Home", R.drawable.home_icon)
    object Services : NavigationItem("services", "Services", R.drawable.services_icon)
    object Activity : NavigationItem("activity", "Activity", R.drawable.activity_icon)
    object Profile : NavigationItem("profile", "Profile", R.drawable.person_icon)
}