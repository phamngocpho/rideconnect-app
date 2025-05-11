package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.service.LocationUpdateService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDashboardViewModel @Inject constructor() : ViewModel() {

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    fun toggleOnlineStatus(context: Context, isOnline: Boolean) {
        _isOnline.value = isOnline

        if (isOnline) {
            // Start location updates
            val serviceIntent = Intent(context, LocationUpdateService::class.java)
            context.startForegroundService(serviceIntent)
        } else {
            // Stop location updates
            context.stopService(Intent(context, LocationUpdateService::class.java))
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Ensure service is stopped when ViewModel is cleared
        if (_isOnline.value) {
            _isOnline.value = false
        }
    }
}
