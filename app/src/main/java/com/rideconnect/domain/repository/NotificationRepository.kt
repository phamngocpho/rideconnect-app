package com.rideconnect.domain.repository

import com.rideconnect.domain.model.notification.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getNotifications(userId: String): Result<List<Notification>>
    suspend fun markNotificationAsRead(notificationId: String): Result<Unit>
    suspend fun markAllNotificationsAsRead(userId: String): Result<Unit>
    suspend fun deleteNotification(notificationId: String): Result<Unit>
    suspend fun observeNotifications(userId: String): Flow<List<Notification>>
}
