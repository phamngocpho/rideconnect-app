package com.rideconnect.data.remote.dto.response.location

data class PlaceDetailResponse(
    val result: PlaceDetail,
    val status: String
)

data class PlaceDetail(
    val name: String,
    val formatted_address: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)