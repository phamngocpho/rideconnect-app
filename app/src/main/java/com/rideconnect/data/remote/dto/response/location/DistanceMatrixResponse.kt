package com.rideconnect.data.remote.dto.response.location

import com.google.gson.annotations.SerializedName

data class DistanceMatrixResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("origin_addresses")
    val originAddresses: List<String>,
    @SerializedName("destination_addresses")
    val destinationAddresses: List<String>,
    @SerializedName("rows")
    val rows: List<Row>
)

data class Row(
    @SerializedName("elements")
    val elements: List<Element>
)

data class Element(
    @SerializedName("status")
    val status: String,
    @SerializedName("distance")
    val distance: Distance?,
    @SerializedName("duration")
    val duration: Duration?,
    @SerializedName("duration_in_traffic")
    val durationInTraffic: Duration?
)
