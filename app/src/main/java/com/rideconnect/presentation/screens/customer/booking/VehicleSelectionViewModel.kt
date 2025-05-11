package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.rideconnect.domain.model.direction.RouteInfo
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.domain.model.vehicle.VehicleType
import com.rideconnect.domain.usecase.booking.GetAvailableVehiclesUseCase
import com.rideconnect.domain.usecase.direction.GetRouteInfoUseCase
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
    val selectedVehicleType: VehicleType? = null,
    val routeInfo: RouteInfo? = null
)

@HiltViewModel
class VehicleSelectionViewModel @Inject constructor(
    private val getAvailableVehiclesUseCase: GetAvailableVehiclesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getRoutePointsUseCase: GetRoutePointsUseCase,
    private val getRouteInfoUseCase: GetRouteInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleSelectionUiState())
    val uiState: StateFlow<VehicleSelectionUiState> = _uiState.asStateFlow()

    private val _routePoints = MutableStateFlow<List<Point>>(emptyList())
    val routePoints: StateFlow<List<Point>> = _routePoints.asStateFlow()

    private val _isLoadingRoute = MutableStateFlow(false)
    val isLoadingRoute: StateFlow<Boolean> = _isLoadingRoute.asStateFlow()

    init {
        Log.d("VehicleViewModel", "init: ViewModel created")
        loadPaymentMethods()
    }

    fun setPickupAndDestination(pickup: Location, destination: Location) {
        Log.d("VehicleViewModel", "setPickupAndDestination: pickup=$pickup, destination=$destination")
        _uiState.update {
            it.copy(
                pickupLocation = pickup,
                destinationLocation = destination
            )
        }
        loadAvailableVehicles(pickup, destination)
        loadRoutePoints(pickup, destination)
        loadRouteInfo(pickup, destination) //
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

                val selectedVehicle = if (vehicles.isNotEmpty()) {
                    _uiState.value.selectedVehicle?.let { current ->
                        vehicles.find { it.id == current.id }
                    } ?: vehicles.first()
                } else null

                _uiState.update {
                    it.copy(
                        availableVehicles = vehicles,
                        selectedVehicle = selectedVehicle,
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

    private fun loadRouteInfo(pickup: Location, destination: Location) {
        viewModelScope.launch {
            getRouteInfoUseCase(pickup, destination)
                .catch { e ->
                    Log.e("VehicleViewModel", "Error loading route info: ${e.message}", e)
                    _uiState.update {
                        it.copy(error = e.message ?: "Không thể tải thông tin quãng đường")
                    }
                }
                .collect { info ->
                    Log.d("VehicleViewModel", "RouteInfo: distance=${info?.distance}, duration=${info?.duration}")
                    _uiState.update { it.copy(routeInfo = info) }
                }
        }
    }

    fun filterByVehicleType(vehicleType: VehicleType?) {
        _uiState.update {
            it.copy(selectedVehicleType = vehicleType)
        }
        _uiState.value.pickupLocation?.let { pickup ->
            _uiState.value.destinationLocation?.let { destination ->
                loadAvailableVehicles(pickup, destination)
            }
        }
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
}