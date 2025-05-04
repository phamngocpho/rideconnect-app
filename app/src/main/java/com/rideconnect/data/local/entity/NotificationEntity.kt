package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "TRIP", "PROMO", "SYSTEM", etc.
    val isRead: Boolean,
    val timestamp: ZonedDateTime,
    val relatedEntityId: String? // Trip ID, promo ID, etc.
)
