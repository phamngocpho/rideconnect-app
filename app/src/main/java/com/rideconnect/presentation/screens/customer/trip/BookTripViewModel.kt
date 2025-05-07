package com.rideconnect.presentation.screens.customer.trip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.RideConnectApplicationTheme

@Composable
fun BookTripScreen(
    onNavigateToMapScreen: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top= 20.dp)
                .padding(16.dp)
        ) {
            // Status Bar and Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Vị trí của bạn",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Hà Nội, Việt Nam",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Badge(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Thông báo",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Background image would go here
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF333333))
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "RideConnect ",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Vị trí",
                                tint = Color.Red
                            )
                        }

                        Text(
                            text = "Tiết kiệm hơn.",
                            color = Color(0xFF4ADE80),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onNavigateToMapScreen() },
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4ADE80)
                            )
                        ) {
                            Text(
                                text = "Đặt xe ngay",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    placeholder = { Text("Bạn muốn đi đâu?") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm"
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .height(50.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Lịch trình",
                            tint = Color.Black
                        )
                        Text(
                            text = "Ngay",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Home Address
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Nhà"
                    )

                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Địa chỉ nhà",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "123 Trần Duy Hưng, Cầu Giấy, Hà Nội",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Đi"
                    )
                }
            }

            Divider()

            // Work Location
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Công việc"
                    )

                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Địa chỉ công ty",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "36 Hoàng Cầu, Đống Đa, Hà Nội",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Đi"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ride options
            Text(
                text = "Dịch vụ đa dạng cho mọi nhu cầu",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ride types
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Group Ride
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD1F5D3)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Xe chung",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Đi chung",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Shuttle
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD1F5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AirportShuttle,
                            contentDescription = "Xe đưa đón",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Xe đưa đón",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Shipment
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE6F5D1)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = "Vận chuyển",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Giao hàng",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save on rides
            Text(
                text = "Tiết kiệm với ưu đãi",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Promo images
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE6F5D1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Giảm 20%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Chuyến đầu tiên",
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFD1F5F5))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Giảm 50K",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Đặt xe buổi sáng",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation
        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(60.dp),
            containerColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Trang chủ",
                        tint = Color(0xFF4ADE80)
                    )
                    Text(
                        text = "Trang chủ",
                        fontSize = 12.sp,
                        color = Color(0xFF4ADE80)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = "Dịch vụ",
                        tint = Color.Gray
                    )
                    Text(
                        text = "Dịch vụ",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Hoạt động",
                        tint = Color.Gray
                    )
                    Text(
                        text = "Hoạt động",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Cá nhân",
                        tint = Color.Gray
                    )
                    Text(
                        text = "Cá nhân",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookTripScreenPreview() {
    RideConnectApplicationTheme {
        BookTripScreen()
    }
}