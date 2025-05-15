package com.example.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileMenuItems(
    onLogoutClick: () -> Unit = {},
    onDocumentUpdateClick: () -> Unit = {},
    onTripHistoryClick: () -> Unit = {}
) {
    Column {
        MenuItemRow(icon = Icons.Default.Person, text = "Your profile")
        MenuItemRow(
            icon = Icons.Outlined.Description,
            text = "Cập nhật giấy tờ",
            onClick = onDocumentUpdateClick
        )
        MenuItemRow(
            icon = Icons.Default.History,
            text = "Lịch sử chuyến đi",
            onClick = onTripHistoryClick
        )
        MenuItemRow(icon = Icons.Default.List, text = "List of trips")
        MenuItemRow(icon = Icons.Outlined.AccountBalanceWallet, text = "Wallet balance")
        MenuItemRow(icon = Icons.Default.Settings, text = "Setting")
        MenuItemRow(icon = Icons.Default.Star, text = "Rating & Reviews")
        LogoutButton(
            onLogoutClick = onLogoutClick
        )
    }
}

@Composable
fun LogoutButton(
    onLogoutClick: () -> Unit = {}
) {
    val white = Color(0xFFFCFFFD)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        ),
        onClick = onLogoutClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MenuItemRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFCFFFD)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Green,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = text,
                color = Black,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Gray
            )
        }
    }
}
