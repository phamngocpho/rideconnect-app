package com.rideconnect.data.remote.api

import com.rideconnect.BuildConfig
import com.rideconnect.data.remote.dto.response.location.DirectionsResponse
import com.rideconnect.data.remote.dto.response.location.DistanceMatrixResponse
import com.rideconnect.data.remote.dto.response.location.GeocodeResponse
import com.rideconnect.data.remote.dto.response.location.PlaceAutocompleteResponse
import com.rideconnect.data.remote.dto.response.location.PlaceDetailResponse
import com.rideconnect.data.remote.dto.response.location.ReverseGeocodeResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoongMapApi {
    @GET(ApiConstants.GOONG_PLACE_AUTOCOMPLETE_ENDPOINT)
    suspend fun getPlaceAutocomplete(
        @Query("input") input: String,
        @Query("location") location: String? = null, // format: "latitude,longitude"
        @Query("radius") radius: Int? = null,
        @Query("more_compound") moreCompound: Boolean = true
    ): Response<PlaceAutocompleteResponse>

    @GET(ApiConstants.GOONG_PLACE_DETAIL_ENDPOINT)
    suspend fun getPlaceDetail(
        @Query("place_id") placeId: String
    ): Response<PlaceDetailResponse>

    // chuyển tọa độ thành địa chỉ
    @GET(ApiConstants.GOONG_REVERSE_GEOCODE_ENDPOINT)
    suspend fun reverseGeocode(
        @Query("latlng") latlng: String,
        @Query("api_key") apiKey: String = BuildConfig.GOONG_API_KEY
    ): Response<ReverseGeocodeResponse>

    // API chỉ đường
    @GET(ApiConstants.GOONG_DIRECTION_ENDPOINT)
    suspend fun getDirections(
        @Query("origin") origin: String, // format: "latitude,longitude" hoặc "place_id:ChIJ..."
        @Query("destination") destination: String, // format: "latitude,longitude" hoặc "place_id:ChIJ..."
        @Query("vehicle") vehicle: String, // "car", "bike", "taxi", "truck", "hd"
        @Query("alternatives") alternatives: Boolean? = null,
        @Query("avoid") avoid: String? = null, // "tolls", "ferries"
        @Query("waypoints") waypoints: String? = null, // format: "latitude,longitude|latitude,longitude"
        @Query("optimize") optimize: Boolean? = null,
        @Query("language") language: String? = null,
    ): Response<DirectionsResponse>

    // API tính khoảng cách và thời gian di chuyển
    @GET(ApiConstants.GOONG_DISTANCE_MATRIX_ENDPOINT)
    suspend fun getDistanceMatrix(
        @Query("origins") origins: String, // format: "latitude,longitude|latitude,longitude"
        @Query("destinations") destinations: String, // format: "latitude,longitude|latitude,longitude"
        @Query("vehicle") vehicle: String, // "car", "bike", "taxi", "truck", "hd"
        @Query("avoid") avoid: String? = null, // "tolls", "ferries"
        @Query("language") language: String? = null,
    ): Response<DistanceMatrixResponse>

    // API Geocoding (chuyển đổi địa chỉ thành tọa độ)
    @GET(ApiConstants.GOONG_GEOCODE_ENDPOINT)
    suspend fun geocode(
        @Query("address") address: String,
    ): Response<GeocodeResponse>
}
