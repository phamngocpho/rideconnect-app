package com.rideconnect.presentation.screens.customer.dashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R


@Composable
fun CustomerDashboardScreen() {
    Scaffold(
        contentColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))
        ) {
            // Header with location
            LocationHeader()

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clip(RoundedCornerShape(topStart = 24.dp))
                    .padding(16.dp)
            ) {
                // Banner
                TravelBanner()

                Spacer(modifier = Modifier.height(16.dp))

                // Search bar
                SearchBar()

                Spacer(modifier = Modifier.height(16.dp))

                // Saved locations
                SavedLocations()

                Spacer(modifier = Modifier.height(16.dp))

                // Ride options
                Text(
                    "Rides For your every need",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Ride types
                RideTypesRow()

                Spacer(modifier = Modifier.height(16.dp))

                // Save on rides
                Text(
                    "Save on rides",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Promotions
                PromotionCard()
            }
        }
    }
}

@Composable
fun LocationHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Your Location",
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.width(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Alexandria, Egypt",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Change location",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Icon(
            Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun TravelBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
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
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Where To?", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColorFor(backgroundColor = Color(0xFFF5F5F5)) )
        ) {
            Text("Now", color = Color.Black)
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun SavedLocations() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        backgroundColor = Color.White
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
        elevation = 2.dp
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
fun PromotionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {
        // Trong ứng dụng thực tế, bạn sẽ sử dụng hình ảnh từ resources
        // Ở đây tôi sử dụng Box để thay thế trong preview
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEEEEEE))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    Icons.Default.People,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    "Save 20% on group rides this weekend!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


// Preview cho toàn bộ ứng dụng
@Preview(showBackground = true)
@Composable
fun FullAppPreview() {
    MaterialTheme {
        CustomerDashboardScreen()
    }
}

// Preview cho phần header
@Preview(showBackground = true)
@Composable
fun LocationHeaderPreview() {
    MaterialTheme {
        Surface {
            LocationHeader()
        }
    }
}

// Preview cho banner
@Preview(showBackground = true)
@Composable
fun TravelBannerPreview() {
    MaterialTheme {
        Surface {
            TravelBanner()
        }
    }
}

// Preview cho thanh tìm kiếm
@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MaterialTheme {
        Surface {
            SearchBar()
        }
    }
}

// Preview cho địa điểm đã lưu
@Preview(showBackground = true)
@Composable
fun SavedLocationsPreview() {
    MaterialTheme {
        Surface {
            SavedLocations()
        }
    }
}

// Preview cho thẻ khuyến mãi
@Preview(showBackground = true)
@Composable
fun PromotionCardPreview() {
    MaterialTheme {
        Surface {
            PromotionCard()
        }
    }
}
@Preview(showBackground = true, widthDp = 400)
@Composable
fun RideTypesRowPreview() {
    RideTypesRow()
}

// Preview cho một card riêng lẻ
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




