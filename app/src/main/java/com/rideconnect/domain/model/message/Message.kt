package com.rideconnect.domain.model.message

import com.rideconnect.domain.model.user.User
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Message(
    val id: String,
    val tripId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: ZonedDateTime,
    val isRead: Boolean,
    val sender: User? = null,
    val receiver: User? = null
) {
    fun getFormattedTime(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getFormattedDate(): String {
        val now = ZonedDateTime.now()
        val messageDate = timestamp.toLocalDate()

        return when {
            messageDate.isEqual(now.toLocalDate()) -> "Today"
            messageDate.isEqual(now.minusDays(1).toLocalDate()) -> "Yesterday"
            ChronoUnit.DAYS.between(messageDate, now.toLocalDate()) < 7 ->
                timestamp.format(DateTimeFormatter.ofPattern("EEEE")) // Day name
            else -> timestamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
    }

    fun isRecent(): Boolean {
        val now = ZonedDateTime.now()
        return ChronoUnit.MINUTES.between(timestamp, now) < 5
    }

    fun isSentByMe(currentUserId: String): Boolean {
        return senderId == currentUserId
    }
}

// UI State cho Message List Screen
data class MessageListUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val tripId: String = "",
    val otherUser: User? = null,
    val newMessageText: String = ""
)

data class MessageUiModel(
    val message: Message,
    val isSentByMe: Boolean,
    val showDateHeader: Boolean = false
)
