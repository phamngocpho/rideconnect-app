package com.rideconnect.data.remote.dto.response.location

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("results")
    val results: List<GeocodingResult>
)

data class GeocodingResult(
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @SerializedName("geometry")
    val geometry: GeocodeGeometry,
    @SerializedName("address_components")
    val addressComponents: List<GeoAddressComponent>,
    @SerializedName("plus_code")
    val plusCode: PlusCode?
)

data class GeocodeGeometry(
    @SerializedName("location")
    val location: LatLng,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("viewport")
    val viewport: Bounds
)

data class GeoAddressComponent(
    @SerializedName("long_name")
    val longName: String,
    @SerializedName("short_name")
    val shortName: String,
    @SerializedName("types")
    val types: List<String>
)

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String,
    @SerializedName("global_code")
    val globalCode: String
)
