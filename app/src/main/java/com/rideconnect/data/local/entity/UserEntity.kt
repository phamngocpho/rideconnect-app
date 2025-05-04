package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val phoneNumber: String,
    val email: String?,
    val fullName: String,
    val avatarUrl: String?,
    val userType: String, // "DRIVER" or "CUSTOMER"
    val authToken: String?,
    val refreshToken: String?,
    val tokenExpiryDate: ZonedDateTime?,
    val lastSyncTime: ZonedDateTime
)
