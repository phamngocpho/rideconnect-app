package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
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
        Log.d("VehicleViewModel", "init: ViewModel created")
        loadPaymentMethods()
    }

    fun setPickupAndDestination(pickup: Location, destination: Location) {
        Log.d("VehicleViewModel", "setPickupAndDestination: pickup=$pickup, destination=$destination")
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
            Log.d("VehicleViewModel", "loadAvailableVehicles coroutine started")
            _uiState.update { it.copy(isLoading = true) }
            try {
                val vehicles = getAvailableVehiclesUseCase(
                    sourceLocation = pickup,
                    destinationLocation = destination,
                    filterByType = _uiState.value.selectedVehicleType
                )

                Log.d("VehicleViewModel", "getAvailableVehiclesUseCase returned ${vehicles.size} vehicles")
                vehicles.forEach { Log.d("VehicleViewModel", "  Vehicle: ${it.id}, ${it.type}") }

                val selectedVehicle = if (vehicles.isNotEmpty()) {
                    _uiState.value.selectedVehicle?.let { currentSelected ->
                        vehicles.find { it.id == currentSelected.id }
                    } ?: vehicles.first()
                } else {
                    Log.w("VehicleViewModel", "  No vehicles available after use case")
                    null
                }

                _uiState.update {
                    Log.d("VehicleViewModel", "Updating uiState with ${vehicles.size} availableVehicles, selectedVehicle=${selectedVehicle?.id}")
                    it.copy(
                        availableVehicles = vehicles,
                        selectedVehicle = selectedVehicle,
                        isLoading = false
                    )
                }
                Log.d("VehicleViewModel", "uiState.availableVehicles size after update: ${_uiState.value.availableVehicles.size}")

            } catch (e: Exception) {
                Log.e("VehicleViewModel", "Error loading vehicles: ${e.message}", e)
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
        Log.d("VehicleViewModel", "filterByVehicleType: $vehicleType")
        _uiState.update {
            it.copy(
                selectedVehicleType = vehicleType
            )
        }

        _uiState.value.pickupLocation?.let { pickup ->
            _uiState.value.destinationLocation?.let { destination ->
                loadAvailableVehicles(pickup, destination)
            }
        }
    }

    private fun loadRoutePoints(pickup: Location, destination: Location) {
        Log.d("VehicleViewModel", "loadRoutePoints: pickup=$pickup, destination=$destination")
        _isLoadingRoute.value = true

        getRoutePointsUseCase(source = pickup, destination = destination)
            .onEach { points ->
                Log.d("VehicleViewModel", "getRoutePointsUseCase emitted ${points.size} points")
                _routePoints.value = points
                _isLoadingRoute.value = false
            }
            .catch { error ->
                Log.e("VehicleViewModel", "Error loading route points: ${error.message}", error)
                _isLoadingRoute.value = false
                _uiState.update {
                    it.copy(error = error.message ?: "Không thể tải tuyến đường")
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectVehicle(vehicle: Vehicle) {
        Log.d("VehicleViewModel", "selectVehicle: ${vehicle.id}")
        _uiState.update { it.copy(selectedVehicle = vehicle) }
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            Log.d("VehicleViewModel", "loadPaymentMethods coroutine started")
            try {
                val paymentMethods = getPaymentMethodsUseCase()
                Log.d("VehicleViewModel", "getPaymentMethodsUseCase returned ${paymentMethods.size} methods")
                paymentMethods.forEach { Log.d("VehicleViewModel", "  Method: ${it.id}") }

                _uiState.update {
                    it.copy(
                        paymentMethods = paymentMethods,
                        selectedPaymentMethod = paymentMethods.firstOrNull()
                    )
                }
                Log.d("VehicleViewModel", "uiState.paymentMethods size after update: ${_uiState.value.paymentMethods.size}")

            } catch (e: Exception) {
                Log.e("VehicleViewModel", "Error loading payment methods: ${e.message}", e)
                _uiState.update {
                    it.copy(error = e.message ?: "Không thể tải phương thức thanh toán")
                }
            }
        }
    }

    fun selectPaymentMethod(paymentMethodId: String) {
        Log.d("VehicleViewModel", "selectPaymentMethod: $paymentMethodId")
        val selectedMethod = _uiState.value.paymentMethods.find { it.id == paymentMethodId }
        selectedMethod?.let {
            _uiState.update { state -> state.copy(selectedPaymentMethod = it) }
        }
    }

    fun clearError() {
        Log.d("VehicleViewModel", "clearError")
        _uiState.update { it.copy(error = null) }
    }
}