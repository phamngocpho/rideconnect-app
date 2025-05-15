package com.rideconnect.di

import com.rideconnect.data.repository.AuthRepositoryImpl
import com.rideconnect.data.repository.LocationRepositoryImpl
import com.rideconnect.data.repository.TripRepositoryImpl
import com.rideconnect.domain.repository.AuthRepository
import com.rideconnect.domain.repository.LocationRepository
import com.rideconnect.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindTripRepository(tripRepositoryImpl: TripRepositoryImpl): TripRepository
}
