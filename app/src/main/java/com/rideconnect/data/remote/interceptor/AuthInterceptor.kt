package com.rideconnect.data.remote.interceptor

import com.rideconnect.data.remote.auth.AuthTokenProvider
import com.rideconnect.util.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: AuthTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth header for login and register endpoints
        if (originalRequest.url.encodedPath.contains("auth/login") ||
            originalRequest.url.encodedPath.contains("auth/register")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenProvider.getToken()

        val request = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        // Thực hiện request và nhận response
        val response = chain.proceed(request)

        // Xử lý khi token hết hạn (lỗi 401)
        if (response.code == 401) {
            runBlocking {
                tokenProvider.clearToken()
                SessionManager.setSessionExpired(true)
            }
        }

        return response
    }
}
