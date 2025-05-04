package com.rideconnect.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.rideconnect.domain.model.location.Location

class LocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return location?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return locationString?.let { gson.fromJson(it, Location::class.java) }
    }
}
