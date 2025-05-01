package com.rideconnect.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rideconnect.domain.model.location.Location

class LocationListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocationList(locations: List<Location>?): String? {
        return locations?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLocationList(locationsString: String?): List<Location>? {
        if (locationsString == null) return null
        val type = object : TypeToken<List<Location>>() {}.type
        return gson.fromJson(locationsString, type)
    }
}
