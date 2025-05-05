package com.rideconnect.presentation.screens.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R
import com.rideconnect.presentation.screens.driver.dashboard.MovelitViewModel
import com.rideconnect.presentation.theme.LightGreen


@Composable
fun LoginScreen(
    viewModel: MovelitViewModel,
    onLoginClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
){
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(R.drawable.car),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.White.copy(alpha = 0.7f),
                        Color.White
                    ),
                    startY = 1200f,
                    endY =  1600f
                )
            )) {
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).padding(bottom = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                horizontalArrangement =Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Ride", style = MaterialTheme
                        .typography.headlineLarge
                        .copy(fontWeight = FontWeight.Bold, fontSize = 50.sp),
                    color = Color.Black,

                    )
                Text(
                    text = "Connect", style = MaterialTheme
                        .typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold, fontSize = 50.sp),
                    color = LightGreen
                )
            }
            Text(
                text = "Login to reach new experience",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray.copy(alpha = 0.99f),
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 50.dp)
            )
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen
                )
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onCreateAccountClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),  // Đặt shape ở parameter của OutlinedButton
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "Create an account",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                )
            }


            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}


