package com.rideconnect.presentation.screens.customer.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.presentation.components.dashboard.*
import com.rideconnect.presentation.components.menu.ServiceItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rideconnect.R

@Composable
fun CustomerDashboardScreen(
    viewModel: CustomerDashboardViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            // Header với background mây
            item {
                DashboardHeader(
                    userName = "Phạm",
                    temperature = "27°C",
                    clubName = "VinClub"
                )
            }

            item {
                TravelBanner()
            }

            // Thanh tìm kiếm "Bạn muốn đi đâu?"
            item {
                SearchBar(
                    text = "Bạn muốn đi đâu?",
                    onClick = { onNavigate("search_location") },
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Lưới dịch vụ
            item {
                SavedLocations()
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
                RideTypesRow()
            }

            // Indicator cho menu
            item {
                PageIndicator(
                    pageCount = 3,
                    currentPage = 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            // Banner quảng cáo
            item {
                PromotionBanner(
                    title = "ĐÀ NẴNG TRONG XANH",
                    promotionValues = listOf("50K", "50K"),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }

            // Xanh Thổ Địa section
            item {
                ListItem(
                    headlineContent = { Text("Xanh Thổ Địa - Top 1 thịnh hành") },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

@Composable
fun RideTypesRow() {
    val rideTypes = listOf(
        RideTypeInfo(
            title = "Group Ride",
            backgroundColor = Color(0xFFAEE4B3),
            imageResId = R.drawable.group_ride
        ),
        RideTypeInfo(
            title = "Shuttle",
            backgroundColor = Color(0xFFB3D4FF),
            imageResId = R.drawable.shuttle
        ),
        RideTypeInfo(
            title = "Shipment",
            backgroundColor = Color(0xFFFFE0A3),
            imageResId = R.drawable.shipment
        ),
        RideTypeInfo(
            title = "Group Ride",
            backgroundColor = Color(0xFFAEE4B3),
            imageResId = R.drawable.group_ride
        ),
        RideTypeInfo(
            title = "Shuttle",
            backgroundColor = Color(0xFFB3D4FF),
            imageResId = R.drawable.shuttle
        ),
        RideTypeInfo(
            title = "Shipment",
            backgroundColor = Color(0xFFFFE0A3),
            imageResId = R.drawable.shipment
        )
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rideTypes) { rideType ->
            RideTypeCardWithImage(rideType)
        }
    }
}

data class RideTypeInfo(
    val title: String,
    val backgroundColor: Color,
    val imageResId: Int
)
@Composable
fun RideTypeCardWithImage(rideType: RideTypeInfo) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(rideType.backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = rideType.title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Image(
                painter = painterResource(id = rideType.imageResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun TravelBanner(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Banner background (would be an image in a real app)
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Image(
                    painter = painterResource(R.drawable.drive),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Text(
                    "Travel Smart",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Save More,",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Ride with us", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SavedLocations() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Home address
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_icon),
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        "Home Address",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "13 sidi Bishr, Montaza, Alexandria",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Divider()

            // Work location
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.work_icon),
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        "Work Location",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "22 Smouha, Alexandria",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CustomerDashboardScreenPreview() {
    CustomerDashboardScreen(
        onNavigate = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardHeaderPreview() {
    DashboardHeader(
        userName = "Phạm",
        temperature = "27°C",
        clubName = "VinClub"
    )
}

@Preview(showBackground = true)
@Composable
fun ServiceMenuGridPreview() {
    val services = remember {
        listOf(
            ServiceItem(
                id = "car",
                title = "Ô tô",
                icon = Icons.Default.DirectionsCar
            ),
            ServiceItem(
                id = "bike",
                title = "Xe máy",
                icon = Icons.Default.TwoWheeler
            ),
            ServiceItem(
                id = "delivery",
                title = "Giao hàng",
                icon = Icons.Default.LocalShipping
            ),
            ServiceItem(
                id = "airport",
                title = "Ô tô sân bay",
                icon = Icons.Default.FlightTakeoff
            ),
            ServiceItem(
                id = "vinfast",
                title = "Mua xe VinFast",
                icon = Icons.Default.CarRental
            ),
            ServiceItem(
                id = "tour",
                title = "Xanh Tour",
                icon = Icons.Default.Tour
            ),
            ServiceItem(
                id = "intercity",
                title = "Xanh Liên Tỉnh",
                icon = Icons.Default.Route
            ),
            ServiceItem(
                id = "taxi",
                title = "Taxi",
                icon = Icons.Default.LocalTaxi
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(
        text = "Bạn muốn đi đâu?",
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PromotionBannerPreview() {
    PromotionBanner(
        title = "ĐÀ NẴNG TRONG XANH",
        promotionValues = listOf("50K", "50K"),
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    PageIndicator(
        pageCount = 3,
        currentPage = 0,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TravelBannerPreview() {
    MaterialTheme {
        Surface {
            TravelBanner()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RideTypeCardPreview() {
    RideTypeCardWithImage(
        RideTypeInfo(
            title = "Group Ride",
            backgroundColor = Color(0xFFAEE4B3),
            imageResId = R.drawable.group_ride
        )
    )
}