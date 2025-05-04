package com.rideconnect.data.remote.dto.response.message

import java.time.ZonedDateTime

data class ConversationResponse(
    val conversations: List<Conversation>
) {
    data class Conversation(
        val conversationId: String,
        val otherUserId: String,
        val otherUserName: String,
        val otherUserProfilePicture: String?,
        val lastMessage: String,
        val lastMessageTime: ZonedDateTime,
        val unreadCount: Int,
        val tripId: String?
    )
}
