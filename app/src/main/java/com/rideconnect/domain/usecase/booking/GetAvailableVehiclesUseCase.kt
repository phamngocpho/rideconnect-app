package com.rideconnect.domain.usecase.booking

import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.domain.model.vehicle.VehicleType
import com.rideconnect.domain.usecase.direction.GetRouteInfoUseCase
import kotlinx.coroutines.flow.firstOrNull
import java.text.DecimalFormat
import javax.inject.Inject

class GetAvailableVehiclesUseCase @Inject constructor(
    private val getRouteInfoUseCase: GetRouteInfoUseCase
) {

    suspend operator fun invoke(
        sourceLocation: Location,
        destinationLocation: Location?,
        filterByType: VehicleType? = null
    ): List<Vehicle> {
        val availableVehicles = getMockVehiclesBaseInfo() // Lấy thông tin cơ bản của xe

        return availableVehicles.map { vehicle ->
            var calculatedPrice = 0.0
            destinationLocation?.let { dest ->
                val routeInfo = getRouteInfoUseCase(sourceLocation, dest).firstOrNull() // Lấy RouteInfo từ Flow
                if (routeInfo != null) {
                    calculatedPrice = calculatePrice(routeInfo.distance, routeInfo.duration, vehicle.type)
                }
            }
            vehicle.copy(price = calculatedPrice) // Cập nhật giá đã tính toán
        }.filter {
            filterByType?.let { type -> it.type == type } ?: true
        }
    }

    // Công thức tính giá (dựa trên khoảng cách và loại xe)
    private fun calculatePrice(distance: Int, duration: Int, vehicleType: VehicleType): Double {
        val distanceInKm = distance / 1000.0
        val durationInMinutes = duration / 60.0

        val baseFareCar = 5000.0
        val pricePerKmCar = 4000.0
        val pricePerMinuteCar = 1000.0

        val baseFareBike = 2500.0
        val pricePerKmBike = 2000.0
        val pricePerMinuteBike = 500.0

        var totalPrice = when (vehicleType) {
            VehicleType.car, VehicleType.car_premium, VehicleType.taxi ->
                baseFareCar + (distanceInKm * pricePerKmCar) + (durationInMinutes * pricePerMinuteCar)
            VehicleType.bike, VehicleType.bike_plus ->
                baseFareBike + (distanceInKm * pricePerKmBike) + (durationInMinutes * pricePerMinuteBike)
            VehicleType.van ->
                baseFareCar * 1.5 + (distanceInKm * pricePerKmCar * 1.2) + (durationInMinutes * pricePerMinuteCar * 1.2)
        }

        val minFare = 20000.0
        totalPrice = maxOf(totalPrice, minFare)

        val df = DecimalFormat("#.###")
        return df.format(totalPrice).toDouble()
    }

    private fun getMockVehiclesBaseInfo(): List<Vehicle> {
        return listOf(
            Vehicle(
                id = "car1",
                type = VehicleType.car,
                name = "RideConnect Car",
                description = "Xe 4 chỗ tiêu chuẩn, thoải mái và tiết kiệm",
                price = 0.0,
                estimatedPickupTime = 5,
                capacity = 4),
            Vehicle(id = "car_premium1", type = VehicleType.car_premium, name = "RideConnect Premium", description = "Xe 4 chỗ cao cấp, sang trọng và đẳng cấp", price = 0.0, estimatedPickupTime = 7, capacity = 4),
            Vehicle(id = "bike1", type = VehicleType.bike, name = "RideConnect Bike", description = "Xe máy nhanh chóng, linh hoạt trong phố", price = 0.0, estimatedPickupTime = 3, capacity = 1),
            Vehicle(id = "bike_plus1", type = VehicleType.bike_plus, name = "RideConnect Bike+", description = "Xe máy cao cấp, tài xế ưu tú", price = 0.0, estimatedPickupTime = 4, capacity = 1),
            Vehicle(id = "taxi1", type = VehicleType.taxi, name = "RideConnect Taxi", description = "Dịch vụ taxi truyền thống, an toàn và tin cậy", price = 0.0, estimatedPickupTime = 6, capacity = 4),
            Vehicle(id = "van1", type = VehicleType.van, name = "RideConnect Van", description = "Xe 7 chỗ rộng rãi, phù hợp cho nhóm đông", price = 0.0, estimatedPickupTime = 10, capacity = 7)
        )
    }
}
