package com.rideconnect.domain.model.driver

import java.time.LocalDateTime

data class Activity(
    val name: String,
    val carModel: String,
    val time: LocalDateTime,
    val status: ActivityStatus = ActivityStatus.ACTIVE
)

enum class ActivityStatus {
    ACTIVE, COMPLETED, CANCELLED
}
