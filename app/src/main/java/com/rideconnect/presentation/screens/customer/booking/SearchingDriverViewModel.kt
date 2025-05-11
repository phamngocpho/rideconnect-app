package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rideconnect.data.remote.dto.response.location.DriverInfo
import com.rideconnect.domain.model.location.Location
import com.rideconnect.data.remote.dto.request.location.NearbyDriversRequest
import com.rideconnect.domain.usecase.location.GetNearbyDriversUseCase
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

data class SearchingDriverUiState(
    val driverInfo: DriverInfo? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class SearchingDriverViewModel @Inject constructor(
    private val getNearbyDriversUseCase: GetNearbyDriversUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchingDriverUiState())
    val uiState: StateFlow<SearchingDriverUiState> = _uiState.asStateFlow()

    fun findNearbyDrivers(
        pickupLocation: Location,
        requestedVehicleType: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            var radius = 5.0
            repeat(6) { attempt ->
                try {
                    Log.d("SearchingDriverVM", "Finding drivers at lat=${pickupLocation.latitude}, lng=${pickupLocation.longitude}, type=$requestedVehicleType, attempt=${attempt + 1}, radius=$radius")
                    val request = NearbyDriversRequest(
                        latitude = pickupLocation.latitude,
                        longitude = pickupLocation.longitude,
                        radiusInKm = radius,
                        vehicleType = requestedVehicleType
                    )
                    val response = getNearbyDriversUseCase(request)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("SearchingDriverVM", "Response successful: drivers=${responseBody?.drivers?.size ?: 0}, count=${responseBody?.count ?: 0}")
                        val foundDrivers = responseBody?.drivers ?: emptyList()
                        if (foundDrivers.isNotEmpty()) {
                            val firstDriver = foundDrivers.first()
                            Log.d("SearchingDriverVM", "First driver: id=${firstDriver.id}, latitude=${firstDriver.latitude}, longitude=${firstDriver.longitude}, plateNumber=${firstDriver.plateNumber}, vehicleType=${firstDriver.vehicleType}, name=${firstDriver.name}, rating=${firstDriver.rating}, estimatedArrivalTime=${firstDriver.estimatedArrivalTime}")
                            _uiState.value = _uiState.value.copy(
                                driverInfo = firstDriver,
                                isLoading = false
                            )
                            return@launch
                        }
                    } else {
                        Log.e("SearchingDriverVM", "Server error: ${response.code()} ${response.message()}")
                    }
                    radius += 1.0
                } catch (e: Exception) {
                    Log.e("SearchingDriverVM", "Error: ${e.message}", e)
                }
                if (attempt < 5) delay(5_000)
            }
            _uiState.value = _uiState.value.copy(
                error = "Không tìm thấy tài xế phù hợp sau nhiều lần thử. Vui lòng thử lại hoặc thay đổi loại xe.",
                isLoading = false
            )
        }
    }
}
