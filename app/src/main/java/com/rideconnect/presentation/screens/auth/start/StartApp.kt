package com.rideconnect.presentation.screens.auth.start


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.R

@Composable
fun StartApp(
    modifier: Modifier = Modifier,
    viewModel: StartAppViewModel = hiltViewModel(),
    onLoginClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {},
    onNavigateToCustomerHome: () -> Unit = {},
    onNavigateToDriverHome: () -> Unit = {}
){
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    // Kiểm tra trạng thái đăng nhập và điều hướng nếu đã đăng nhập
    LaunchedEffect(isLoggedIn, userRole) {
        Log.d("StartApp", "LaunchedEffect: isLoggedIn = $isLoggedIn, userRole = $userRole")
        if (isLoggedIn) {
            when (userRole) {
                "DRIVER" -> {
                    Log.d("StartApp", "Navigating to driver home")
                    onNavigateToDriverHome()
                }
                else -> {
                    Log.d("StartApp", "Navigating to customer home")
                    onNavigateToCustomerHome()
                }
            }
        }
    }

    // Hiển thị UI chỉ khi chưa đăng nhập hoặc đang kiểm tra
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(R.drawable.img_2),
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
        ){
            Text(
                text = "Đặt xe nhanh chóng",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Đặt xe an toàn, tiện lợi và nhanh chóng với RideConnect",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Đăng nhập",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Tạo tài khoản",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}



// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        StartApp()
    }
}
