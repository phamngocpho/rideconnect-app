package com.rideconnect.data.remote.interceptor

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class LoggingInterceptor @Inject constructor() {

    companion object {
        private const val TAG = "API_LOG"
    }

    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d(TAG, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
