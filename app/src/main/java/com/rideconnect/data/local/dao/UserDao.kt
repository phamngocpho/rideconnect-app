package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUserData()

    @Query("UPDATE users SET authToken = :token, refreshToken = :refreshToken, tokenExpiryDate = :expiryDate")
    suspend fun updateAuthTokens(token: String, refreshToken: String, expiryDate: ZonedDateTime)
}
