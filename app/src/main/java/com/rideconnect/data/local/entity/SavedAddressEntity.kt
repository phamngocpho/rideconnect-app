package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rideconnect.domain.model.location.Location

@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String,
    val location: Location,
    val type: String, // "HOME", "WORK", "FAVORITE"
    val isDefault: Boolean
)
