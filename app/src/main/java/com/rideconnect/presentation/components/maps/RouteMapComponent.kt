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
    var isMapStyleLoaded by remember { mutableStateOf(false) }
    var polylineAnnotationManager: PolylineAnnotationManager? by remember { mutableStateOf(null) }

    val mapView = remember {
        MapView(context).apply {
            mapboxMap.loadStyle(Style.MAPBOX_STREETS) { _ ->
                isMapStyleLoaded = true
            }
        }
    }

    // Update route khi có thay đổi và map đã sẵn sàng
    LaunchedEffect(routePoints, isMapStyleLoaded) {
        if (routePoints.isNotEmpty() && isMapStyleLoaded) {
            // Xóa annotations cũ
            polylineAnnotationManager?.deleteAll()

            // Tạo polyline annotation manager nếu chưa có
            if (polylineAnnotationManager == null) {
                polylineAnnotationManager = mapView.annotations.createPolylineAnnotationManager()
            }

            // Tính toán camera bounds
            val minLat = routePoints.minOf { it.latitude() }
            val maxLat = routePoints.maxOf { it.latitude() }
            val minLng = routePoints.minOf { it.longitude() }
            val maxLng = routePoints.maxOf { it.longitude() }

            val latPadding = (maxLat - minLat) * 0.1
            val lngPadding = (maxLng - minLng) * 0.1

            val northeast = Point.fromLngLat(
                maxLng + lngPadding,
                maxLat + latPadding
            )
            val southwest = Point.fromLngLat(
                minLng - lngPadding,
                minLat - latPadding
            )

            // Set camera position ngay lập tức
            val cameraOptions = mapView.mapboxMap.cameraForCoordinates(
                coordinates = listOf(southwest, northeast),
                EdgeInsets(50.0, 50.0, 50.0, 50.0)
            )

            mapView.mapboxMap.setCamera(cameraOptions)

            // Tạo và thêm routes
            val shadowRouteOptions = PolylineAnnotationOptions()
                .withPoints(routePoints)
                .withLineColor("#000000")
                .withLineWidth(routeWidth + 3.0)
                .withLineOpacity(0.2)
                .withLineSortKey(1.0)

            val mainRouteOptions = PolylineAnnotationOptions()
                .withPoints(routePoints)
                .withLineColor(routeColor)
                .withLineWidth(routeWidth)
                .withLineSortKey(2.0)

            polylineAnnotationManager?.create(shadowRouteOptions)
            polylineAnnotationManager?.create(mainRouteOptions)
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

