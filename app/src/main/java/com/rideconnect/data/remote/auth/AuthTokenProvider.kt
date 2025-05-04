package com.rideconnect.data.remote.auth

import androidx.datastore.preferences.core.stringPreferencesKey
import com.rideconnect.util.preferences.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lớp quản lý token xác thực, giúp tránh vòng lặp phụ thuộc giữa AuthRepository và AuthInterceptor
 */
@Singleton
class AuthTokenProvider @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private const val EMPTY_TOKEN = ""
    }

    // Lưu token vào DataStore
    suspend fun setToken(token: String?) {
        preferenceManager.setPreference(AUTH_TOKEN_KEY, token ?: EMPTY_TOKEN)
    }

    // Lấy token dưới dạng Flow
    // Sử dụng runBlocking để gọi hàm suspend từ hàm không suspend
    fun getTokenFlow(): Flow<String?> = runBlocking {
        preferenceManager.getPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN)
            .map { it.ifEmpty { null } }
    }

    // Lấy token ngay lập tức (blocking) - chỉ sử dụng trong AuthInterceptor
    fun getToken(): String? = runBlocking {
        val token = preferenceManager.getPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN).firstOrNull()
        if (token.isNullOrEmpty()) null else token
    }

    // Xóa token
    suspend fun clearToken() {
        preferenceManager.setPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN)
    }
}
