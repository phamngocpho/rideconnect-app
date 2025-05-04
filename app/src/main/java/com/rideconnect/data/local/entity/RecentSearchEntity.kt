package com.rideconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rideconnect.domain.model.location.Location
import java.time.ZonedDateTime

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val address: String,
    val location: Location,
    val searchTime: ZonedDateTime
)
