package com.rideconnect.di

import android.content.Context
import androidx.room.Room
import com.rideconnect.data.local.RideConnectDatabase
import com.rideconnect.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRideConnectDatabase(@ApplicationContext context: Context): RideConnectDatabase {
        return Room.databaseBuilder(
                context,
                RideConnectDatabase::class.java,
                "rideconnect_db"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: RideConnectDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideMessageDao(database: RideConnectDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: RideConnectDatabase): NotificationDao {
        return database.notificationDao()
    }

    @Provides
    @Singleton
    fun provideTripHistoryDao(database: RideConnectDatabase): TripHistoryDao {
        return database.tripHistoryDao()
    }

    @Provides
    @Singleton
    fun provideSavedAddressDao(database: RideConnectDatabase): SavedAddressDao {
        return database.savedAddressDao()
    }

    @Provides
    @Singleton
    fun provideRecentSearchDao(database: RideConnectDatabase): RecentSearchDao {
        return database.recentSearchDao()
    }

    @Provides
    @Singleton
    fun provideAppSettingsDao(database: RideConnectDatabase): AppSettingsDao {
        return database.appSettingsDao()
    }
}
