package com.rideconnect.domain.model.location

data class SavedLocation(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val type: Type = Type.OTHER
) {
    enum class Type {
        HOME, WORK, OTHER
    }
}