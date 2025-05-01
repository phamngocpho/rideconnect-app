package com.rideconnect.util.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rideconnect.domain.model.location.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    /**
     * Kiểm tra xem ứng dụng có quyền truy cập vị trí không
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Lấy vị trí hiện tại của người dùng
     * @throws SecurityException nếu không có quyền truy cập vị trí
     * @throws Exception nếu không thể lấy vị trí vì lý do khác
     */
    suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { continuation ->
        try {
            // Kiểm tra quyền truy cập vị trí trước khi gọi API
            if (!hasLocationPermission()) {
                continuation.resumeWithException(SecurityException("Không có quyền truy cập vị trí"))
                return@suspendCancellableCoroutine
            }

            val cancellationToken = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // Reverse geocode to get address
                        try {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val address = if (addresses?.isNotEmpty() == true) {
                                addresses[0].getAddressLine(0)
                            } else {
                                "Vị trí hiện tại"
                            }

                            continuation.resume(
                                Location(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    address = address,
                                    name = "Vị trí hiện tại"
                                )
                            )
                        } catch (e: Exception) {
                            continuation.resume(
                                Location(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    address = "Vị trí hiện tại",
                                    name = "Vị trí hiện tại"
                                )
                            )
                        }
                    } else {
                        continuation.resumeWithException(Exception("Không thể lấy vị trí hiện tại"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }

            continuation.invokeOnCancellation {
                cancellationToken.cancel()
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    /**
     * Lấy vị trí mặc định nếu không thể lấy vị trí thực
     */
    fun getDefaultLocation(): Location {
        // Vị trí mặc định (có thể là trung tâm thành phố Hồ Chí Minh hoặc Hà Nội)
        return Location(
            latitude = 10.762622,  // Vị trí mặc định (Hồ Chí Minh)
            longitude = 106.660172,
            address = "Thành phố Hồ Chí Minh",
            name = "Vị trí mặc định"
        )
    }
}
