package com.rideconnect.presentation.screens.driver.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TripHistoryUiState>(TripHistoryUiState.Loading)
    val uiState: StateFlow<TripHistoryUiState> = _uiState.asStateFlow()

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false
    private var isLoading = false

    init {
        loadInitialTrips()
    }

    fun loadInitialTrips() {
        currentPage = 0
        _trips.value = emptyList()
        isLastPage = false
        loadMoreTrips()
    }

    fun loadMoreTrips() {
        if (isLoading || isLastPage) return
        isLoading = true

        viewModelScope.launch {
            _uiState.value = TripHistoryUiState.Loading

            when (val result = tripRepository.getTripHistory(currentPage, PAGE_SIZE)) {
                is Resource.Success -> {
                    val newTrips = result.data ?: emptyList()
                    if (newTrips.isEmpty()) {
                        isLastPage = true
                    } else {
                        val updatedList = _trips.value + newTrips
                        _trips.value = updatedList
                        currentPage++
                    }
                    _uiState.value = TripHistoryUiState.Success
                }
                is Resource.Error -> {
                    _uiState.value = TripHistoryUiState.Error(result.message ?: "Đã xảy ra lỗi")
                }
                // Thêm xử lý cho các trạng thái còn lại
                is Resource.Idle -> {
                    // Không làm gì, giữ trạng thái hiện tại
                }
                is Resource.Loading -> {
                    _uiState.value = TripHistoryUiState.Loading
                }
            }

            isLoading = false
        }
    }

    fun retryLoading() {
        if (currentPage == 0) {
            loadInitialTrips()
        } else {
            loadMoreTrips()
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}

sealed class TripHistoryUiState {
    object Loading : TripHistoryUiState()
    object Success : TripHistoryUiState()
    data class Error(val message: String) : TripHistoryUiState()
}
