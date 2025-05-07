package com.rideconnect.presentation.components.navigation

import android.Manifest
import android.R.attr.duration
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.LocationPuck3D
import com.mapbox.maps.plugin.ModelScaleMode
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions

@ExperimentalPreviewMapboxNavigationAPI
@Composable
fun NavigationComponent(
    originLongitude: Double,
    originLatitude: Double,
    destinationLongitude: Double,
    destinationLatitude: Double
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val originPoint = Point.fromLngLat(originLongitude, originLatitude)
    val destinationPoint = Point.fromLngLat(destinationLongitude, destinationLatitude)

    // Kiểm tra quyền truy cập vị trí
    val hasLocationPermission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasLocationPermission) {
        Toast.makeText(
            context,
            "Ứng dụng cần quyền truy cập vị trí để hoạt động",
            Toast.LENGTH_LONG
        ).show()
        return
    }

    val navigationOptions = remember { NavigationOptions.Builder(context).build() }
    val navigationLocationProvider = remember { NavigationLocationProvider() }

    LaunchedEffect(Unit) {
        MapboxNavigationApp.setup(navigationOptions)
    }

    val mapboxNavigation = remember {
        MapboxNavigationProvider.create(navigationOptions)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                mapboxNavigation.stopTripSession()
                MapboxNavigationApp.disable()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapboxNavigation.stopTripSession()
            MapboxNavigationApp.disable()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                createMapView(
                    context = ctx,
                    originPoint = originPoint,
                    destinationPoint = destinationPoint,
                    navigationLocationProvider = navigationLocationProvider,
                    mapboxNavigation = mapboxNavigation
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@SuppressLint("MissingPermission", "VisibleForTests")
@ExperimentalPreviewMapboxNavigationAPI
private fun createMapView(
    context: Context,
    originPoint: Point,
    destinationPoint: Point,
    navigationLocationProvider: NavigationLocationProvider,
    mapboxNavigation: MapboxNavigation
): MapView {
    val mapInitOptions = MapInitOptions(
        context = context,
        styleUri = Style.STANDARD,
    )

    val mapView = MapView(context, mapInitOptions)

    // Thiết lập camera ban đầu
    mapView.mapboxMap.setCamera(
        CameraOptions.Builder()
            .center(originPoint)
            .zoom(15.0)
            .build()
    )

    // Thiết lập location puck 3D
    mapView.location.apply {
        setLocationProvider(navigationLocationProvider)
        // Thay đổi sang LocationPuck3D
        locationPuck = LocationPuck3D(
            modelUri = "mapbox://models/mapbox/mapbox-navigation-puck-3d",
            modelScale = listOf(0.6f, 0.6f, 0.6f),        // Scale nhỏ hơn để phù hợp với map
            modelRotation = listOf(0f, 0f, 90f),          // Xoay 90 độ để mũi tên chỉ đúng hướng
            modelOpacity = 0.9f,                          // Độ trong suốt
            modelCastShadows = true,                      // Cho phép tạo bóng
            modelReceiveShadows = true,                   // Cho phép nhận bóng
            modelScaleMode = ModelScaleMode.VIEWPORT      // Scale theo viewport để kích thước không đổi khi zoom
        )

        enabled = true
    }

    // Khởi tạo viewport data source với padding phù hợp cho chế độ following
    val viewportDataSource = MapboxNavigationViewportDataSource(mapView.mapboxMap)
    val pixelDensity = context.resources.displayMetrics.density
    viewportDataSource.followingPadding = EdgeInsets(
        180.0 * pixelDensity,
        40.0 * pixelDensity,
        150.0 * pixelDensity,
        40.0 * pixelDensity
    )

    // Khởi tạo navigation camera với chế độ following
    val navigationCamera = NavigationCamera(
        mapboxMap = mapView.mapboxMap,
        cameraPlugin = mapView.camera,
        viewportDataSource = viewportDataSource
    )

    // Thiết lập chế độ following cho camera
    navigationCamera.requestNavigationCameraToFollowing()

    // Khởi tạo route line components
    val routeLineApi = MapboxRouteLineApi(MapboxRouteLineApiOptions.Builder().build())
    val routeLineView = MapboxRouteLineView(
        MapboxRouteLineViewOptions.Builder(context).build()
    )

    // Routes observer
    val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.navigationRoutes.isNotEmpty()) {
            routeLineApi.setNavigationRoutes(routeUpdateResult.navigationRoutes) { value ->
                mapView.mapboxMap.style?.let { style ->
                    routeLineView.renderRouteDrawData(style, value)
                }
            }

            viewportDataSource.onRouteChanged(routeUpdateResult.navigationRoutes.first())
            viewportDataSource.evaluate()
        }
    }

    // Location observer để cập nhật vị trí và hướng của camera
    val locationObserver = object : LocationObserver {
        override fun onNewRawLocation(rawLocation: Location) {}

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            // Cập nhật viewport và camera theo vị trí mới
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

            // Cập nhật góc camera theo hướng của thiết bị
            mapView.camera.easeTo(
                CameraOptions.Builder()
                    .bearing(enhancedLocation.bearing?.toDouble())
                    .build(),
                mapAnimationOptions {
                    duration(300)
                }
            )
        }
    }

    // Đăng ký observers
    mapboxNavigation.registerRoutesObserver(routesObserver)
    mapboxNavigation.registerLocationObserver(locationObserver)

    // Bắt đầu trip session với vị trí thực
    mapboxNavigation.startTripSession()

    // Yêu cầu tạo route
    mapboxNavigation.requestRoutes(
        RouteOptions.builder()
            .applyDefaultNavigationOptions()
            .coordinatesList(listOf(originPoint, destinationPoint))
            .layersList(listOf(mapboxNavigation.getZLevel(), null))
            .build(),
        object : NavigationRouterCallback {
            override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {}

            override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                Toast.makeText(
                    context,
                    "Không thể tạo route: ${reasons.firstOrNull()?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: String) {
                mapboxNavigation.setNavigationRoutes(routes)
            }
        }
    )

    return mapView
}

