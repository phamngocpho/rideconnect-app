package com.rideconnect.presentation.screens.customer.location

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.location.SavedLocation

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
    var selectedTab by remember { mutableStateOf(2) } // Mặc định chọn tab "Saved"
    val tabs = listOf("Recent", "Suggested", "Saved")

    // Cập nhật sourceQuery khi currentLocation thay đổi
    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            sourceQuery = currentLocation?.address ?: "Vị trí hiện tại"
        }
    }

    // Danh sách địa điểm đã lưu
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Status Bar Space
            Spacer(modifier = Modifier.height(24.dp))

            // Back Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Quay lại",
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Source Search Field
            OutlinedTextField(
                value = sourceQuery,
                onValueChange = {
                    sourceQuery = it
                    if (it.isNotEmpty()) {
                        viewModel.searchSourceLocation(it)
                    } else {
                        viewModel.refreshCurrentLocation()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = { Text("Enter your pickup location") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { viewModel.refreshCurrentLocation() }
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "My Location",
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Chọn vị trí hiện tại",
                            tint = Color.Gray
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray,
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Destination Search Field
            OutlinedTextField(
                value = destinationQuery,
                onValueChange = {
                    destinationQuery = it
                    viewModel.searchPlaces(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = { Text("Enter your destination") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (destinationQuery.isNotEmpty()) {
                        IconButton(onClick = { destinationQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Xóa",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray,
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tab layout: Recent, Suggested, Saved
            if (destinationQuery.isEmpty() && sourceSearchState !is SearchLocationViewModel.SearchState.Success) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Color(0xFFE6F9E6) else Color(0xFFF5F5F5)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color(0xFFE6F9E6) else Color.LightGray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) Color(0xFF4CAF50) else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Nội dung cuộn
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Kết quả tìm kiếm điểm đón
                if (sourceSearchState is SearchLocationViewModel.SearchState.Success) {
                    val predictions = (sourceSearchState as SearchLocationViewModel.SearchState.Success).predictions
                    if (predictions.isNotEmpty()) {
                        item {
                            Text(
                                text = "Kết quả tìm kiếm điểm đón",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        items(predictions) { prediction ->
                            LocationResultItem(
                                name = prediction.description,
                                address = prediction.description, // API có thể cần điều chỉnh để lấy address chi tiết
                                onClick = {
                                    viewModel.selectSourcePlace(prediction.place_id)
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }

                // Kết quả tìm kiếm điểm đến
                when (searchState) {
                    is SearchLocationViewModel.SearchState.Success -> {
                        val predictions = (searchState as SearchLocationViewModel.SearchState.Success).predictions
                        if (sourceSearchState !is SearchLocationViewModel.SearchState.Success && predictions.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Kết quả tìm kiếm điểm đến",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            items(predictions) { prediction ->
                                LocationResultItem(
                                    name = prediction.description,
                                    address = prediction.description,
                                    onClick = {
                                        viewModel.selectPlace(prediction.place_id) { location ->
                                            onLocationSelected(currentLocation, location)
                                            focusManager.clearFocus()
                                        }
                                    }
                                )
                            }
                        }
                    }
                    is SearchLocationViewModel.SearchState.Error -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (searchState as SearchLocationViewModel.SearchState.Error).message,
                                    color = Color(0xFFE57373),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    else -> {
                        // Hiển thị địa điểm đã lưu khi không có kết quả tìm kiếm
                        if (destinationQuery.isEmpty() &&
                            sourceSearchState !is SearchLocationViewModel.SearchState.Success &&
                            sourceSearchState !is SearchLocationViewModel.SearchState.Loading &&
                            selectedTab == 2
                        ) {
                            items(savedLocations) { location ->
                                SavedLocationItem(
                                    title = location.name,
                                    address = location.address,
                                    icon = when (location.type) {
                                        SavedLocation.Type.HOME -> Icons.Outlined.Home
                                        SavedLocation.Type.WORK -> Icons.Outlined.Work
                                        else -> Icons.Outlined.LocationOn
                                    },
                                    onClick = {
                                        // Giả lập chọn địa điểm đã lưu
                                        val selectedLocation = Location(
                                            latitude = location.latitude,
                                            longitude = location.longitude,
                                            address = location.address,
                                            name = location.name
                                        )
                                        onLocationSelected(currentLocation, selectedLocation)
                                    }
                                )
                            }
                        }
                    }
                }

                // Loading indicators
                if (sourceSearchState is SearchLocationViewModel.SearchState.Loading) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
                if (searchState is SearchLocationViewModel.SearchState.Loading) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }
    }
}

// Item hiển thị kết quả tìm kiếm
@Composable
fun LocationResultItem(
    name: String,
    address: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = { /* Menu options */ }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = Color.Gray
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
        color = Color(0xFFEEEEEE),
        thickness = 1.dp
    )
}

// Item hiển thị địa điểm đã lưu
@Composable
fun SavedLocationItem(
    title: String,
    address: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        IconButton(onClick = { /* Menu options */ }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = Color.Gray
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color(0xFFEEEEEE),
        thickness = 1.dp
    )
}