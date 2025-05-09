package com.rideconnect.domain.repository

import com.mapbox.geojson.Point
import com.rideconnect.data.remote.dto.response.location.DirectionsResponse
import com.rideconnect.data.remote.dto.response.location.DistanceMatrixResponse
import com.rideconnect.data.remote.dto.response.location.GeocodeResponse
import com.rideconnect.data.remote.dto.response.location.PlaceAutocompleteResponse
import com.rideconnect.data.remote.dto.response.location.PlaceDetailResponse
import com.rideconnect.data.remote.dto.response.location.ReverseGeocodeResponse
import com.rideconnect.data.repository.DistanceDuration
import retrofit2.Response

interface LocationRepository {
    suspend fun getPlaceAutocomplete(
        input: String,
        location: String? = null,
        radius: Int? = null,
        moreCompound: Boolean = true
    ): Response<PlaceAutocompleteResponse>

    suspend fun getPlaceDetail(placeId: String): Response<PlaceDetailResponse>

    suspend fun reverseGeocode(latlng: String): Response<ReverseGeocodeResponse>

    suspend fun getGeocoding(address: String): Response<GeocodeResponse>

    suspend fun getReverseGeocoding(latlng: String): Response<GeocodeResponse>

    suspend fun getDistanceMatrix(
        origins: String,
        destinations: String
    ): Response<DistanceMatrixResponse>

    // Thêm phương thức mới để lấy thông tin chỉ đường
    suspend fun getDirections(
        origin: String,
        destination: String,
        vehicle: String = "car",
        alternatives: Boolean? = null,
        avoid: String? = null,
        waypoints: String? = null,
        optimize: Boolean? = null,
        language: String? = null
    ): Response<DirectionsResponse>

    suspend fun getRoutePoints(
        sourceLatitude: Double,
        sourceLongitude: Double,
        destLatitude: Double,
        destLongitude: Double
    ): List<Point>

    suspend fun getDistanceDuration(
        sourceLatitude: Double,
        sourceLongitude: Double,
        destLatitude: Double,
        destLongitude: Double
    ): DistanceDuration?

    suspend fun getDistance(
        sourceLatitude: Double,
        sourceLongitude: Double,
        destLatitude: Double,
        destLongitude: Double
    ): Int

    suspend fun getDuration(
        sourceLatitude: Double,
        sourceLongitude: Double,
        destLatitude: Double,
        destLongitude: Double
    ): Int
}
