package com.rideconnect.presentation.components.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.model.trip.TripStatus
import kotlinx.coroutines.delay
import java.math.BigDecimal

@Composable
fun TripRequestDialog(
    trip: Trip,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Dialog can't be dismissed by clicking outside */ },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Yêu cầu chuyến đi mới!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Customer info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = trip.customerName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pickup location
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Điểm đón",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = trip.pickupAddress,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Drop-off location
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Điểm đến",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = trip.dropOffAddress,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Trip details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Khoảng cách",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${trip.estimatedDistance} km",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Giá tiền",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${trip.estimatedFare} VND",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Countdown timer
                CountdownTimer(
                    totalTimeInSeconds = 30,
                    onTimeFinished = onReject
                )

                // Buttons - Vertical layout
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nhận chuyến")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onReject,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Từ chối")
                }
            }
        },
        // Xóa bỏ các nút ở vị trí cũ
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun CountdownTimer(
    totalTimeInSeconds: Int,
    onTimeFinished: () -> Unit
) {
    var remainingTime by remember { mutableIntStateOf(totalTimeInSeconds) }

    LaunchedEffect(key1 = totalTimeInSeconds) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
        onTimeFinished()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$remainingTime",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = when {
                    remainingTime <= 5 -> Color.Red
                    remainingTime <= 10 -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            Text(
                text = " giây để trả lời",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = remainingTime.toFloat() / totalTimeInSeconds,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = when {
                remainingTime <= 5 -> Color.Red
                remainingTime <= 10 -> Color(0xFFFF9800)
                else -> MaterialTheme.colorScheme.primary
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TripRequestDialogPreview() {
//    MaterialTheme {
//        Surface {
//            TripRequestDialog(
//                trip = Trip(
//                    id = "trip123",
//                    customerId = "cust456",
//                    customerName = "Nguyễn Văn A",
//                    customerPhone = "0901234567",
//                    driverId = null,
//                    driverName = null,
//                    driverPhone = null,
//                    vehicleType = null,
//                    vehiclePlate = null,
//                    pickupLatitude = 10.762622,
//                    pickupLongitude = 106.660172,
//                    pickupAddress = "227 Nguyễn Văn Cừ, Quận 5, TP.HCM",
//                    dropOffLatitude = 10.773201,
//                    dropOffLongitude = 106.685609,
//                    dropOffAddress = "Chợ Bến Thành, Quận 1, TP.HCM",
//                    status = TripStatus.PENDING,
//                    estimatedDistance = 5.2,
//                    estimatedDuration = 15,
//                    estimatedFare = BigDecimal("75000"),
//                    actualFare = null,
//                    requestedAt = "2025-05-13T11:30:00Z",
//                    startedAt = null,
//                    completedAt = null,
//                    cancelledAt = null,
//                    cancellationReason = null,
//                    driverLocation = null
//                ),
//                onAccept = {},
//                onReject = {}
//            )
//        }
//    }
//}