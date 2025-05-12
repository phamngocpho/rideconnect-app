package com.rideconnect.presentation.screens.driver.trips
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.component.ActivityHeader
import com.example.testapp.component.ActivityItem
import com.example.testapp.component.ActivityTabs
import com.rideconnect.domain.model.driver.Activity
import com.rideconnect.domain.model.driver.ActivityStatus
import com.rideconnect.presentation.components.navigation.DriverBottomNavigation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun ActiveTripScreeen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(ActivityStatus.ACTIVE) }

    // Mẫu dữ liệu
    val activities = remember {
        listOf(
            Activity(
                name = "Nade",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Ahmed",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Mohamed",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Shrouk",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Mena",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Eslam",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 20)),
                status = ActivityStatus.ACTIVE
            ),
            Activity(
                name = "Omar",
                carModel = "Mustang Shelby GT",
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 20)),
                status = ActivityStatus.ACTIVE
            )
        )
    }

    // Chuyển đổi trạng thái dựa theo tab được chọn
    val displayActivities = activities.map { activity ->
        when (selectedTab) {
            ActivityStatus.ACTIVE -> activity.copy(status = ActivityStatus.ACTIVE)
            ActivityStatus.COMPLETED -> activity.copy(status = ActivityStatus.COMPLETED)
            ActivityStatus.CANCELLED -> activity.copy(status = ActivityStatus.CANCELLED)
        }
    }

    Scaffold(
        bottomBar = { DriverBottomNavigation(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            color = Color.White
            // Màu nền nhẹ nhàng hơn
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header với nền trắng
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column {
                        ActivityHeader(onBackClick = {})
                        Spacer(modifier = Modifier.height(8.dp))
                        ActivityTabs(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Danh sách hoạt động
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayActivities) { activity ->
                            ActivityItem(activity = activity)

                            // Thêm đường phân cách giữa các mục trừ mục cuối cùng
                            if (activity != displayActivities.last()) {
                                Divider(
                                    modifier = Modifier.padding(top = 8.dp),
                                    thickness = 0.5.dp,
                                    color = Color.LightGray
                                )
                            }
                        }
                        // Thêm padding ở cuối danh sách
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}


