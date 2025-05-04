package com.rideconnect.presentation.components.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures

@Composable
fun SearchingMapComponent(
    modifier: Modifier = Modifier,
    searchRadius: Double,
    centerLocation: Point,
    onMapViewCreated: (MapView) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isMapStyleLoaded by remember { mutableStateOf(false) }
    var circleAnnotationManager: CircleAnnotationManager? by remember { mutableStateOf(null) }

    val mapView = remember {
        MapView(context).apply {
            mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                isMapStyleLoaded = true

                this.gestures.apply {
                    pitchEnabled = false
                    rotateEnabled = false
                    scrollEnabled = false
                    pinchToZoomEnabled = false
                    doubleTapToZoomInEnabled = false
                    doubleTouchToZoomOutEnabled = false
                }

                mapboxMap.setCamera(
                    CameraOptions.Builder()
                        .center(centerLocation)
                        .zoom(14.0)
                        .bearing(0.0)
                        .pitch(0.0)
                        .build()
                )

                onMapViewCreated(this)
            }
        }
    }

    LaunchedEffect(isMapStyleLoaded) {
        if (isMapStyleLoaded) {
            circleAnnotationManager?.deleteAll()

            if (circleAnnotationManager == null) {
                circleAnnotationManager = mapView.annotations.createCircleAnnotationManager()
            }

            // Chỉ vẽ điểm trung tâm (chấm xanh)
            val centerPoint = CircleAnnotationOptions()
                .withPoint(centerLocation)
                .withCircleRadius(6.0)
                .withCircleColor("#4A90E2")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeColor("#FFFFFF")
                .withCircleOpacity(1.0)

            circleAnnotationManager?.create(centerPoint)
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            circleAnnotationManager?.deleteAll()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
