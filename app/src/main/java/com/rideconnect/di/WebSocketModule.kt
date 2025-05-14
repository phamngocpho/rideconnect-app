package com.rideconnect.di

import com.google.gson.Gson
import com.rideconnect.data.remote.websocket.RideConnectWebSocket
import com.rideconnect.data.remote.websocket.WebSocketManager
import com.rideconnect.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideRideConnectWebSocket(
        okHttpClient: OkHttpClient,
        authRepository: AuthRepository
    ): RideConnectWebSocket {
        return RideConnectWebSocket(okHttpClient, authRepository)
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(
        webSocket: RideConnectWebSocket,
        gson: Gson
    ): WebSocketManager {
        return WebSocketManager(webSocket, gson)
    }
}