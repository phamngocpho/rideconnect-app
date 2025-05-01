package com.rideconnect.domain.usecase.booking

import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.domain.model.vehicle.VehicleType
import javax.inject.Inject

class GetAvailableVehiclesUseCase @Inject constructor() {

    suspend operator fun invoke(
        sourceLocation: Location,
        destinationLocation: Location?,
        filterByType: VehicleType? = null
    ): List<Vehicle> {
        // Mô phỏng dữ liệu phương tiện có sẵn
        val availableVehicles = getMockVehicles()

        // Áp dụng bộ lọc theo loại nếu có
        return if (filterByType != null) {
            availableVehicles.filter { it.type == filterByType }
        } else {
            availableVehicles
        }
    }

    // Tạo dữ liệu mẫu cho phương tiện
    private fun getMockVehicles(): List<Vehicle> {
        return listOf(
            Vehicle(
                id = "car1",
                type = VehicleType.CAR,
                name = "RideConnect Car",
                description = "Xe 4 chỗ tiêu chuẩn, thoải mái và tiết kiệm",
                price = 120000.0,
                estimatedPickupTime = 5,
                capacity = 4
            ),
            Vehicle(
                id = "car_premium1",
                type = VehicleType.CAR_PREMIUM,
                name = "RideConnect Premium",
                description = "Xe 4 chỗ cao cấp, sang trọng và đẳng cấp",
                price = 250000.0,
                estimatedPickupTime = 7,
                capacity = 4
            ),
            Vehicle(
                id = "bike1",
                type = VehicleType.BIKE,
                name = "RideConnect Bike",
                description = "Xe máy nhanh chóng, linh hoạt trong phố",
                price = 50000.0,
                estimatedPickupTime = 3,
                capacity = 1
            ),
            Vehicle(
                id = "bike_plus1",
                type = VehicleType.BIKE_PLUS,
                name = "RideConnect Bike+",
                description = "Xe máy cao cấp, tài xế ưu tú",
                price = 70000.0,
                estimatedPickupTime = 4,
                capacity = 1
            ),
            Vehicle(
                id = "taxi1",
                type = VehicleType.TAXI,
                name = "RideConnect Taxi",
                description = "Dịch vụ taxi truyền thống, an toàn và tin cậy",
                price = 150000.0,
                estimatedPickupTime = 6,
                capacity = 4
            ),
            Vehicle(
                id = "van1",
                type = VehicleType.VAN,
                name = "RideConnect Van",
                description = "Xe 7 chỗ rộng rãi, phù hợp cho nhóm đông",
                price = 200000.0,
                estimatedPickupTime = 10,
                capacity = 7
            )
        )
    }
}
