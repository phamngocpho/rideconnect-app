package com.rideconnect.presentation.screens.customer.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rideconnect.presentation.navigation.NavigationItem

@Composable
fun HistoryTripScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color(0xFFF0F0F0),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable {
                        navController.navigate(NavigationItem.Services.route)
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Trips",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ride History",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SectionHeader("Yesterday")
                RideItem(
                    tripId = "trip2",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                RideItem(
                    tripId = "trip3",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }

            item {
                SectionHeader("Last Week")
                Text(
                    text = "July 17th, 2024",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                RideItem(
                    tripId = "trip4",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }
            item {
                SectionHeader("Yesterday")
                RideItem(
                    tripId = "trip2",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                RideItem(
                    tripId = "trip3",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }

            item {
                SectionHeader("Last Week")
                Text(
                    text = "July 17th, 2024",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                RideItem(
                    tripId = "trip4",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }
            item {
                SectionHeader("Yesterday")
                RideItem(
                    tripId = "trip2",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                RideItem(
                    tripId = "trip3",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }

            item {
                SectionHeader("Last Week")
                Text(
                    text = "July 17th, 2024",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                RideItem(
                    tripId = "trip4",
                    pickupLocation = "Emtedad Dr Abdel Hamid Street",
                    pickupTime = "Pickup point at 9:41 pm",
                    destination = "Alexandria Bibliotheca",
                    destinationTime = "Reach destination at 10:05 pm",
                    price = "40.25 EGP",
                    distance = "5.3 km",
                    onTripClick = { tripId ->
                        navController.navigate("trip_details/$tripId")
                    },
                    isActive = false
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun RideItem(
    tripId: String,
    pickupLocation: String,
    pickupTime: String,
    destination: String,
    destinationTime: String,
    price: String,
    distance: String,
    onTripClick: (String) -> Unit,
    isActive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTripClick(tripId) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pickupLocation,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = pickupTime,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = destination,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = destinationTime,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = distance,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            if (isActive) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Active",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8, heightDp = 800)
@Composable
fun ActivityScreenPreview() {
    HistoryTripScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader("Yesterday")
}

@Preview(showBackground = true)
@Composable
fun RideItemPreview() {
    RideItem(
        tripId = "trip2",
        pickupLocation = "Emtedad Dr Abdel Hamid Street",
        pickupTime = "Pickup point at 9:41 pm",
        destination = "Alexandria Bibliotheca",
        destinationTime = "Reach destination at 10:05 pm",
        price = "40.25 EGP",
        distance = "5.3 km",
        onTripClick = {},
        isActive = false
    )
}

@Preview(showBackground = true)
@Composable
fun RideItemActivePreview() {
    RideItem(
        tripId = "trip1",
        pickupLocation = "Emtedad Dr Abdel Hamid Street",
        pickupTime = "Pickup point at 9:41 pm",
        destination = "Alexandria Bibliotheca",
        destinationTime = "Reach destination at 10:05 pm",
        price = "40.25 EGP",
        distance = "5.3 km",
        onTripClick = {},
        isActive = true
    )
}
