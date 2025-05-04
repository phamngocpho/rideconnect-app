package com.rideconnect.domain.usecase.auth

import com.rideconnect.domain.model.user.User
import com.rideconnect.domain.repository.AuthRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, password: String): Resource<User> {
        if (phoneNumber.isBlank()) {
            return Resource.Error("Số điện thoại không được để trống")
        }

        if (phoneNumber.length < 9 || phoneNumber.length > 12) {
            return Resource.Error("Số điện thoại không hợp lệ")
        }

        if (password.isBlank()) {
            return Resource.Error("Mật khẩu không được để trống")
        }

        if (password.length < 6) {
            return Resource.Error("Mật khẩu phải có ít nhất 6 ký tự")
        }

        return authRepository.login(phoneNumber, password)
    }
}
