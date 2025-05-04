package com.rideconnect.presentation.screens.customer.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.location.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmLocationViewModel @Inject constructor() : ViewModel() {
    private val _sourceLocation = MutableStateFlow<Location?>(null)
    val sourceLocation: StateFlow<Location?> = _sourceLocation

    private val _destinationLocation = MutableStateFlow<Location?>(null)
    val destinationLocation: StateFlow<Location?> = _destinationLocation

    fun setSourceLocation(location: Location) {
        _sourceLocation.value = location
    }

    fun setDestinationLocation(location: Location?) {
        _destinationLocation.value = location
    }
}