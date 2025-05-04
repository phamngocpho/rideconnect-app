package com.rideconnect.presentation.navigation


import com.rideconnect.R
import okhttp3.Route

sealed class NavigationItem(val route: String, val title: String, val iconResId: Int) {
    object Home : NavigationItem("home", "Home", R.drawable.home)
    object Services : NavigationItem("services", "Services", R.drawable.service)
    object Activity : NavigationItem("activity", "Activity", R.drawable.activity)
    object Profile : NavigationItem("profile", "Profile", R.drawable.person_icon)
}