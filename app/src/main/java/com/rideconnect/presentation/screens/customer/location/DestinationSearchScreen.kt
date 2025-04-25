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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.presentation.theme.RideConnectApplicationTheme

// Data class để lưu thông tin địa điểm
data class LocationSuggestion(
    val name: String,
    val address: String,
    val distance: String
)

data class SavedLocation(
    val title: String,
    val address: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationSearchScreen(
    onBackClick: () -> Unit = {},
    onSelectLocationClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    onTabClick: (Int) -> Unit = {},
    onPopularLocationsClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Recent", "Suggested", "Saved")
    var searchText by remember { mutableStateOf("") }

    // Sử dụng LaunchedEffect để xử lý chuyển tab
    LaunchedEffect(selectedTab) {
        if (selectedTab == 2) {
            // Gọi callback khi tab Saved được chọn
            onTabClick(2)
        }
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

            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    onSearchTextChanged(newText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = { Text("Enter your destinations") },
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
                            .clickable { /* Mở dropdown */ }
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Alex",
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Chọn địa điểm",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Tab layout: Recent, Suggested, Saved
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
                            .clickable {
                                selectedTab = index
                                onTabClick(index)
                            }
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

            Spacer(modifier = Modifier.height(40.dp))

            // Sử dụng LazyColumn để phần còn lại có thể cuộn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Icon tìm kiếm trong vòng tròn
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE6F9E6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Văn bản hướng dẫn
                    Text(
                        text = "Where do you want to go?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Enter your destination in the search area above to find your locations",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Nút chọn vị trí
                    Button(
                        onClick = { onSelectLocationClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = "Select Location on map",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút bỏ qua
                    OutlinedButton(
                        onClick = { onSkipClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4CAF50)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = SolidColor(Color(0xFF4CAF50))
                        )
                    ) {
                        Text(
                            text = "Skip destination step",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Thêm khoảng trống để đảm bảo có thể cuộn xuống dưới
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// Màn hình hiển thị kết quả tìm kiếm
@Composable
fun SearchResultsScreen(
    searchText: String,
    onBackClick: () -> Unit = {},
    onLocationClick: (LocationSuggestion) -> Unit = {}
) {
    // Danh sách gợi ý địa điểm
    val suggestions = listOf(
        LocationSuggestion(
            name = "Alexandria University",
            address = "22 El-Gaish Rd, Alexandria Governorate",
            distance = "0.6 km"
        ),
        LocationSuggestion(
            name = "Alexandria Bibliotheca",
            address = "Bab Sharqi, Alexandria Governorate 21526",
            distance = "0.6 km"
        ),
        LocationSuggestion(
            name = "Al Ittihad Alexandria Club",
            address = "Labs Ahmed Hassanein, Bab Sharqi",
            distance = "1 km"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 32.dp)
    ) {
        // Nút quay lại (mũi tên xuống)
        Box(
            modifier = Modifier
                .padding(16.dp)
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

        // Thanh tìm kiếm với dropdown "Alex"
        OutlinedTextField(
            value = searchText,
            onValueChange = { /* Không cần xử lý */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = 56.dp),
            placeholder = { Text("All") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { /* Mở dropdown */ }
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "Alex",
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Chọn địa điểm",
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

        Spacer(modifier = Modifier.height(16.dp))

        // Tiêu đề "Search Suggestion"
        Text(
            text = "Search Suggestion",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách gợi ý địa điểm
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(suggestions) { suggestion ->
                LocationSuggestionItem(
                    suggestion = suggestion,
                    onClick = { onLocationClick(suggestion) }
                )
            }
        }
    }
}

// Màn hình hiển thị địa điểm phổ biến
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularLocationsScreen(
    onBackClick: () -> Unit = {},
    onLocationClick: (LocationSuggestion) -> Unit = {}
) {
    // Danh sách địa điểm phổ biến
    val popularLocations = listOf(
        LocationSuggestion(
            name = "Alexandria Bibliotheca",
            address = "Bab Sharqi, Alexandria Governorate 21526",
            distance = "0.6 km"
        ),
        LocationSuggestion(
            name = "Borg El Arab Stadium",
            address = "Ekeingy Maryout, Alexandria Governorate",
            distance = "1 km"
        ),
        LocationSuggestion(
            name = "Hannoville",
            address = "Al Agamy Al Bahri, Dekhela, Alexandria",
            distance = "2 km"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(popularLocations) { location ->
                LocationSuggestionItem(
                    suggestion = location,
                    onClick = { onLocationClick(location) }
                )
            }
        }
    }
}

// Item hiển thị gợi ý địa điểm
@Composable
fun LocationSuggestionItem(
    suggestion: LocationSuggestion,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon vị trí
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin địa điểm
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = suggestion.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = suggestion.distance,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "  ${suggestion.address}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Icon menu 3 chấm
        IconButton(onClick = { /* Menu options */ }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = Color.Gray
            )
        }
    }

    // Thêm đường kẻ ngang ở dưới mỗi item
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
        color = Color(0xFFEEEEEE),
        thickness = 1.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedLocationsScreen(
    onBackClick: () -> Unit,
    onLocationClick: (String) -> Unit,
    onAddNewClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Thanh tiêu đề với nút back
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Thanh tìm kiếm
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Enter your destinations",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Row {
                Text(
                    text = "Alex",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Profile",
                    tint = Color.Gray
                )
            }
        }

        // Tab lựa chọn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Recent", "Suggested", "Saved").forEachIndexed { index, title ->
                val isSelected = index == 2 // Saved tab is selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color(0xFF4CAF50) else Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }

        // Danh sách địa điểm đã lưu
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val savedLocations = listOf(
                SavedLocation(
                    title = "Home Address",
                    address = "13 sidi Bishr, Montaza, Alexandria",
                    icon = Icons.Outlined.Home
                ),
                SavedLocation(
                    title = "Work Location",
                    address = "22 Smouha, Alexandria",
                    icon = Icons.Outlined.Work
                )
            )

            savedLocations.forEach { location ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationClick(location.title) }
                        .padding(vertical = 8.dp),
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
                            imageVector = location.icon,
                            contentDescription = location.title,
                            tint = Color.Black
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = location.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = location.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    IconButton(onClick = { /* Menu options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.Gray
                        )
                    }
                }

                HorizontalDivider(
                    color = Color(0xFFEEEEEE),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Nút thêm địa điểm mới
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onAddNewClick,
                modifier = Modifier.padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    text = "Add New +",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DestinationSearchScreenPreview() {
    RideConnectApplicationTheme {
        DestinationSearchScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultsScreenPreview() {
    RideConnectApplicationTheme {
        SearchResultsScreen(searchText = "Alexandria")
    }
}

@Preview(showBackground = true)
@Composable
fun PopularLocationsScreenPreview() {
    RideConnectApplicationTheme {
        PopularLocationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SavedLocationsScreenPreview() {
    RideConnectApplicationTheme {
        SavedLocationsScreen(
            onBackClick = {},
            onLocationClick = {},
            onAddNewClick = {}
        )
    }
}
