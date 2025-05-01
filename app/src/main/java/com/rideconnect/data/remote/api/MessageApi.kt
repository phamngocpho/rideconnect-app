package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.message.SendMessageRequest
import com.rideconnect.data.remote.dto.response.message.ConversationResponse
import com.rideconnect.data.remote.dto.response.message.MessageResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface MessageApi {

    @GET(ApiConstants.CONVERSATIONS_ENDPOINT)
    suspend fun getConversations(): Response<ConversationResponse>

    @GET(ApiConstants.MESSAGES_ENDPOINT)
    suspend fun getMessages(
        @Query("conversationId") conversationId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<List<MessageResponse>>

    @POST(ApiConstants.MESSAGES_ENDPOINT)
    suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest): Response<MessageResponse>

    @PUT(ApiConstants.MESSAGES_ENDPOINT + "/{messageId}/read")
    suspend fun markAsRead(@Path("messageId") messageId: String): Response<Unit>
}
