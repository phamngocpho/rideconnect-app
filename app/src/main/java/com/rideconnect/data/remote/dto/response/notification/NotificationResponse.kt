package com.rideconnect.data.remote.dto.response.notification

import java.time.ZonedDateTime
import java.util.UUID

data class NotificationsResponse(
    val notifications: List<NotificationDto>,
    val unreadCount: Int
) {
    data class NotificationDto(
        val notificationId: UUID,
        val type: String,
        val title: String,
        val message: String,
        val relatedId: UUID?,
        val isRead: Boolean,
        val createdAt: ZonedDateTime
    )
}
