package com.rideconnect.data.remote.dto.response.location

data class ReverseGeocodeResponse(
    val results: List<GeocodeResult>,
    val status: String
)

data class GeocodeResult(
    val formatted_address: String,
    val address_components: List<AddressComponent>,
    val geometry: Geometry, // Sử dụng lại Geometry class từ các DTO khác
    val place_id: String
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)
