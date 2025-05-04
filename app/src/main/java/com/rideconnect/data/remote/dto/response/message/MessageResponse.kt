package com.rideconnect.data.remote.dto.response.message

import java.time.ZonedDateTime

data class MessageResponse(
    val messageId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: ZonedDateTime,
    val isRead: Boolean
)
