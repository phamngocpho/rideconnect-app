package com.rideconnect.presentation.components.driver

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.presentation.screens.driver.dashboard.DriverDashboardViewModel

@Composable
fun StatusToggle(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val isOnline by viewModel.isOnline.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val pendingStatus by viewModel.pendingStatus.collectAsState()

    // Xác định trạng thái loading cho từng nút
    val isLoadingOffline = isLoading && pendingStatus == "offline"
    val isLoadingOnline = isLoading && pendingStatus == "online"

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
                .clickable(enabled = !isLoading) { viewModel.toggleOnlineStatus(context, false) },
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingOffline) {
                // Hiển thị loading indicator khi đang chuyển sang offline
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = if (!isOnline) Color.White else Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
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
        }

        // Nút Online
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(if (isOnline) Color(0xFF7BC67E) else Color.White)
                .clickable(enabled = !isLoading) { viewModel.toggleOnlineStatus(context, true) },
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingOnline) {
                // Hiển thị loading indicator khi đang chuyển sang online
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = if (isOnline) Color.White else Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
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
}
