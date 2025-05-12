package com.rideconnect.data.repository

import com.rideconnect.data.local.dao.UserDao
import com.rideconnect.data.local.entity.UserEntity
import com.rideconnect.data.remote.api.AuthApi
import com.rideconnect.data.remote.auth.AuthTokenProvider
import com.rideconnect.data.remote.dto.request.auth.LoginRequest
import com.rideconnect.data.remote.dto.request.auth.RegisterRequest
import com.rideconnect.domain.model.user.User
import com.rideconnect.domain.model.user.UserRole
import com.rideconnect.domain.repository.AuthRepository
import com.rideconnect.util.Resource
import com.rideconnect.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val tokenProvider: AuthTokenProvider
) : AuthRepository {
    // Tạo scope riêng cho repository này để quản lý coroutine
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun login(phoneNumber: String, password: String): Resource<User> {
        return try {
            val request = LoginRequest(phoneNumber = phoneNumber, password = password)
            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val userRole = loginResponse.userType ?: "CUSTOMER"
                val userEntity = UserEntity(
                    id = loginResponse.userId,
                    phoneNumber = phoneNumber,
                    email = null,
                    fullName = loginResponse.fullName ?: "",
                    avatarUrl = null,
                    userType = userRole,
                    authToken = loginResponse.token,
                    refreshToken = "",
                    tokenExpiryDate = if (loginResponse.expiresAt != null)
                        ZonedDateTime.parse(loginResponse.expiresAt)
                    else
                        ZonedDateTime.now().plusDays(1),
                    lastSyncTime = ZonedDateTime.now()
                )


                userDao.insertUser(userEntity)

                // Lưu token vào AuthTokenProvider
                tokenProvider.setToken(loginResponse.token, loginResponse.expiresAt)

                // Đặt lại trạng thái phiên khi đăng nhập thành công
                SessionManager.resetSessionExpired()

                val user = User(
                    id = userEntity.id,
                    email = userEntity.email ?: "",
                    fullName = userEntity.fullName,
                    phoneNumber = userEntity.phoneNumber,
                    profilePicture = userEntity.avatarUrl,
                    role = UserRole.fromString(userEntity.userType),
                    isActive = true,
                    createdAt = userEntity.lastSyncTime,
                    updatedAt = null
                )

                Resource.Success(user)
            } else {
                Resource.Error(
                    message = response.errorBody()?.string() ?: "Đăng nhập thất bại"
                )
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Đã xảy ra lỗi kết nối")
        }
    }

    override suspend fun register(
        fullName: String,
        phoneNumber: String,
        email: String?,
        password: String
    ): Resource<User> {
        return try {
            val request = RegisterRequest(
                fullName = fullName,
                phoneNumber = phoneNumber,
                email = email,
                password = password
            )

            val response = authApi.register(request)

            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!

                val user = User(
                    id = registerResponse.userId,
                    email = email ?: "",
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    profilePicture = null,
                    role = UserRole.CUSTOMER,
                    isActive = true,
                    createdAt = ZonedDateTime.now(),
                    updatedAt = null
                )

                Resource.Success(user)
            } else {
                Resource.Error(
                    message = response.errorBody()?.string() ?: "Đăng ký thất bại"
                )
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Đã xảy ra lỗi kết nối")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            tokenProvider.clearToken()

            // Xóa user khỏi database
            userDao.clearUserData()

            // Đặt lại trạng thái phiên
            SessionManager.setSessionExpired(true)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Đăng xuất thất bại")
        }
    }

    override suspend fun getAuthToken(): Flow<String> {
        // Sử dụng tokenProvider để lấy token thay vì truy vấn từ UserDao
        return tokenProvider.getTokenFlow().map { it ?: "" }
    }

    override suspend fun isTokenValid(): Boolean {
        return tokenProvider.isTokenValid()
    }
    override fun getCurrentUser(): Flow<User?> {
        return userDao.getCurrentUser().map { userEntity ->
            userEntity?.let {
                // Kiểm tra token hết hạn khi lấy thông tin người dùng
                if (isTokenExpiredSync(it)) {
                    // Xử lý đăng xuất trong background
                    handleTokenExpiration()
                    null
                } else {
                    User(
                        id = it.id,
                        email = it.email ?: "",
                        fullName = it.fullName,
                        phoneNumber = it.phoneNumber,
                        profilePicture = it.avatarUrl,
                        role = UserRole.fromString(it.userType),
                        isActive = true,
                        createdAt = it.lastSyncTime,
                        updatedAt = null
                    )
                }
            }
        }
    }
    override suspend fun refreshToken(): Resource<String> {
        // Vì không sử dụng refresh token, chỉ trả về lỗi
        return Resource.Error("Refresh token không được hỗ trợ")
    }

    override fun getUserId(): Flow<String> {
        return userDao.getCurrentUser().map { userEntity ->
            userEntity?.id ?: ""
        }
    }

    // Hàm kiểm tra token hết hạn (không suspend)
    private fun isTokenExpiredSync(userEntity: UserEntity): Boolean {
        return ZonedDateTime.now().isAfter(userEntity.tokenExpiryDate)
    }

    // Hàm xử lý khi token hết hạn
    private fun handleTokenExpiration() {
        // Sử dụng scope riêng thay vì GlobalScope
        repositoryScope.launch {
            userDao.clearUserData()
            // Xóa token trong AuthTokenProvider
            tokenProvider.clearToken()
            SessionManager.setSessionExpired(true)
        }
    }
}
