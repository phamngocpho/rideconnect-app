package com.rideconnect.data.repository

import android.util.Log
import com.rideconnect.data.remote.api.TripApi
import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse
import com.rideconnect.data.remote.websocket.WebSocketManager
import com.rideconnect.domain.model.trip.DriverLocation
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.model.trip.TripStatus
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val tripApi: TripApi,
    private val webSocketManager: WebSocketManager
) : TripRepository {

    private val TAG = "TripRepositoryImpl"

    // Tạo scope riêng cho repository này để quản lý coroutine
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Lưu trữ chuyến đi hiện tại
    private val _currentTrip = MutableStateFlow<Trip?>(null)
    override val currentTrip: StateFlow<Trip?> = _currentTrip.asStateFlow()

    init {
        // Lắng nghe các cập nhật từ WebSocket và cập nhật _currentTrip
        repositoryScope.launch {
            webSocketManager.tripUpdatesFlow.collect { tripResponse ->
                _currentTrip.value?.let { currentTrip ->
                    if (tripResponse.tripId == currentTrip.id) {
                        Log.d(TAG, "Cập nhật thông tin chuyến đi hiện tại: ${tripResponse.tripId}, trạng thái: ${tripResponse.status}")
                        _currentTrip.value = mapTripDetailsResponseToTrip(tripResponse)
                    }
                }
            }
        }
    }

    override suspend fun createTrip(createTripRequest: CreateTripRequest): Resource<Trip> {
        return try {
            val response = tripApi.createTrip(createTripRequest)

            if (response.isSuccessful && response.body() != null) {
                val tripResponse = response.body()!!
                val trip = mapTripDetailsResponseToTrip(tripResponse)
                // Lưu chuyến đi hiện tại
                _currentTrip.value = trip
                Resource.Success(trip)
            } else {
                Resource.Error(
                    message = response.errorBody()?.string() ?: "Không thể tạo chuyến đi"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating trip: ${e.message}", e)
            Resource.Error(message = e.message ?: "Đã xảy ra lỗi kết nối")
        }
    }

    override suspend fun getTripDetails(tripId: String): Resource<Trip> {
        Log.e(TAG, "KIỂM TRA: Bắt đầu gọi getTripDetails với tripId: $tripId")
        return try {
            Log.d(TAG, "Đang gọi API lấy chi tiết chuyến đi: $tripId")
            val response = tripApi.getTripDetails(tripId)
            Log.d(TAG, "Kết quả API: ${response.code()}, Thành công: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val tripResponse = response.body()!!
                Log.d(TAG, "Dữ liệu nhận được từ API: $tripResponse")
                try {
                    val trip = mapTripDetailsResponseToTrip(tripResponse)
                    Log.d(TAG, "Mapping thành công: $trip")
                    Resource.Success(trip)
                } catch (e: Exception) {
                    Log.e(TAG, "Lỗi khi mapping dữ liệu: ${e.message}", e)
                    Resource.Error("Lỗi khi xử lý dữ liệu: ${e.message}")
                }
            } else {
                Log.e(TAG, "API trả về lỗi: ${response.code()} - ${response.message()}")
                Resource.Error(
                    message = response.errorBody()?.string() ?: "Không thể lấy thông tin chuyến đi"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception khi gọi API: ${e.message}", e)
            Resource.Error(message = e.message ?: "Đã xảy ra lỗi kết nối")
        }
    }

    override suspend fun updateTripStatus(
        tripId: String,
        updateTripStatusRequest: UpdateTripStatusRequest
    ): Resource<Trip> {
        return try {
            val response = tripApi.updateTripStatus(tripId, updateTripStatusRequest)

            if (response.isSuccessful && response.body() != null) {
                val tripResponse = response.body()!!
                val trip = mapTripDetailsResponseToTrip(tripResponse)

                // Cập nhật chuyến đi hiện tại nếu ID trùng khớp
                _currentTrip.value?.let {
                    if (it.id == trip.id) {
                        _currentTrip.value = trip
                    }
                }

                Resource.Success(trip)
            } else {
                Resource.Error(
                    message = response.errorBody()?.string() ?: "Không thể cập nhật trạng thái chuyến đi"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating trip status: ${e.message}", e)
            Resource.Error(message = e.message ?: "Đã xảy ra lỗi kết nối")
        }
    }

    // Thêm phương thức để xóa chuyến đi hiện tại
    override fun clearCurrentTrip() {
        _currentTrip.value = null
        Log.d(TAG, "Đã xóa chuyến đi hiện tại")
    }

    // Hàm chuyển đổi từ DTO sang domain model
    private fun mapTripDetailsResponseToTrip(response: TripDetailsResponse): Trip {
        return Trip(
            id = response.tripId,
            customerId = response.customerId,
            customerName = response.customerName,
            customerPhone = response.customerPhone,
            driverId = response.driverId,
            driverName = response.driverName,
            driverPhone = response.driverPhone,
            vehicleType = response.vehicleType,
            vehiclePlate = response.vehiclePlate,
            pickupLatitude = response.pickupLatitude,
            pickupLongitude = response.pickupLongitude,
            pickupAddress = response.pickupAddress,
            dropOffLatitude = response.dropoffLatitude,
            dropOffLongitude = response.dropoffLongitude,
            dropOffAddress = response.dropoffAddress,
            status = mapStringToTripStatus(response.status),
            estimatedDistance = response.estimatedDistance,
            estimatedDuration = response.estimatedDuration,
            estimatedFare = response.estimatedFare,
            actualFare = response.actualFare,
            requestedAt = response.createdAt,
            startedAt = response.startedAt,
            completedAt = response.completedAt,
            cancelledAt = response.cancelledAt,
            cancellationReason = response.cancellationReason,
            driverLocation = response.driverLocation?.let {
                DriverLocation(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    heading = it.heading,
                    lastUpdated = it.lastUpdated
                )
            }
        )
    }

    private fun mapStringToTripStatus(status: String): TripStatus {
        return when (status.uppercase()) {
            "PENDING" -> TripStatus.pending
            "ACCEPTED" -> TripStatus.accepted
            "ARRIVED" -> TripStatus.arrived
            "IN_PROGRESS" -> TripStatus.in_progress
            "COMPLETED" -> TripStatus.completed
            "CANCELLED" -> TripStatus.cancelled
            else -> TripStatus.pending
        }
    }
}