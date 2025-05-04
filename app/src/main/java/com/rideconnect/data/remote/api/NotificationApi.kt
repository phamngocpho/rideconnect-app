package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.response.notification.NotificationsResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {

    @GET(ApiConstants.NOTIFICATIONS_ENDPOINT)
    suspend fun getNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<NotificationsResponse>

    @PUT(ApiConstants.NOTIFICATIONS_ENDPOINT + "/{notificationId}/read")
    suspend fun markAsRead(@Path("notificationId") notificationId: String): Response<Unit>

    @PUT(ApiConstants.NOTIFICATIONS_ENDPOINT + "/read-all")
    suspend fun markAllAsRead(): Response<Unit>

    @DELETE(ApiConstants.NOTIFICATIONS_ENDPOINT + "/{notificationId}")
    suspend fun deleteNotification(@Path("notificationId") notificationId: String): Response<Unit>
}
