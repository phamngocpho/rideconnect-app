package com.rideconnect.presentation.screens.customer.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.R
import com.rideconnect.presentation.ui.theme.LightGray
import com.rideconnect.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    viewModel: CustomerProfileViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit
) {
    val white = Color(0xFFFCFFFD)
    val green = Color(0xFF37B44E)
    val black = Color(0xFF050505)
    val darkGray = Color(0xFF98A39A)
    val lightGray = Color(0xFFBBBFBC)

    val logoutState by viewModel.logoutState.collectAsState()
    LaunchedEffect(logoutState) {
        when(logoutState){
            is Resource.Success -> {
                onLogoutSuccess()
            }
            else -> { }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileTopBar()

        Divider(color = lightGray.copy(alpha = 0.5f))

        ProfileHeader()

        Spacer(modifier = Modifier.height(20.dp))

        // Menu Items
        ProfileMenuSection(
            onLogoutClick = { (viewModel.logout()) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    val white = Color(0xFFFCFFFD)
    val black = Color(0xFF050505)

    TopAppBar(
        title = {
            Text(
                text = "Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                color = black
            )
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = white
        )
    )
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .size(90.dp)
            .clip(CircleShape)
            .background(
                color = LightGray
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(R.drawable.person_icon),
            contentDescription = ""
        )
    }

    Text(
        text = "UserName",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun ProfileMenuSection(
    onLogoutClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileMenuItem(
            icon = Icons.Default.Person,
            title = "Your profile",
            onClick = {}
        )

        ProfileMenuItem(
            icon = Icons.Default.LocationOn,
            title = "Manage Address",
            onClick = {}
        )

        ProfileMenuItem(
            icon = Icons.Default.Notifications,
            title = "Notification",
            onClick = {}
        )

        ProfileMenuItem(
            icon = Icons.Default.CreditCard,
            title = "Payment Methods",
            onClick = {}
        )

        ProfileMenuItem(
            icon = Icons.Default.DateRange,
            title = "Pre-Booked Rides",
            onClick = {}
        )

        ProfileMenuItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = { }
        )

        ProfileMenuItem(
            icon = Icons.Default.Call,
            title = "Emergency Contact",
            onClick = {}
        )
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
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val black = Color(0xFF050505)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFE8E8E8),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                tint = black,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        CustomerProfileScreen(
            onLogoutSuccess = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTopBarPreview() {
    MaterialTheme {
        ProfileTopBar()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    MaterialTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            ProfileHeader()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileMenuItemPreview() {
    MaterialTheme {
        ProfileMenuItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileMenuSectionPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            ProfileMenuSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoutButtonPreview() {
    MaterialTheme {
        LogoutButton()
    }
}