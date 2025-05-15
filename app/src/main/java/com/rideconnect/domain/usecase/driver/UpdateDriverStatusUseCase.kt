package com.rideconnect.domain.usecase.driver

import android.util.Log
import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
import com.rideconnect.domain.repository.DriverRepository
import com.rideconnect.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class UpdateDriverStatusUseCase @Inject constructor(
    private val driverRepository: DriverRepository
) {
    suspend operator fun invoke(status: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val request = UpdateDriverStatusRequest(isAvailable = status)
            val response = driverRepository.updateDriverStatus(request)

            if (response.isSuccessful) {
                emit(Resource.Success(status))
            } else {
                // Xử lý lỗi HTTP
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    // Cố gắng parse JSON error
                    val jsonError = JSONObject(errorBody ?: "")
                    jsonError.optString("message", "Lỗi không xác định")
                } catch (e: Exception) {
                    // Fallback nếu không parse được JSON
                    when (response.code()) {
                        500 -> "Lỗi máy chủ (500). Vui lòng thử lại sau."
                        401 -> "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại."
                        else -> "Lỗi HTTP ${response.code()}: ${response.message()}"
                    }
                }

                Log.e("UpdateDriverStatusUseCase", "invoke: Cập nhật trạng thái thất bại - HTTP ${response.code()}, message: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: IOException) {
            Log.e("UpdateDriverStatusUseCase", "invoke: Lỗi kết nối", e)
            emit(Resource.Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng."))
        } catch (e: Exception) {
            Log.e("UpdateDriverStatusUseCase", "invoke: Lỗi không xác định", e)
            emit(Resource.Error("Đã xảy ra lỗi: ${e.message ?: "Không xác định"}"))
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
