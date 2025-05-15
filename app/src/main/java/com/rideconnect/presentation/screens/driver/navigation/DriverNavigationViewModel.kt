package com.rideconnect.presentation.screens.driver.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.domain.model.trip.TripStatus
import com.rideconnect.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DriverNavigationViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _updateState = MutableStateFlow<UpdateTripState>(UpdateTripState.Idle)
    val updateState: StateFlow<UpdateTripState> = _updateState.asStateFlow()

    // Lưu trữ tripId
    private var tripId: String? = null

    fun setTripId(id: String) {
        tripId = id
        Log.d("DriverNavigationVM", "Trip ID set: $id")
    }

    fun completeTripAndNavigateBack(navController: NavController) {
        viewModelScope.launch {
            _updateState.value = UpdateTripState.Loading

            Log.d("DriverNavigationVM", "Attempting to complete trip")

            // Sử dụng tripId đã lưu thay vì phụ thuộc vào currentTrip
            val currentTripId = tripId

            if (currentTripId != null) {
                try {
                    // Đầu tiên, cập nhật trạng thái thành "started"
                    Log.d("DriverNavigationVM", "Calling API to update trip status to started for trip $currentTripId")
                    val startRequest = UpdateTripStatusRequest(status = "started")
                    val startResult = tripRepository.updateTripStatus(currentTripId, startRequest)

                    if (startResult.message != null) {
                        Log.w("DriverNavigationVM", "Warning when updating to started: ${startResult.message}")
                        // Không return ở đây, vẫn tiếp tục thử cập nhật thành completed
                    }

                    // Sau đó, cập nhật trạng thái thành "completed"
                    Log.d("DriverNavigationVM", "Calling API to update trip status to completed for trip $currentTripId")
                    val completeRequest = UpdateTripStatusRequest(status = "completed")
                    val result = tripRepository.updateTripStatus(currentTripId, completeRequest)

                    if (result.data != null) {
                        Log.d("DriverNavigationVM", "Trip completed successfully")
                        _updateState.value = UpdateTripState.Success
                        // Navigate back
                        navController.navigateUp()
                    } else {
                        Log.e("DriverNavigationVM", "Error completing trip: ${result.message}")
                        _updateState.value = UpdateTripState.Error(result.message ?: "Lỗi không xác định")
                    }
                } catch (e: Exception) {
                    Log.e("DriverNavigationVM", "Exception when completing trip", e)
                    _updateState.value = UpdateTripState.Error(e.message ?: "Lỗi không xác định")
                }
            } else {
                Log.e("DriverNavigationVM", "No trip ID available")
                _updateState.value = UpdateTripState.Error("Không tìm thấy ID chuyến đi")
            }
        }
    }
}


// Thêm sealed class để theo dõi trạng thái
sealed class UpdateTripState {
    object Idle : UpdateTripState()
    object Loading : UpdateTripState()
    object Success : UpdateTripState()
    data class Error(val message: String) : UpdateTripState()
}
