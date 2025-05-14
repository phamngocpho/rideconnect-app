package com.rideconnect.presentation.components.navigation

import android.Manifest
import com.rideconnect.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.api.directions.v5.models.BannerInstructions
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.LocationPuck2D
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
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.BannerInstructionsObserver
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
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

    // State for navigation instructions
    var currentStreetName by remember { mutableStateOf("") }
    var distanceToNextManeuver by remember { mutableStateOf("") }

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

        // Banner instructions observer
        val bannerInstructionsObserver = BannerInstructionsObserver { bannerInstructions ->
            bannerInstructions.primary()?.let { primary ->
                currentStreetName = primary.text() ?: ""
            }
        }

        // Route progress observer
        val routeProgressObserver = RouteProgressObserver { routeProgress ->
            val nextManeuverDistance = routeProgress.currentLegProgress?.currentStepProgress?.distanceRemaining
            nextManeuverDistance?.let {
                distanceToNextManeuver = formatDistance(it.toDouble())
            }
        }

        mapboxNavigation.registerBannerInstructionsObserver(bannerInstructionsObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapboxNavigation.unregisterBannerInstructionsObserver(bannerInstructionsObserver)
            mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
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

        // Navigation direction banner
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2C3E50))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Turn arrow icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_turn_right),
                    contentDescription = "Turn direction",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Street name and distance
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = currentStreetName.ifEmpty { "16th Street" },
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = distanceToNextManeuver.ifEmpty { "100 ft" },
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

// Helper function to format distance
private fun formatDistance(distanceInMeters: Double): String {
    return when {
        distanceInMeters < 30 -> "${distanceInMeters.toInt()} ft"
        distanceInMeters < 1000 -> "${(distanceInMeters / 0.3048).toInt()} ft"
        else -> String.format("%.1f mi", distanceInMeters / 1609.34)
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

    // Thiết lập location puck 2D
    mapView.location.apply {
        setLocationProvider(navigationLocationProvider)
        locationPuck = LocationPuck2D(
            bearingImage = ImageHolder.Companion.from(
                R.drawable.mapbox_navigation_puck_icon
            ),
            topImage = ImageHolder.Companion.from(
                R.drawable.mapbox_navigation_puck_icon  // Sử dụng cùng icon cho top image
            ),
            shadowImage = ImageHolder.Companion.from(
                R.drawable.mapbox_navigation_puck_shadow  // Shadow icon mới tạo
            )
        )

        puckBearingEnabled = true
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
