package com.rideconnect.presentation.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.rideconnect.util.map.MapRouteUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RouteMapComponent(
    modifier: Modifier = Modifier,
    mapViewportState: MapViewportState,
    routePoints: List<Point> = emptyList(),
    showSourceMarker: Boolean = false,
    routeColor: Color = Color(0xFF4285F4),
    routeWidth: Double = 8.0
) {
    val TAG = "RouteMapComponent"

    // State để kiểm soát việc hiển thị route
    var shouldShowRoute by remember { mutableStateOf(false) }

    val routeFeatureCollection = remember(routePoints) {
        MapRouteUtils.createRouteFeatureCollection(routePoints)
    }

    // Thêm delay trước khi hiển thị route
    LaunchedEffect(routePoints) {
        shouldShowRoute = false
        if (routePoints.isNotEmpty()) {
            delay(500) // Thêm delay để đảm bảo map đã load
            shouldShowRoute = true
        }
    }

    val bounds = remember(routePoints) {
        if (routePoints.isNotEmpty()) {
            var minLat = routePoints.minOf { it.latitude() }
            var maxLat = routePoints.maxOf { it.latitude() }
            var minLng = routePoints.minOf { it.longitude() }
            var maxLng = routePoints.maxOf { it.longitude() }

            val latPadding = (maxLat - minLat) * 0.2
            val lngPadding = (maxLng - minLng) * 0.2

            CoordinateBounds(
                Point.fromLngLat(minLng - lngPadding, minLat - latPadding),
                Point.fromLngLat(maxLng + lngPadding, maxLat + latPadding)
            )
        } else null
    }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = {
                MapboxStandardStyle()
                Log.d(TAG, "Đã áp dụng MapboxStandardStyle")

                // Thêm source trước
                geoJsonSource(id = "route-source") {
                    featureCollection(routeFeatureCollection)
                    lineMetrics(true)
                    Log.d(TAG, "Updated source with ${routePoints.size} points")
                }
                Log.d(TAG, "Đã tạo GeoJsonSource 'route-source'")

                // Sau đó thêm layer
                lineLayer(
                    layerId = "route-layer",
                    sourceId = "route-source"
                ) {
                    lineColor(routeColor.hashCode())
                    lineWidth(routeWidth)
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                    lineOpacity(1.0)
                    lineBlur(1.0)
                    lineDasharray(listOf(1.0, 2.0))
                }
                Log.d(TAG, "Line style: color=${routeColor.hashCode()}, width=$routeWidth")
                Log.d(TAG, "Đã tạo LineLayer 'route-layer'")
            }
        )
    }

    // Di chuyển camera sau khi map đã load
    LaunchedEffect(bounds) {
        bounds?.let { boundingBox ->
            delay(1000) // Đảm bảo map đã load
            val cameraOptions = mapViewportState.cameraForCoordinates(
                coordinates = listOf(boundingBox.southwest, boundingBox.northeast),
                coordinatesPadding = EdgeInsets(50.0, 50.0, 50.0, 50.0)
            )

            mapViewportState.flyTo(
                cameraOptions = cameraOptions,
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(1000)
                }
            )
        }
    }
}
