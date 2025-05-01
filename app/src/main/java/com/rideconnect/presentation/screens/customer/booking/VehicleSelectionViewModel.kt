package com.rideconnect.presentation.screens.customer.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.payment.PaymentSelectionUiState
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.domain.model.vehicle.VehicleType
import com.rideconnect.domain.usecase.booking.GetAvailableVehiclesUseCase
import com.rideconnect.domain.usecase.payment.GetPaymentMethodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehicleSelectionState(
    val isLoading: Boolean = false,
    val availableVehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: Vehicle? = null,
    val selectedVehicleType: VehicleType? = null,
    val error: String? = null,
    val paymentState: PaymentSelectionUiState = PaymentSelectionUiState()
)

@HiltViewModel
class VehicleSelectionViewModel @Inject constructor(
    private val getAvailableVehiclesUseCase: GetAvailableVehiclesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VehicleSelectionState())
    val state: StateFlow<VehicleSelectionState> = _state.asStateFlow()

    private var currentSourceLocation: Location? = null
    private var currentDestinationLocation: Location? = null

    fun loadAvailableVehicles(sourceLocation: Location, destinationLocation: Location?) {
        currentSourceLocation = sourceLocation
        currentDestinationLocation = destinationLocation

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val vehicles = getAvailableVehiclesUseCase(
                    sourceLocation = sourceLocation,
                    destinationLocation = destinationLocation,
                    filterByType = _state.value.selectedVehicleType
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        availableVehicles = vehicles,
                        selectedVehicle = vehicles.firstOrNull() ?: it.selectedVehicle,
                        error = if (vehicles.isEmpty()) "Không tìm thấy phương tiện nào" else null
                    )
                }

                // Tải phương thức thanh toán
                loadPaymentMethods()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Đã xảy ra lỗi khi tải danh sách phương tiện"
                    )
                }
            }
        }
    }

    fun selectVehicleType(type: VehicleType?) {
        if (_state.value.selectedVehicleType == type) return

        _state.update { it.copy(selectedVehicleType = type) }

        // Tải lại danh sách phương tiện với bộ lọc mới
        currentSourceLocation?.let { source ->
            loadAvailableVehicles(source, currentDestinationLocation)
        }
    }

    fun selectVehicle(vehicle: Vehicle) {
        _state.update { it.copy(selectedVehicle = vehicle) }
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            try {
                val paymentMethods = getPaymentMethodsUseCase()

                // Lấy danh sách khuyến mãi từ các phương thức thanh toán
                val promotions = paymentMethods
                    .filter { it.type == com.rideconnect.domain.model.payment.PaymentMethodType.PROMOTION }
                    .mapNotNull { it.promotionInfo }

                _state.update { currentState ->
                    currentState.copy(
                        paymentState = PaymentSelectionUiState(
                            availablePaymentMethods = paymentMethods,
                            selectedPaymentMethod = paymentMethods.find { it.isDefault } ?: paymentMethods.firstOrNull() ?: PaymentMethod.cash(),
                            availablePromotions = promotions,
                            isLoading = false,
                            error = null
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        paymentState = currentState.paymentState.copy(
                            isLoading = false,
                            error = "Không thể tải phương thức thanh toán"
                        )
                    )
                }
            }
        }
    }

    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        _state.update { currentState ->
            currentState.copy(
                paymentState = currentState.paymentState.copy(
                    selectedPaymentMethod = paymentMethod
                )
            )
        }
    }

    fun retry() {
        currentSourceLocation?.let { source ->
            loadAvailableVehicles(source, currentDestinationLocation)
        }
    }
}
