package com.example.testapp.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.domain.model.driver.ActivityStatus

@Composable
fun ActivityTabs(
    selectedTab: ActivityStatus,
    onTabSelected: (ActivityStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActivityStatus.values().forEach { status ->
            val isSelected = status == selectedTab

            OutlinedButton(
                onClick = { onTabSelected(status) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Color(0xFF4CAF50) else Color.White,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFF4CAF50) else Color.LightGray
                )
            ) {
                Text(text = status.name.capitalize())
            }
        }
    }
}

// Hàm hỗ trợ viết hoa chữ cái đầu
private fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}


@Composable
fun ActivityTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Green else Color.Transparent)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            fontSize = 18.sp
        )
    }
}
