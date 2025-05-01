package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE tripId = :tripId ORDER BY timestamp ASC")
    fun getMessagesByTrip(tripId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("UPDATE messages SET isRead = 1 WHERE conversationId = :conversationId AND receiverId = :currentUserId")
    suspend fun markConversationAsRead(conversationId: String, currentUserId: String)

    @Query("SELECT COUNT(*) FROM messages WHERE isRead = 0 AND receiverId = :userId")
    fun getUnreadMessageCount(userId: String): Flow<Int>

    @Query("DELETE FROM messages WHERE timestamp < :olderThan")
    suspend fun deleteOldMessages(olderThan: ZonedDateTime)

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()
}
