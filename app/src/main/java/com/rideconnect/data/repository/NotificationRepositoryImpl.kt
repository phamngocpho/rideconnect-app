package com.rideconnect.data.repository

import com.rideconnect.data.mapper.toDomainModel
import com.rideconnect.data.remote.api.NotificationApi
import com.rideconnect.domain.model.notification.Notification
import com.rideconnect.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
) : NotificationRepository {

    private val notificationsFlow = MutableSharedFlow<List<Notification>>(replay = 1)

    override suspend fun getNotifications(userId: String): Result<List<Notification>> {
        return try {
            val response = notificationApi.getNotifications()

            if (!response.isSuccessful) {
                return Result.failure(HttpException(response))
            }

            val responseBody = response.body() ?: return Result.failure(
                Exception("Response body is null")
            )

            val (notifications, _) = responseBody.toDomainModel(userId)
            notificationsFlow.emit(notifications)
            Result.success(notifications)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return try {
            val response = notificationApi.markAsRead(notificationId)
            if (!response.isSuccessful) {
                return Result.failure(HttpException(response))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllNotificationsAsRead(userId: String): Result<Unit> {
        return try {
            val response = notificationApi.markAllAsRead()
            if (!response.isSuccessful) {
                return Result.failure(HttpException(response))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> {
        return try {
            val response = notificationApi.deleteNotification(notificationId)
            if (!response.isSuccessful) {
                return Result.failure(HttpException(response))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun observeNotifications(userId: String): Flow<List<Notification>> {
        getNotifications(userId)
        return notificationsFlow
    }
}
