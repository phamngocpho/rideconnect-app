package com.example.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R

@Composable
fun TopBar(
    onBackClick: () -> Unit = {}  // Thêm tham số này
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {  // Sử dụng tham số đã thêm
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Black
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Profile",
            color = Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        // Empty space for symmetry
        Spacer(modifier = Modifier.width(48.dp))
    }
}

@Composable
fun ProfileInfo(
    onEditProfileClick: () -> Unit = {}  // Thêm tham số này
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.drive),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onEditProfileClick,  // Sử dụng tham số đã thêm
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .background(Gray, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Black,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ahmed Aboelhassan",
            color = Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "01552882189",
            color = Gray,
            fontSize = 18.sp
        )
    }
}
