package com.rideconnect.presentation.components.maps

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager

@Composable
fun RouteMapComponent(
    modifier: Modifier = Modifier,
    routePoints: List<Point>,
    routeColor: String = "#4A90E2",
    routeWidth: Double = 8.0
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var polylineAnnotationManager: PolylineAnnotationManager? by remember { mutableStateOf(null) }

    val mapView = remember {
        MapView(context).apply {
            mapboxMap.loadStyle(Style.MAPBOX_STREETS)
        }
    }

    // Update route khi có thay đổi
    LaunchedEffect(routePoints) {
        if (routePoints.isNotEmpty()) {
            // Xóa annotations cũ
            polylineAnnotationManager?.deleteAll()

            // Tạo polyline annotation manager nếu chưa có
            if (polylineAnnotationManager == null) {
                polylineAnnotationManager = mapView.annotations.createPolylineAnnotationManager()
            }

            // Tạo route với shadow
            val shadowRouteOptions = PolylineAnnotationOptions()
                .withPoints(routePoints)
                .withLineColor("#000000")
                .withLineWidth(routeWidth + 3.0)
                .withLineOpacity(0.2)
                .withLineSortKey(1.0)

            // Tạo main route
            val mainRouteOptions = PolylineAnnotationOptions()
                .withPoints(routePoints)
                .withLineColor(routeColor)
                .withLineWidth(routeWidth)
                .withLineSortKey(2.0)

            // Add routes
            polylineAnnotationManager?.create(shadowRouteOptions)
            polylineAnnotationManager?.create(mainRouteOptions)

            // Tính toán bounds với padding
            val minLat = routePoints.minOf { it.latitude() }
            val maxLat = routePoints.maxOf { it.latitude() }
            val minLng = routePoints.minOf { it.longitude() }
            val maxLng = routePoints.maxOf { it.longitude() }

            // Thêm padding 30%
            val latPadding = (maxLat - minLat) * 0.4
            val lngPadding = (maxLng - minLng) * 0.4

            val northeast = Point.fromLngLat(
                maxLng + lngPadding,
                maxLat + latPadding
            )
            val southwest = Point.fromLngLat(
                minLng - lngPadding,
                minLat - latPadding
            )

            // Camera animation với padding và zoom control
            val cameraPosition = mapView.mapboxMap.cameraForCoordinates(
                coordinates = listOf(southwest, northeast),
                EdgeInsets(
                    120.0,
                    120.0,
                    120.0,
                    120.0
                ),
                bearing = 0.0,
                pitch = 0.0
            )

            // Điều chỉnh zoom level
            val adjustedCamera = cameraPosition.toBuilder().apply {
                // Giới hạn zoom trong khoảng 11-16
                zoom(
                    (cameraPosition.zoom ?: 0.0).coerceIn(10.0, 15.0)
                )
            }.build()

            // Áp dụng animation
            mapView.mapboxMap.flyTo(
                adjustedCamera,
                MapAnimationOptions.Builder()
                    .duration(1000L)
                    .build()
            )
        }
    }

    // Cleanup
    DisposableEffect(lifecycleOwner) {
        onDispose {
            polylineAnnotationManager?.deleteAll()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
