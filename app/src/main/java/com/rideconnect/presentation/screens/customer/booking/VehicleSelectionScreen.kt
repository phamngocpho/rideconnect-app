package com.rideconnect.presentation.screens.customer.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.vehicle.VehicleType
import com.rideconnect.presentation.components.booking.BookTripButton
import com.rideconnect.presentation.components.booking.LocationRouteHeader
import com.rideconnect.presentation.components.booking.PaymentMethodSelector
import com.rideconnect.presentation.components.booking.RidePreferenceCard
import com.rideconnect.presentation.components.booking.VehicleList
import com.rideconnect.presentation.components.booking.VehicleTypeFilter
import com.rideconnect.presentation.components.common.LoadingStateContent
import com.rideconnect.presentation.components.navigation.VehicleSelectionTopBar

@Composable
fun VehicleSelectionScreen(
    viewModel: VehicleSelectionViewModel = hiltViewModel(),
    sourceLocation: Location,
    destinationLocation: Location?,
    onBackClick: () -> Unit,
    onBookRide: (vehicleType: VehicleType) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(sourceLocation, destinationLocation) {
        viewModel.loadAvailableVehicles(sourceLocation, destinationLocation)
    }

    Scaffold(
        topBar = {
            VehicleSelectionTopBar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Hiển thị thông tin tuyến đường
            LocationRouteHeader(
                sourceLocation = sourceLocation,
                destinationLocation = destinationLocation,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Bộ lọc loại phương tiện
            VehicleTypeFilter(
                selectedType = state.selectedVehicleType,
                onTypeSelected = { viewModel.selectVehicleType(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Phần nội dung chính với xử lý loading/error
            LoadingStateContent(
                isLoading = state.isLoading,
                error = state.error,
                onRetry = { viewModel.retry() },
                modifier = Modifier.weight(1f),
                content = {
                    VehicleList(
                        vehicles = state.availableVehicles,
                        onVehicleSelected = { viewModel.selectVehicle(it) },
                        selectedVehicleId = state.selectedVehicle?.id,
                        isLoading = false,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            )

            // Phần thanh toán
            val paymentState = state.paymentState
            if (paymentState.availablePaymentMethods.isNotEmpty() && paymentState.selectedPaymentMethod != null) {
                PaymentMethodSelector(
                    availablePaymentMethods = paymentState.availablePaymentMethods,
                    selectedMethod = paymentState.selectedPaymentMethod,
                    onPaymentMethodSelected = { method ->
                        viewModel.selectPaymentMethod(method)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // Gợi ý tùy chọn chuyến đi
            RidePreferenceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Nút đặt xe
            BookTripButton(
                selectedVehicle = state.selectedVehicle,
                onBookRide = {
                    state.selectedVehicle?.let {
                        onBookRide(it.type)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}