package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rideconnect.R
import com.rideconnect.presentation.components.navigation.DriverBottomNavigation
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DriverDashboardScreen(
    onLogout: () -> Unit,
    navController: NavController,
    viewModel: DriverDashboardViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsState()

    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Toggle
                StatusToggle(viewModel = viewModel)

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị thông báo trạng thái hiện tại
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isOnline)
                            "Đang tìm kiếm khách hàng, vui lòng đợi."
                        else
                            "Bạn đang Offline, không thể nhận cuốc xe.",
                        fontSize = 14.sp,
                        color = if (isOnline) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị phần tìm kiếm chỉ khi đang online
                if (isOnline) {
                    // Location Indicator
                    LocationIndicator()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Looking for Rider Text
                    Text(
                        text = "Đang tìm kiếm hành khách",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Illustration
                    RideIllustration()
                } else {
                    // Hiển thị thông báo khi offline
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ride_illustration),
                                contentDescription = "Offline Illustration",
                                modifier = Modifier.size(200.dp),
                                alpha = 0.5f
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Chuyển sang trạng thái Online để bắt đầu nhận cuốc",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatusToggle(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val isOnline by viewModel.isOnline.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(40.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Offline
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(if (!isOnline) Color(0xFFE57373) else Color.White)
                .clickable { viewModel.toggleOnlineStatus(context, false) },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Offline",
                    tint = if (!isOnline) Color.White else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Offline",
                    color = if (!isOnline) Color.White else Color.Gray,
                    fontWeight = if (!isOnline) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }

        // Nút Online
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(if (isOnline) Color(0xFF7BC67E) else Color.White)
                .clickable { viewModel.toggleOnlineStatus(context, true) },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Online",
                    tint = if (isOnline) Color.White else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Online",
                    color = if (isOnline) Color.White else Color.Gray,
                    fontWeight = if (isOnline) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LocationIndicator() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
            }
        }
    }
}

@Composable
fun RideIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ride_illustration),
            contentDescription = "Ride Illustration",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
fun StatusTogglePreview() {
    // Tạo một DriverDashboardViewModel giả lập cho preview
    val fakeViewModel = object {
        val isOnline = MutableStateFlow(true)
        fun toggleOnlineStatus(context: Context, online: Boolean) {}
    }

    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(40.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút Offline
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Offline",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Offline",
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            // Nút Online
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFF7BC67E)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Online",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Online",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationIndicatorPreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        LocationIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun RideIllustrationPreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Ride Illustration Preview")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriverAppPreview() {
    // Không thể preview với hiltViewModel(), nên chúng ta tạo một giao diện giả
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatusTogglePreview()

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Đang tìm kiếm khách hàng, vui lòng đợi.",
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LocationIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đang tìm kiếm hành khách",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}
