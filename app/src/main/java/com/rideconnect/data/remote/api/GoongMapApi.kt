package com.rideconnect.data.remote.api

import com.rideconnect.BuildConfig
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

    @GET(ApiConstants.GOONG_REVERSE_GEOCODE_ENDPOINT)
    suspend fun reverseGeocode(
        @Query("latlng") latlng: String,
        @Query("api_key") apiKey: String = BuildConfig.GOONG_API_KEY
    ): Response<ReverseGeocodeResponse>

}
