package com.rideconnect.domain.model.trip

enum class TripStatus {
    PENDING,
    ACCEPTED,
    ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    override fun toString(): String {
        return name.lowercase()
    }
}
