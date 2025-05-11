package com.rideconnect.data.repository

import android.util.Log
import com.mapbox.geojson.Point
import com.rideconnect.data.remote.api.GoongMapApi
import com.rideconnect.data.remote.api.LocationApi
import com.rideconnect.data.remote.dto.request.location.LocationUpdateRequest
import com.rideconnect.data.remote.dto.response.location.DirectionsResponse
import com.rideconnect.data.remote.dto.response.location.DistanceMatrixResponse
import com.rideconnect.data.remote.dto.response.location.GeocodeResponse
import com.rideconnect.data.remote.dto.response.location.PlaceAutocompleteResponse
import com.rideconnect.data.remote.dto.response.location.PlaceDetailResponse
import com.rideconnect.data.remote.dto.response.location.ReverseGeocodeResponse
import com.rideconnect.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val goongMapApi: GoongMapApi,
    private val locationApi: LocationApi
) : LocationRepository {
    private val TAG = "LocationRepositoryImpl"

    override suspend fun getPlaceAutocomplete(
        input: String,
        location: String?,
        radius: Int?,
        moreCompound: Boolean
    ): Response<PlaceAutocompleteResponse> {
        Log.d(TAG, "getPlaceAutocomplete: input=$input, location=$location, radius=$radius, moreCompound=$moreCompound")
        return goongMapApi.getPlaceAutocomplete(
            input = input,
            location = location,
            radius = radius,
            moreCompound = moreCompound
        )
    }

    override suspend fun getPlaceDetail(placeId: String): Response<PlaceDetailResponse> {
        Log.d(TAG, "getPlaceDetail: placeId=$placeId")
        return goongMapApi.getPlaceDetail(placeId = placeId)
    }

    override suspend fun reverseGeocode(latlng: String): Response<ReverseGeocodeResponse> {
        Log.d(TAG, "reverseGeocode: latlng=$latlng")
        return goongMapApi.reverseGeocode(latlng = latlng)
    }

    override suspend fun getGeocoding(address: String): Response<GeocodeResponse> {
        Log.d(TAG, "getGeocoding: address=$address")
        // Triển khai phương thức này nếu có
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun getReverseGeocoding(latlng: String): Response<GeocodeResponse> {
        Log.d(TAG, "getReverseGeocoding: latlng=$latlng")
        // Triển khai phương thức này nếu có
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun getDistanceMatrix(
        origins: String,
        destinations: String
    ): Response<DistanceMatrixResponse> {
        Log.d(TAG, "getDistanceMatrix: origins=$origins, destinations=$destinations")
        // Triển khai phương thức này nếu có
        throw NotImplementedError("Method not implemented")
    }

    // Triển khai phương thức getDirections
    override suspend fun getDirections(
        origin: String,
        destination: String,
        vehicle: String,
        alternatives: Boolean?,
        avoid: String?,
        waypoints: String?,
        optimize: Boolean?,
        language: String?
    ): Response<DirectionsResponse> {
        Log.d(TAG, "getDirections: origin=$origin, destination=$destination, vehicle=$vehicle")
        return goongMapApi.getDirections(
            origin = origin,
            destination = destination,
            vehicle = vehicle,
            alternatives = alternatives,
            avoid = avoid,
            waypoints = waypoints,
            optimize = optimize,
            language = language
        )
    }

    override suspend fun getRoutePoints(
        sourceLatitude: Double,
        sourceLongitude: Double,
        destLatitude: Double,
        destLongitude: Double
    ): List<Point> = withContext(Dispatchers.IO) {
        Log.d(TAG, "getRoutePoints: từ (lat=$sourceLatitude, lng=$sourceLongitude) đến (lat=$destLatitude, lng=$destLongitude)")

        try {
            val origin = "$sourceLatitude,$sourceLongitude"
            val destination = "$destLatitude,$destLongitude"
            Log.d(TAG, "Đang gọi API với origin=$origin, destination=$destination")

            val response = goongMapApi.getDirections(
                origin = origin,
                destination = destination,
                vehicle = "car"
            )

            if (response.isSuccessful) {
                Log.d(TAG, "API call thành công: ${response.code()}")
                val directionsResponse = response.body()

                if (directionsResponse != null) {
                    val routes = directionsResponse.routes
                    Log.d(TAG, "Số lượng tuyến đường nhận được: ${routes.size}")

                    if (routes.isNotEmpty()) {
                        // Lấy tuyến đường đầu tiên
                        val firstRoute = routes[0]

                        // Log thông tin về tuyến đường mà không tính tổng khoảng cách
                        Log.d(TAG, "Tuyến đường đầu tiên: ${firstRoute.legs?.size ?: 0} chặng")
                        firstRoute.legs?.forEachIndexed { index, leg ->
                            Log.d(TAG, "Chặng $index: khoảng cách=${leg.distance}, thời gian=${leg.duration}")
                        }

                        // Lấy danh sách các điểm tọa độ từ geometry
                        val encodedPolyline = firstRoute.overviewPolyline.points
                        Log.d(TAG, "Encoded polyline: ${encodedPolyline.take(20)}...")

                        // Giải mã polyline thành danh sách các điểm
                        val points = decodePolyline(encodedPolyline)
                        Log.d(TAG, "Đã giải mã thành ${points.size} điểm")

                        if (points.isNotEmpty()) {
                            val first = points.first()
                            val last = points.last()
                            Log.d(TAG, "Điểm đầu: (${first.longitude()}, ${first.latitude()}), Điểm cuối: (${last.longitude()}, ${last.latitude()})")
                        }

                        return@withContext points
                    } else {
                        Log.w(TAG, "Không có tuyến đường nào được tìm thấy")
                    }
                } else {
                    Log.w(TAG, "Response body rỗng")
                }
            } else {
                Log.e(TAG, "API call thất bại: ${response.code()} - ${response.errorBody()?.string()}")
            }

            // Trả về danh sách rỗng nếu không có tuyến đường
            Log.w(TAG, "Trả về danh sách điểm rỗng")
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Exception trong getRoutePoints: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }

    // Hàm giải mã polyline thành danh sách các điểm
    private fun decodePolyline(encoded: String): List<Point> {
        Log.d(TAG, "decodePolyline: độ dài encoded string = ${encoded.length}")

        val poly = ArrayList<Point>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        try {
            while (index < len) {
                var b: Int
                var shift = 0
                var result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat

                shift = 0
                result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dlng

                val p = Point.fromLngLat(
                    lng.toDouble() / 1E5,
                    lat.toDouble() / 1E5
                )
                poly.add(p)
            }

            Log.d(TAG, "Đã giải mã thành công ${poly.size} điểm")

            if (poly.size > 1) {
                val first = poly.first()
                val last = poly.last()
                Log.d(TAG, "Phạm vi điểm: từ (${first.longitude()}, ${first.latitude()}) đến (${last.longitude()}, ${last.latitude()})")
            }

            return poly
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi giải mã polyline: ${e.message}", e)
            return emptyList()
        }
    }

    override suspend fun updateDriverLocation(location: Point): Response<Unit> {
        Log.d(TAG, "updateDriverLocation: Chuẩn bị gửi vị trí - lat=${location.latitude()}, lng=${location.longitude()}")

        try {
            val currentTimeMillis = System.currentTimeMillis()

            val request = LocationUpdateRequest(
                latitude = location.latitude(),
                longitude = location.longitude(),
                heading = null,
                speed = null,
                bearing = 0f,
                accuracy = 0f,
                timestamp = currentTimeMillis
            )

            Log.d(TAG, "updateDriverLocation: Gửi request - lat=${request.latitude}, lng=${request.longitude}, " +
                    "bearing=${request.bearing}, accuracy=${request.accuracy}, timestamp=${request.timestamp}")

            val response = locationApi.updateLocation(request)

            if (response.isSuccessful) {
                Log.d(TAG, "updateDriverLocation: API call thành công - HTTP ${response.code()}")
            } else {
                Log.e(TAG, "updateDriverLocation: API call thất bại - HTTP ${response.code()}, message: ${response.message()}")
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "updateDriverLocation: Error body: $errorBody")
            }

            return response
        } catch (e: Exception) {
            Log.e(TAG, "updateDriverLocation: Exception khi gọi API - ${e.message}", e)
            throw e
        }
    }

}
