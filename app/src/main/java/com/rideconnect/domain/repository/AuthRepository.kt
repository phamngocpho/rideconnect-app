package com.rideconnect.domain.repository

import com.rideconnect.domain.model.user.User
import com.rideconnect.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Resource<User>
    suspend fun register(fullName: String, phoneNumber: String, email: String?, password: String): Resource<User>
    suspend fun logout(): Resource<Unit>
    suspend fun getAuthToken(): Flow<String>
    fun getCurrentUser(): Flow<User?>
    suspend fun refreshToken(): Resource<String>
    fun getUserId(): Flow<String>
    suspend fun isTokenValid(): Boolean
}
