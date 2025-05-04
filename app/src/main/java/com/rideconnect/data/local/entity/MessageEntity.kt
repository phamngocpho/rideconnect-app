package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: ZonedDateTime,
    val isRead: Boolean,
    val isSent: Boolean,
    val isDelivered: Boolean,
    val tripId: String?
)
