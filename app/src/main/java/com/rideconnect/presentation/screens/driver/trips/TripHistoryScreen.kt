package com.rideconnect.presentation.screens.driver.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.model.trip.TripStatus
import com.rideconnect.presentation.components.driver.EmptyState
import com.rideconnect.presentation.components.driver.ErrorState
import com.rideconnect.presentation.components.driver.LoadingIndicator
import com.rideconnect.presentation.components.navigation.DriverBottomNavigation
import com.rideconnect.util.formatDateTime
import com.rideconnect.util.formatPrice

// Định nghĩa màu chủ đạo và các màu phái sinh
val LightGreen = Color(0xFF37B44E)
val DarkGreen = Color(0xFF2A8A3B)
val LightGreenBackground = Color(0xFFEBF7ED)
val LightGreenSurface = Color(0xFFD6EFD9)
val LightGreenAccent = Color(0xFFA7E0B1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripHistoryScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    viewModel: TripHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val trips by viewModel.trips.collectAsState()
    val listState = rememberLazyListState()

    // Kiểm tra khi người dùng cuộn đến cuối danh sách để tải thêm
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 3
            }
        }
    }

    LaunchedEffect(loadMore.value) {
        if (loadMore.value) {
            viewModel.loadMoreTrips()
        }
    }

    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lịch sử chuyến đi",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Thêm một Spacer để cân đối với nút quay lại
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )

        },
        containerColor = LightGreenBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightGreenBackground)
        ) {
            when {
                trips.isEmpty() && uiState is TripHistoryUiState.Loading -> {
                    LoadingIndicator()
                }
                trips.isEmpty() && uiState is TripHistoryUiState.Success -> {
                    EmptyState(
                        message = "Bạn chưa có chuyến đi nào",
                        onRetry = { viewModel.loadInitialTrips() }
                    )
                }
                uiState is TripHistoryUiState.Error && trips.isEmpty() -> {
                    val errorMessage = (uiState as TripHistoryUiState.Error).message
                    ErrorState(
                        message = errorMessage,
                        onRetry = { viewModel.retryLoading() }
                    )
                }
                else -> {
                    TripHistoryList(
                        trips = trips,
                        listState = listState
                    )

                    // Hiển thị loading ở cuối danh sách khi đang tải thêm
                    if (uiState is TripHistoryUiState.Loading && trips.isNotEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            color = LightGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TripHistoryList(
    trips: List<Trip>,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(trips) { trip ->
            TripHistoryItem(trip = trip)
        }
    }
}

@Composable
fun TripHistoryItem(trip: Trip) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Thời gian và trạng thái
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = LightGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatDateTime(trip.requestedAt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }

                TripStatusChip(status = trip.status)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = LightGreenBackground
            )

            // Địa điểm đón
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(LightGreenSurface)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = LightGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Điểm đón",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = trip.pickupAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Địa điểm trả khách
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(Color(0xFFFFEBEE))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Điểm đến",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = trip.dropOffAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = LightGreenBackground
            )

            // Giá tiền
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loại xe: ${trip.vehicleType ?: "Chưa xác định"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = null,
                        tint = LightGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${formatPrice(trip.actualFare ?: trip.estimatedFare)} VNĐ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen
                    )
                }
            }
        }
    }
}

@Composable
fun TripStatusChip(status: TripStatus) {
    val (backgroundColor, textColor, statusText) = when (status) {
        TripStatus.COMPLETED -> Triple(LightGreenSurface, DarkGreen, "Hoàn thành")
        TripStatus.CANCELLED -> Triple(Color(0xFFFFEBEE), Color(0xFFD32F2F), "Đã hủy")
        TripStatus.IN_PROGRESS -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "Đang đi")
        TripStatus.PENDING -> Triple(Color(0xFFFFFDE7), Color(0xFF616161), "Chờ xác nhận")
        TripStatus.ACCEPTED -> Triple(Color(0xFFE0F7FA), Color(0xFF00838F), "Đã nhận")
        TripStatus.ARRIVED -> Triple(Color(0xFFF3E5F5), Color(0xFF7B1FA2), "Đã đến")
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = statusText,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
