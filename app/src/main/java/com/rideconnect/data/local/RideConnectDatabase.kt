package com.rideconnect.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rideconnect.data.local.converter.DateTimeConverter
import com.rideconnect.data.local.converter.LocationConverter
import com.rideconnect.data.local.dao.*
import com.rideconnect.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        TripHistoryEntity::class,
        SavedAddressEntity::class,
        AppSettingsEntity::class,
        RecentSearchEntity::class,
        NotificationEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTimeConverter::class,
    LocationConverter::class
)
abstract class RideConnectDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tripHistoryDao(): TripHistoryDao
    abstract fun savedAddressDao(): SavedAddressDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun notificationDao(): NotificationDao
    abstract fun messageDao(): MessageDao
}
