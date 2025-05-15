package com.rideconnect.util.extensions

import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.model.trip.TripStatus

/**
 * Extension functions cho Trip
 */

fun Trip.isCompleted(): Boolean {
    return status == TripStatus.COMPLETED
}

fun Trip.isCancelled(): Boolean {
    return status == TripStatus.CANCELLED
}

fun Trip.isActive(): Boolean {
    return status == TripStatus.ACCEPTED ||
            status == TripStatus.ARRIVED ||
            status == TripStatus.IN_PROGRESS
}

fun Trip.isPending(): Boolean {
    return status == TripStatus.PENDING
}
