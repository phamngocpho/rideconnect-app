package com.example.drive_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R
import com.rideconnect.presentation.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerServiceScreen() {
    Column {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Service", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                )
            },
            containerColor = Color(0xFFF8F8F8)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Box {
                            BannerImage()
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    "Premium",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    "Professional Transportation Service",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { /* Handle registration */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Register Now", color = Color.White)
                                }
                            }
                        }
                    }
                }
                item {
                    Text(
                        "Service Categories",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Black
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ServiceCategory(
                                icon = Icons.Default.DirectionsCar,
                                title = "Car",
                                backgroundColor = Color(0xFFE3F2FD)
                            )
                        }
                        item {
                            ServiceCategory(
                                icon = Icons.Default.AirportShuttle,
                                title = "Bus",
                                backgroundColor = Color(0xFFE8F5E9)
                            )
                        }
                        item {
                            ServiceCategory(
                                icon = Icons.Default.LocalShipping,
                                title = "Shipping",
                                backgroundColor = Color(0xFFFFF3E0)
                            )
                        }
                        item {
                            ServiceCategory(
                                icon = Icons.Default.Motorcycle,
                                title = "Motorbike",
                                backgroundColor = Color(0xFFE1F5FE)
                            )
                        }
                    }
                }
                item {
                    Text(
                        "Popular Services",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Black
                    )
                }
                val serviceList = listOf(
                    ServiceData(
                        "Airport Transfer",
                        "From $7.50",
                        "Book 2 hours in advance",
                        Icons.Default.Flight
                    ),
                    ServiceData(
                        "Daily Car Rental",
                        "From $40/day",
                        "Book 1 day in advance",
                        Icons.Default.DateRange
                    ),
                    ServiceData(
                        "Cargo Shipping",
                        "From $5",
                        "Same-day delivery",
                        Icons.Default.LocalShipping
                    ),
                    ServiceData(
                        "Ride Sharing",
                        "25% discount",
                        "Share the cost",
                        Icons.Default.PeopleAlt
                    )
                )

                items(serviceList) { service ->
                    ServiceItem(service)
                }

                item {
                    Text(
                        "Special Offers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Black
                    )
                }

                item {
                    PromotionCard()
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun BannerImage() {
    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ServiceCategory(
    icon: ImageVector,
    title: String,
    backgroundColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = backgroundColor.darken(0.3f),
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

data class ServiceData(
    val title: String,
    val price: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun ServiceItem(service: ServiceData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFBAFAC8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = null,
                    tint = LightGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.price,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = " • ${service.description}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PromotionCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFFFFF),
        border = BorderStroke(1.dp, Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "30% Off Your First Ride",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text("Valid for new users until May 30")
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

fun Color.darken(factor: Float): Color {
    return Color(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor),
        alpha = alpha
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ServicesScreenPreview() {
    MaterialTheme {
        CustomerServiceScreen()
    }
}

/**
 * Preview for banner
 */
@Preview(showBackground = true)
@Composable
fun BannerPreview() {
    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box {
                BannerImage()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        "GIPS Premium",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        "Professional Transportation Service",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* Handle registration */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Register Now", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Preview for service categories
 */
@Preview(showBackground = true)
@Composable
fun ServiceCategoryPreview() {
    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            ServiceCategory(
                icon = Icons.Default.DirectionsCar,
                title = "Private Car",
                backgroundColor = Color(0xFFE3F2FD)
            )
            ServiceCategory(
                icon = Icons.Default.AirportShuttle,
                title = "Bus",
                backgroundColor = Color(0xFFE8F5E9)
            )
        }
    }
}

/**
 * Preview for service item
 */
@Preview(showBackground = true)
@Composable
fun ServiceItemPreview() {
    MaterialTheme {
        ServiceItem(
            ServiceData(
                "Airport Transfer",
                "From $7.50",
                "Book 2 hours in advance",
                Icons.Default.Flight
            )
        )
    }
}

/**
 * Preview for promotion card
 */
@Preview(showBackground = true)
@Composable
fun PromotionCardPreview() {
    MaterialTheme {
        PromotionCard()
    }
}

/**
 * Preview for TopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text("Service", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
        )
    }
}
