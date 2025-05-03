package com.rideconnect.presentation.screens.customer.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.domain.model.vehicle.VehicleType
import com.rideconnect.domain.usecase.booking.GetAvailableVehiclesUseCase
import com.rideconnect.domain.usecase.direction.GetRoutePointsUseCase
import com.rideconnect.domain.usecase.payment.GetPaymentMethodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

data class VehicleSelectionUiState(
    val pickupLocation: Location? = null,
    val destinationLocation: Location? = null,
    val availableVehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: Vehicle? = null,
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedPaymentMethod: PaymentMethod? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedVehicleType: VehicleType? = null
)

@HiltViewModel
class VehicleSelectionViewModel @Inject constructor(
    private val getAvailableVehiclesUseCase: GetAvailableVehiclesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getRoutePointsUseCase: GetRoutePointsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleSelectionUiState())
    val uiState: StateFlow<VehicleSelectionUiState> = _uiState.asStateFlow()

    // State cho route points
    private val _routePoints = MutableStateFlow<List<Point>>(emptyList())
    val routePoints: StateFlow<List<Point>> = _routePoints.asStateFlow()

    // State cho loading route
    private val _isLoadingRoute = MutableStateFlow(false)
    val isLoadingRoute: StateFlow<Boolean> = _isLoadingRoute.asStateFlow()

    init {
        loadPaymentMethods()
    }

    fun setPickupAndDestination(pickup: Location, destination: Location) {
        _uiState.update { currentState ->
            currentState.copy(
                pickupLocation = pickup,
                destinationLocation = destination
            )
        }
        loadAvailableVehicles(pickup, destination)
        loadRoutePoints(pickup, destination)
    }

    private fun loadAvailableVehicles(pickup: Location, destination: Location) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val vehicles = getAvailableVehiclesUseCase(
                    sourceLocation = pickup,
                    destinationLocation = destination,
                    filterByType = _uiState.value.selectedVehicleType
                )

                // Đảm bảo luôn có một vehicle được chọn
                val selectedVehicle = _uiState.value.selectedVehicle?.let { currentSelected ->
                    // Nếu vehicle đang chọn vẫn còn trong danh sách mới thì giữ nguyên
                    vehicles.find { it.id == currentSelected.id }
                } ?: vehicles.firstOrNull() // Nếu không có vehicle nào được chọn hoặc vehicle đã chọn không còn trong danh sách, chọn vehicle đầu tiên

                _uiState.update {
                    it.copy(
                        availableVehicles = vehicles,
                        selectedVehicle = selectedVehicle, // Luôn set selectedVehicle
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Không thể tải danh sách phương tiện",
                        isLoading = false
                    )
                }
            }
        }
    }


    fun filterByVehicleType(vehicleType: VehicleType?) {
        _uiState.update {
            it.copy(
                selectedVehicleType = vehicleType
                // Không reset selectedVehicle ở đây nữa
            )
        }

        // Tải lại danh sách phương tiện với bộ lọc mới
        _uiState.value.pickupLocation?.let { pickup ->
            _uiState.value.destinationLocation?.let { destination ->
                loadAvailableVehicles(pickup, destination)
            }
        }
    }



    // Cập nhật hàm loadRoutePoints để sử dụng Flow
    private fun loadRoutePoints(pickup: Location, destination: Location) {
        _isLoadingRoute.value = true

        getRoutePointsUseCase(source = pickup, destination = destination)
            .onEach { points ->
                _routePoints.value = points
                _isLoadingRoute.value = false
            }
            .catch { error ->
                _isLoadingRoute.value = false
                _uiState.update {
                    it.copy(error = error.message ?: "Không thể tải tuyến đường")
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectVehicle(vehicle: Vehicle) {
        _uiState.update { it.copy(selectedVehicle = vehicle) }
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            try {
                val paymentMethods = getPaymentMethodsUseCase()
                _uiState.update {
                    it.copy(
                        paymentMethods = paymentMethods,
                        selectedPaymentMethod = paymentMethods.firstOrNull()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Không thể tải phương thức thanh toán")
                }
            }
        }
    }

    fun selectPaymentMethod(paymentMethodId: String) {
        val selectedMethod = _uiState.value.paymentMethods.find { it.id == paymentMethodId }
        selectedMethod?.let {
            _uiState.update { state -> state.copy(selectedPaymentMethod = it) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun getEstimatedPrice(): Double {
        return _uiState.value.selectedVehicle?.price ?: 0.0
    }

    fun getEstimatedPickupTime(): Int {
        return _uiState.value.selectedVehicle?.estimatedPickupTime ?: 0
    }

    fun getEstimatedDistance(): Double {
        val pickup = _uiState.value.pickupLocation
        val destination = _uiState.value.destinationLocation

        if (pickup == null || destination == null) return 0.0

        return calculateDistance(pickup, destination)
    }

    private fun calculateDistance(pickup: Location, destination: Location): Double {
        // Công thức Haversine để tính khoảng cách giữa hai điểm trên Trái Đất
        val R = 6371.0 // Bán kính Trái Đất tính bằng km

        val lat1 = Math.toRadians(pickup.latitude)
        val lon1 = Math.toRadians(pickup.longitude)
        val lat2 = Math.toRadians(destination.latitude)
        val lon2 = Math.toRadians(destination.longitude)

        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = Math.sin(dlat / 2).pow(2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c // Khoảng cách tính bằng km
    }
}
