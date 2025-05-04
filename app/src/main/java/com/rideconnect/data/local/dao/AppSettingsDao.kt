package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings LIMIT 1")
    fun getAppSettings(): Flow<AppSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppSettings(settings: AppSettingsEntity)

    @Update
    suspend fun updateAppSettings(settings: AppSettingsEntity)

    @Query("UPDATE app_settings SET darkMode = :darkMode")
    suspend fun updateDarkMode(darkMode: Boolean)

    @Query("UPDATE app_settings SET notificationsEnabled = :enabled")
    suspend fun updateNotifications(enabled: Boolean)

    @Query("UPDATE app_settings SET language = :language")
    suspend fun updateLanguage(language: String)
}
