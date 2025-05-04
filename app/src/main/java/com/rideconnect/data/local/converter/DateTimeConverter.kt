package com.rideconnect.data.local.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    fun fromString(value: String?): ZonedDateTime? {
        return value?.let { ZonedDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun toString(dateTime: ZonedDateTime?): String? {
        return dateTime?.format(formatter)
    }
}
