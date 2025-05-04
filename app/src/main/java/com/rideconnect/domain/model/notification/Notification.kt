package com.rideconnect.domain.model.notification

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val type: String,
    val message: String,
    val createdAt: ZonedDateTime,
    val isRead: Boolean,
    val readAt: ZonedDateTime?,
    val relatedId: String?
) {
    fun getFormattedTime(): String {
        val now = ZonedDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(createdAt, now)
        val hours = ChronoUnit.HOURS.between(createdAt, now)
        val days = ChronoUnit.DAYS.between(createdAt, now)

        return when {
            minutes < 1 -> "Vừa xong"
            minutes < 60 -> "$minutes phút trước"
            hours < 24 -> "$hours giờ trước"
            days < 7 -> "$days ngày trước"
            else -> createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    }

    fun getNotificationType(): NotificationType {
        return when (type.uppercase()) {
            "TRIP_REQUEST" -> NotificationType.TRIP_REQUEST
            "TRIP_ACCEPTED" -> NotificationType.TRIP_ACCEPTED
            "DRIVER_ARRIVED" -> NotificationType.DRIVER_ARRIVED
            "TRIP_STARTED" -> NotificationType.TRIP_STARTED
            "TRIP_COMPLETED" -> NotificationType.TRIP_COMPLETED
            "TRIP_CANCELLED" -> NotificationType.TRIP_CANCELLED
            "PAYMENT_COMPLETED" -> NotificationType.PAYMENT_COMPLETED
            "PAYMENT_FAILED" -> NotificationType.PAYMENT_FAILED
            "RATING_RECEIVED" -> NotificationType.RATING_RECEIVED
            "PROMOTION" -> NotificationType.PROMOTION
            else -> NotificationType.SYSTEM
        }
    }

    fun getIcon(): ImageVector {
        return getNotificationType().icon
    }

    fun getIconTint(): Color {
        return getNotificationType().color
    }

    fun isActionable(): Boolean {
        return relatedId != null && getNotificationType() != NotificationType.SYSTEM
                && getNotificationType() != NotificationType.PROMOTION
    }

    fun getReadStatusText(): String {
        return if (isRead) "Đã đọc" else "Chưa đọc"
    }
}

enum class NotificationType(val icon: ImageVector, val color: Color) {
    TRIP_REQUEST(Icons.Default.DirectionsCar, Color(0xFF2196F3)),
    TRIP_ACCEPTED(Icons.Default.Check, Color(0xFF4CAF50)),
    DRIVER_ARRIVED(Icons.Default.Place, Color(0xFF9C27B0)),
    TRIP_STARTED(Icons.Default.PlayArrow, Color(0xFF03A9F4)),
    TRIP_COMPLETED(Icons.Default.Done, Color(0xFF4CAF50)),
    TRIP_CANCELLED(Icons.Default.Cancel, Color(0xFFF44336)),
    PAYMENT_COMPLETED(Icons.Default.Payment, Color(0xFF009688)),
    PAYMENT_FAILED(Icons.Default.Error, Color(0xFFF44336)),
    RATING_RECEIVED(Icons.Default.Star, Color(0xFFFFEB3B)),
    SYSTEM(Icons.Default.Info, Color(0xFF607D8B)),
    PROMOTION(Icons.Default.LocalOffer, Color(0xFFFF9800))
}

// UI State cho Notification List Screen
data class NotificationListUiState(
    val notifications: List<NotificationUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val unreadCount: Int = 0
)

data class NotificationUiModel(
    val notification: Notification,
    val isExpanded: Boolean = false
)
