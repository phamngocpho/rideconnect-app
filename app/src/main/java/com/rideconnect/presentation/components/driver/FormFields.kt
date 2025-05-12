package com.example.profile.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Text(
                text = value,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FormFieldWithAction(
    label: String,
    value: String,
    actionText: String,
    onActionClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Text(
                    text = actionText,
                    color = Green,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(onClick = onActionClick)
                )
            }
        }
    }
}

@Composable
fun FormFieldWithDropdown(
    label: String,
    value: String,
    onDropdownClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .clickable(onClick = onDropdownClick)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun FormFieldWithNavigation(
    label: String,
    value: String,
    onNavigateClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .clickable(onClick = onNavigateClick)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Black
                )
            }
        }
    }
}
