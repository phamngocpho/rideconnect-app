package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rideconnect.domain.model.location.Location
import java.time.ZonedDateTime

@Entity(tableName = "trip_history")
data class TripHistoryEntity(
    @PrimaryKey
    val id: String,
    val pickupLocation: Location,
    val dropOffLocation: Location,
    val pickupAddress: String,
    val dropOffAddress: String,
    val status: String,
    val fare: Double,
    val distance: Double,
    val duration: Int,
    val driverName: String?,
    val driverAvatar: String?,
    val vehiclePlate: String?,
    val date: ZonedDateTime,
    val rating: Float?
)
