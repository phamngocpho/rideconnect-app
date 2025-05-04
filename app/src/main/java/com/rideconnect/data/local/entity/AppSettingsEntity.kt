package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1, // Singleton
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val language: String = "vi",
    val defaultPaymentMethod: String? = null,
    val defaultVehicleType: String? = null
)
