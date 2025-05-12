package com.rideconnect.presentation.screens.driver.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.usecase.auth.LogoutUseCase
import com.rideconnect.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DriverProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _logoutState = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading()
            try {
                logoutUseCase()
                _logoutState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = Resource.Error(e.message ?: "Logout failed")
            }
        }
    }
}