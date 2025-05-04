package com.rideconnect.data.local.converter

import androidx.room.TypeConverter
import com.rideconnect.domain.model.trip.TripStatus

class TripStatusConverter {
    @TypeConverter
    fun fromTripStatus(status: TripStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toTripStatus(statusName: String?): TripStatus? {
        return statusName?.let { TripStatus.valueOf(it) }
    }
}
