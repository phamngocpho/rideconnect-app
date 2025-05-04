package com.rideconnect.data.remote.dto.request.message

data class SendMessageRequest(
    val receiverId: String,
    val content: String,
    val tripId: String? = null
)
