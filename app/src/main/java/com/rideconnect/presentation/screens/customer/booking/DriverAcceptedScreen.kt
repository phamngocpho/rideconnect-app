package com.rideconnect.presentation.screens.customer.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.domain.model.location.Location
import com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse
import com.rideconnect.domain.model.trip.Trip

@Composable
fun DriverAcceptedScreen(
    tripDetails: Trip,
    pickupLocation: Location,
    dropOffLocation: Location,
    onCallDriver: () -> Unit,
    onChatDriver: () -> Unit,
    onCancelTrip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Driver is on the way....",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Driver profile section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    // Placeholder for driver image
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Driver info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tripDetails.driverName ?: "Unknown Driver",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Biển số: ${tripDetails.vehiclePlate ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "(1600)Trip 4.9",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Call and Chat buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCallDriver,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call")
                }

                OutlinedButton(
                    onClick = onChatDriver,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Trip details section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Trip details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pickup and destination
            Column(modifier = Modifier.fillMaxWidth()) {
                // Pickup location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = pickupLocation.address ?: "Unknown pickup location",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Dotted line
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color.Gray)
                )

                // Destination location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = dropOffLocation.address ?: "Unknown destination",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Car type and price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Car icon placeholder
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.LightGray, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("\uD83D\uDE97", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            // Sửa: Sử dụng trường đúng cho loại xe
                            text = tripDetails.vehicleType ?: "Saver Car",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Price
                        Text(
                            text = "Vnđ ${tripDetails.estimatedFare?.let { "%.2f".format(it) } ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Thông tin về khoảng cách và thời gian
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Khoảng cách: ${tripDetails.estimatedDistance?.let { "%.1f".format(it) } ?: "N/A"} km",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = "Thời gian: ${tripDetails.estimatedDuration ?: "N/A"} phút",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cancel button
                Button(
                    onClick = onCancelTrip,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Cancel Request")
                }
            }
        }
    }
}
