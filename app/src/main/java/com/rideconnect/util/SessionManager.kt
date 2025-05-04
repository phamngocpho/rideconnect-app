package com.rideconnect.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired: StateFlow<Boolean> = _sessionExpired.asStateFlow()

    fun setSessionExpired(expired: Boolean) {
        _sessionExpired.value = expired
    }

    fun resetSessionExpired() {
        _sessionExpired.value = false
    }
}
