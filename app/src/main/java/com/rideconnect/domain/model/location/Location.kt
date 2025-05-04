package com.rideconnect.domain.model.location

import com.google.gson.Gson

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val name: String? = null,
    val additionalDetails: String? = null
) {
    companion object {
        // Hàm để chuyển đổi từ JSON sang đối tượng Location
        fun fromJson(json: String): Location? {
            return try {
                Gson().fromJson(json, Location::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    // Hàm để chuyển đổi từ đối tượng Location sang JSON
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
