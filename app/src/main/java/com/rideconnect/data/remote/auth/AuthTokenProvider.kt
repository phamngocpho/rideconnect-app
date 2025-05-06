package com.rideconnect.data.remote.auth

import androidx.datastore.preferences.core.stringPreferencesKey
import com.rideconnect.util.preferences.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class AuthTokenProvider @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val TOKEN_EXPIRY_KEY = stringPreferencesKey("token_expiry")
        private const val EMPTY_TOKEN = ""
        private const val TAG = "AuthTokenProvider"
    }

    // Lưu token và thời gian hết hạn
    suspend fun setToken(token: String?, expiryTime: String? = null) {
        Log.d(TAG, "Saving token: ${token?.take(10)}... with expiry: $expiryTime")
        preferenceManager.setPreference(AUTH_TOKEN_KEY, token ?: EMPTY_TOKEN)
        expiryTime?.let {
            preferenceManager.setPreference(TOKEN_EXPIRY_KEY, it)
        }
    }

    // Lấy token dưới dạng Flow
    suspend fun getTokenFlow(): Flow<String> = preferenceManager.getPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN)
        .map {
            Log.d(TAG, "Getting token flow: ${it.take(10)}...")
            it
        }

    // Lấy token ngay lập tức (blocking)
    fun getToken(): String? = runBlocking {
        val token = preferenceManager.getPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN).firstOrNull()
        Log.d(TAG, "Getting token directly: ${token?.take(10)}...")
        if (token.isNullOrEmpty()) null else token
    }

    // Xóa token
    suspend fun clearToken() {
        Log.d(TAG, "Clearing token")
        preferenceManager.setPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN)
        preferenceManager.setPreference(TOKEN_EXPIRY_KEY, "")
    }

    // Kiểm tra token có hợp lệ không
    suspend fun isTokenValid(): Boolean {
        val token = preferenceManager.getPreference(AUTH_TOKEN_KEY, EMPTY_TOKEN).firstOrNull()
        Log.d(TAG, "Checking token validity: ${token?.take(10)}...")

        if (token.isNullOrEmpty()) {
            Log.d(TAG, "Token is empty or null")
            return false
        }

        val expiryTimeStr = preferenceManager.getPreference(TOKEN_EXPIRY_KEY, "").firstOrNull()
        if (expiryTimeStr.isNullOrEmpty()) {
            Log.d(TAG, "No expiry time found, assuming token is valid")
            return true // Nếu không có expiry, coi như token còn hạn
        }

        try {
            val expiryTime = ZonedDateTime.parse(expiryTimeStr)
            val isValid = ZonedDateTime.now().isBefore(expiryTime)
            Log.d(TAG, "Token expires at: $expiryTime, is valid: $isValid")
            return isValid
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing expiry time: ${e.message}")
            return false
        }
    }
}
