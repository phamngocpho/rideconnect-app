package com.rideconnect.presentation.components.navigation

import android.Manifest
import com.rideconnect.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.formatter.UnitType
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceUtil
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
import java.util.Locale

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

    // Sử dụng remember để tránh tạo lại các đối tượng khi recompose
    val navigationOptions = remember { NavigationOptions.Builder(context).build() }
    val navigationLocationProvider = remember { NavigationLocationProvider() }

    // State cho navigation instructions
    var currentStreetName by remember { mutableStateOf("") }
    var distanceToNextManeuver by remember { mutableStateOf("") }
    var distanceToDestination by remember { mutableDoubleStateOf(Double.MAX_VALUE) }

    // State cho trạng thái của map và route
    var isMapInitialized by remember { mutableStateOf(false) }
    var isRouteReady by remember { mutableStateOf(false) }
    var maneuverType by remember { mutableStateOf("") }

    // Khởi tạo MapboxNavigation một lần duy nhất
    val mapboxNavigation = remember {
        if (!MapboxNavigationApp.isSetup()) {
            MapboxNavigationApp.setup(navigationOptions)
        }
        MapboxNavigationProvider.create(navigationOptions)
    }

    // Xử lý vòng đời và đăng ký observers
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
            bannerInstructions.primary().let { primary ->
                currentStreetName = primary.text()
                // Lấy loại maneuver để hiển thị icon phù hợp
                maneuverType = primary.type() ?: ""
            }
        }

        // Route progress observer
        val routeProgressObserver = RouteProgressObserver { routeProgress ->
            val nextManeuverDistance = routeProgress.currentLegProgress?.currentStepProgress?.distanceRemaining
            nextManeuverDistance?.let {
                // Lấy đối tượng FormattedDistanceData
                val formattedDistance = MapboxDistanceUtil.formatDistance(
                    it.toDouble(),      // Khoảng cách (Double)
                    1,                  // Số chữ số thập phân (Int)
                    UnitType.METRIC,    // Loại đơn vị (UnitType.METRIC hoặc UnitType.IMPERIAL)
                    context,            // Context
                    Locale.getDefault() // Locale
                )

                // Kết hợp distanceAsString và distanceSuffix để tạo chuỗi hiển thị
                distanceToNextManeuver = "${formattedDistance.distanceAsString} ${formattedDistance.distanceSuffix}"
            }
            distanceToDestination = routeProgress.distanceRemaining.toDouble()
        }

        // Routes observer để biết khi nào route đã sẵn sàng
        val routesObserver = RoutesObserver { routeUpdateResult ->
            isRouteReady = routeUpdateResult.navigationRoutes.isNotEmpty()
        }

        mapboxNavigation.registerBannerInstructionsObserver(bannerInstructionsObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerRoutesObserver(routesObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapboxNavigation.unregisterBannerInstructionsObserver(bannerInstructionsObserver)
            mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
            mapboxNavigation.unregisterRoutesObserver(routesObserver)
            mapboxNavigation.stopTripSession()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Sử dụng AndroidView với onReset và onRelease để tối ưu việc tái sử dụng View
        AndroidView(
            factory = { ctx ->
                val mapView = createMapView(
                    context = ctx,
                    originPoint = originPoint,
                    destinationPoint = destinationPoint,
                    navigationLocationProvider = navigationLocationProvider,
                    mapboxNavigation = mapboxNavigation
                )

                // Đăng ký callback khi style load xong
                mapView.mapboxMap.loadStyle(Style.STANDARD) {
                    isMapInitialized = true

                    // Bắt đầu trip session và yêu cầu route chỉ khi style đã load xong
                    mapboxNavigation.startTripSession()
                    requestRoute(
                        mapboxNavigation = mapboxNavigation,
                        originPoint = originPoint,
                        destinationPoint = destinationPoint,
                        context = ctx
                    )
                }

                mapView
            },
            modifier = Modifier.fillMaxSize(),
            // Thêm onReset để tối ưu việc tái sử dụng View
            onReset = { mapView ->
                // Reset map view state nếu cần
                isMapInitialized = false
                isRouteReady = false
            },
            // Giải phóng tài nguyên khi không cần nữa
            onRelease = { mapView ->
                mapView.onStop()
                mapView.onDestroy()
            }
        )

        // Chỉ hiển thị UI khi map đã khởi tạo và route đã sẵn sàng
        if (isMapInitialized && isRouteReady) {
            // Navigation direction banner
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C3E50))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Turn arrow icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF1E2B3C), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Chọn icon dựa vào loại maneuver
                        val iconRes = getManeuverIcon(maneuverType)
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Turn direction",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Street name and distance
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = currentStreetName.ifEmpty { "" },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = distanceToNextManeuver.ifEmpty { "" },
                            color = Color(0xFFBBDEFB),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Nút "Hoàn thành chuyến đi" khi gần đến nơi
            if (distanceToDestination <= 50) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFF4CAF50))
                        .clickable {
                            mapboxNavigation.stopTripSession()
                        }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hoàn thành chuyến đi",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Hàm để chọn icon phù hợp với loại maneuver
private fun getManeuverIcon(maneuverType: String): Int {
    return when (maneuverType) {
        "turn" -> R.drawable.ic_turn_right
        "roundabout" -> R.drawable.ic_roundabout
        "arrive" -> R.drawable.ic_arrive
        "depart" -> R.drawable.ic_depart
        else -> R.drawable.ic_turn_right // Icon mặc định
    }
}

// Tách hàm yêu cầu route để code rõ ràng hơn
private fun requestRoute(
    mapboxNavigation: MapboxNavigation,
    originPoint: Point,
    destinationPoint: Point,
    context: Context
) {
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
                R.drawable.mapbox_navigation_puck_icon
            ),
            shadowImage = ImageHolder.Companion.from(
                R.drawable.mapbox_navigation_puck_shadow
            )
        )

        puckBearingEnabled = true
        enabled = true
    }

    // Khởi tạo viewport data source với padding phù hợp
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

    return mapView
}
