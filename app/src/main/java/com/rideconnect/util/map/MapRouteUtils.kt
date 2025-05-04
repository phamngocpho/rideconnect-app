package com.rideconnect.util.map

import android.util.Log
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.rideconnect.data.remote.dto.response.location.DirectionsResponse

object MapRouteUtils {
    private const val TAG = "MapRouteUtils"

    fun decodePolyline(encodedPolyline: String): List<Point> {
        Log.d(TAG, "Đang giải mã polyline độ dài: ${encodedPolyline.length}")

        try {
            val decodedPoints = PolylineUtils.decode(encodedPolyline, 5)
            Log.d(TAG, "Đã giải mã thành công ${decodedPoints.size} điểm từ polyline")

            val points = decodedPoints.map { Point.fromLngLat(it.longitude(), it.latitude()) }
            return points
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi giải mã polyline: ${e.message}", e)
            return emptyList()
        }
    }

    fun getRoutePointsFromDirectionsResponse(directionsResponse: DirectionsResponse): List<Point> {
        Log.d(TAG, "Đang xử lý DirectionsResponse")

        val route = directionsResponse.routes.firstOrNull()
        if (route == null) {
            Log.w(TAG, "Không tìm thấy tuyến đường trong DirectionsResponse")
            return emptyList()
        }

        val encodedPolyline = route.overviewPolyline.points
        Log.d(TAG, "Trích xuất điểm từ encoded polyline")

        return decodePolyline(encodedPolyline)
    }

    fun createRouteFeatureCollection(points: List<Point>): FeatureCollection {
        Log.d(TAG, "Đang tạo FeatureCollection từ ${points.size} điểm")

        if (points.isEmpty()) {
            Log.w(TAG, "Danh sách điểm trống, trả về FeatureCollection trống")
            return FeatureCollection.fromFeatures(emptyList())
        }

        try {
            val lineString = LineString.fromLngLats(points)
            val feature = Feature.fromGeometry(lineString)
            val featureCollection = FeatureCollection.fromFeature(feature)
            Log.d(TAG, "Đã tạo FeatureCollection thành công")

            return featureCollection
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi tạo FeatureCollection: ${e.message}", e)
            return FeatureCollection.fromFeatures(emptyList())
        }
    }
}

