package com.rideconnect.presentation.screens.customer.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.location.SavedLocation
import com.rideconnect.presentation.components.location.LocationSearchCard
import com.rideconnect.presentation.components.location.LocationResultsList
import com.rideconnect.presentation.components.location.SavedLocationsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLocationScreen(
    viewModel: SearchLocationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onLocationSelected: (source: Location?, destination: Location?) -> Unit
) {
    val searchState by viewModel.searchState.collectAsState()
    val sourceSearchState by viewModel.sourceSearchState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    var destinationQuery by remember { mutableStateOf("") }
    var sourceQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Cập nhật sourceQuery khi currentLocation thay đổi
    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            sourceQuery = currentLocation?.address ?: "Vị trí hiện tại"
        }
    }

    // Danh sách địa điểm đã lưu (ví dụ)
    val savedLocations = remember {
        listOf(
            SavedLocation(
                id = "1",
                name = "Nhà",
                address = "123 Đường ABC, Quận XYZ, TP.HCM",
                latitude = 10.762622,
                longitude = 106.660172,
                type = SavedLocation.Type.HOME
            ),
            SavedLocation(
                id = "2",
                name = "Công ty",
                address = "456 Đường DEF, Quận UVW, TP.HCM",
                latitude = 10.772622,
                longitude = 106.680172,
                type = SavedLocation.Type.WORK
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        TopAppBar(
            title = { Text("Chọn điểm đến") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Location input fields
        LocationSearchCard(
            sourceQuery = sourceQuery,
            onSourceQueryChange = {
                sourceQuery = it
                if (it.isNotEmpty()) {
                    viewModel.searchSourceLocation(it)
                }
            },
            destinationQuery = destinationQuery,
            onDestinationQueryChange = {
                destinationQuery = it
                viewModel.searchPlaces(it)
            },
            isSourceLoading = currentLocation == null,
            isCurrentLocation = sourceQuery == (currentLocation?.address ?: "Vị trí hiện tại"),
            onClearSourceClick = {
                sourceQuery = ""
                viewModel.refreshCurrentLocation()
                focusManager.clearFocus()
            },
            onClearDestinationClick = {
                destinationQuery = ""
                focusManager.clearFocus()
            },
            onMyLocationClick = {
                viewModel.refreshCurrentLocation()
                focusManager.clearFocus()
            }
        )

        // Hiển thị trạng thái loading cho tìm kiếm nguồn
        if (sourceSearchState is SearchLocationViewModel.SearchState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // Hiển thị kết quả tìm kiếm cho vị trí nguồn
        if (sourceSearchState is SearchLocationViewModel.SearchState.Success) {
            val predictions = (sourceSearchState as SearchLocationViewModel.SearchState.Success).predictions
            if (predictions.isNotEmpty()) {
                LocationResultsList(
                    title = "Kết quả tìm kiếm điểm đón",
                    predictions = predictions,
                    onItemClick = { prediction ->
                        viewModel.selectSourcePlace(prediction.place_id)
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Hiển thị trạng thái loading cho tìm kiếm đích đến
        if (searchState is SearchLocationViewModel.SearchState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // Search results for destination
        when (searchState) {
            is SearchLocationViewModel.SearchState.Success -> {
                val predictions = (searchState as SearchLocationViewModel.SearchState.Success).predictions
                if (sourceSearchState !is SearchLocationViewModel.SearchState.Success && predictions.isNotEmpty()) {
                    LocationResultsList(
                        title = "Kết quả tìm kiếm điểm đến",
                        predictions = predictions,
                        onItemClick = { prediction ->
                            viewModel.selectPlace(prediction.place_id) { location ->
                                onLocationSelected(currentLocation, location)
                                focusManager.clearFocus()
                            }
                        }
                    )
                }
            }
            is SearchLocationViewModel.SearchState.Error -> {
                val error = (searchState as SearchLocationViewModel.SearchState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                // Nếu không có kết quả tìm kiếm nào, hiển thị các địa điểm đã lưu
                if (destinationQuery.isEmpty() &&
                    sourceSearchState !is SearchLocationViewModel.SearchState.Success &&
                    sourceSearchState !is SearchLocationViewModel.SearchState.Loading) {

                    SavedLocationsCard(
                        savedLocations = savedLocations,
                        onLocationSelected = { _ ->
                            // Xử lý khi chọn địa điểm đã lưu
                        }
                    )
                }
            }
        }
    }
}
