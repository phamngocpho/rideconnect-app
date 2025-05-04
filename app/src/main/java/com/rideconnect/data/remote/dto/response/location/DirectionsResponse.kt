package com.rideconnect.data.remote.dto.response.location

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("routes")
    val routes: List<Route>
)

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolyline,
    @SerializedName("bounds")
    val bounds: Bounds,
    @SerializedName("legs")
    val legs: List<Leg>,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("distance")
    val distance: Distance,
    @SerializedName("duration")
    val duration: Duration
)

data class OverviewPolyline(
    @SerializedName("points")
    val points: String
)

data class Bounds(
    @SerializedName("northeast")
    val northeast: LatLng,
    @SerializedName("southwest")
    val southwest: LatLng
)

data class Leg(
    @SerializedName("steps")
    val steps: List<Step>,
    @SerializedName("distance")
    val distance: Distance,
    @SerializedName("duration")
    val duration: Duration,
    @SerializedName("start_location")
    val startLocation: LatLng,
    @SerializedName("end_location")
    val endLocation: LatLng,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("end_address")
    val endAddress: String
)

data class Step(
    @SerializedName("distance")
    val distance: Distance,
    @SerializedName("duration")
    val duration: Duration,
    @SerializedName("start_location")
    val startLocation: LatLng,
    @SerializedName("end_location")
    val endLocation: LatLng,
    @SerializedName("html_instructions")
    val htmlInstructions: String,
    @SerializedName("maneuver")
    val maneuver: String?,
    @SerializedName("polyline")
    val polyline: OverviewPolyline,
    @SerializedName("travel_mode")
    val travelMode: String
)

data class Distance(
    @SerializedName("text")
    val text: String,
    @SerializedName("value")
    val value: Int
)

data class Duration(
    @SerializedName("text")
    val text: String,
    @SerializedName("value")
    val value: Int
)

data class LatLng(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
