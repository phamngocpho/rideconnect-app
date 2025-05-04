package com.rideconnect.data.mapper

import com.rideconnect.data.remote.dto.response.notification.NotificationsResponse
import com.rideconnect.domain.model.notification.Notification
import java.time.ZonedDateTime

fun NotificationsResponse.NotificationDto.toDomainModel(
    userId: String,
    readAt: ZonedDateTime? = null
): Notification {
    return Notification(
        id = notificationId.toString(),
        userId = userId,
        title = title,
        message = message,
        type = type,
        createdAt = createdAt,
        isRead = isRead,
        readAt = readAt,
        relatedId = relatedId?.toString()
    )
}

fun NotificationsResponse.toDomainModel(
    userId: String,
    calculateReadAt: Boolean = false
): Pair<List<Notification>, Int> {
    val domainNotifications = notifications.map { dto ->
        val readAt = if (calculateReadAt && dto.isRead) {
            dto.createdAt
        } else {
            null
        }

        dto.toDomainModel(userId, readAt)
    }
    return Pair(domainNotifications, unreadCount)
}