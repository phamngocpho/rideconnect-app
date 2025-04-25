package com.rideconnect.presentation.screens.shared.map

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.presentation.screens.customer.location.DestinationSearchScreen
import com.rideconnect.presentation.screens.customer.location.LocationSuggestion
import com.rideconnect.presentation.screens.customer.location.PopularLocationsScreen
import com.rideconnect.presentation.screens.customer.location.SearchResultsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLocationSelected: (String) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showPopularLocations by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Giả lập nền bản đồ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F2E9))
        )

        // Icon vị trí hiện tại (marker)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-30).dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Current Location",
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF4CAF50)
            )
        }

        // Thẻ địa điểm
        Card(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 120.dp)
                .align(Alignment.BottomStart),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Faculty Of Arts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Alexandria University",
                    fontSize = 14.sp
                )
                Text(
                    text = "كلية الأداب جامعة الإسكندرية",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Nút zoom in/out
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Zoom in */ },
                modifier = Modifier.size(40.dp),
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(
                onClick = { /* Zoom out */ },
                modifier = Modifier.size(40.dp),
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Text(
                    text = "−",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Nút vị trí hiện tại
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Định vị hiện tại */ },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Vị trí hiện tại",
                    tint = Color.Black
                )
            }
        }

        // Bottom sheet cho thanh tìm kiếm
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            // Thanh tìm kiếm có thể kéo lên
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Thanh ngang để kéo
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color.LightGray, RoundedCornerShape(2.dp))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Where to?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Thanh tìm kiếm có thể nhấn
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showBottomSheet = true
                            },
                        placeholder = { Text("Enter your destinations") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm kiếm",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            TextButton(onClick = { /* Skip action */ }) {
                                Text("Skip", color = Color.Gray)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color.LightGray,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        ),
                        enabled = false
                    )
                }
            }
        }
    }

    // Bottom sheet cho màn hình tìm kiếm
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            if (showPopularLocations) {
                // Hiển thị giao diện địa điểm phổ biến từ file DestinationSearchScreen.kt
                PopularLocationsScreen(
                    onBackClick = {
                        showPopularLocations = false
                    },
                    onLocationClick = { location ->
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            // Xử lý khi chọn địa điểm
                        }
                    }
                )
            } else if (isSearchActive) {
                // Hiển thị giao diện kết quả tìm kiếm từ file DestinationSearchScreen.kt
                SearchResultsScreen(
                    searchText = searchText,
                    onBackClick = {
                        isSearchActive = false
                        searchText = ""
                    },
                    onLocationClick = { location ->
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            // Xử lý khi chọn địa điểm
                        }
                    }
                )
            } else {
                // Hiển thị giao diện tìm kiếm ban đầu từ file DestinationSearchScreen.kt
                DestinationSearchScreen(
                    onBackClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    },
                    onSelectLocationClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            // Thực hiện hành động chọn vị trí trên bản đồ
                        }
                    },
                    onSkipClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            // Thực hiện hành động bỏ qua
                        }
                    },
                    onSearchTextChanged = { text ->
                        searchText = text
                        isSearchActive = text.isNotEmpty()
                    },
                    onTabClick = { tabIndex ->
                        if (tabIndex == 1) { // "Suggested" tab
                            showPopularLocations = true
                        }
                    }
                )
            }
        }
    }
    FloatingActionButton(
        onClick = {
            // Giả sử đã chọn một vị trí
            onLocationSelected("Vị trí đã chọn")
        },
        modifier = Modifier
            .padding(top = 120.dp)
//            .align(Alignment.BottomCenter)
            .padding(bottom = 200.dp),
        containerColor = Color(0xFF4CAF50),
        contentColor = Color.White
    ) {
        Text(
            text = "Chọn vị trí này",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

