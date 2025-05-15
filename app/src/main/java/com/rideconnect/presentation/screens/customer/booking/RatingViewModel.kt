package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.usecase.rating.CreateRatingUseCase
import com.rideconnect.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RatingUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val trip: Trip? = null
)

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val createRatingUseCase: CreateRatingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatingUiState())
    val uiState: StateFlow<RatingUiState> = _uiState.asStateFlow()

    fun setTrip(trip: Trip) {
        _uiState.value = _uiState.value.copy(trip = trip)
    }

    fun submitRating(rating: Int, comment: String?) {
        viewModelScope.launch {
            val tripId = _uiState.value.trip?.id ?: return@launch

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                // Gửi đánh giá
                val ratingResult = createRatingUseCase(
                    tripId = tripId,
                    rating = rating,
                    comment = comment
                )

                // Xử lý kết quả đánh giá
                when (ratingResult) {
                    is Resource.Success -> {
                        Log.d("RatingViewModel", "Rating submitted successfully")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    is Resource.Error -> {
                        Log.e("RatingViewModel", "Error submitting rating: ${ratingResult.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Không thể gửi đánh giá: ${ratingResult.message}"
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                Log.e("RatingViewModel", "Exception during rating submission: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Lỗi không xác định: ${e.message}"
                )
            }
        }
    }
}
