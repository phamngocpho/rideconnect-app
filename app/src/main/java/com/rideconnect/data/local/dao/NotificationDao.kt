package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY timestamp DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadNotificationCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE timestamp < :olderThan")
    suspend fun deleteOldNotifications(olderThan: ZonedDateTime)

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()
}
