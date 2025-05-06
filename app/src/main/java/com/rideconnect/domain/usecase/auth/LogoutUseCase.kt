package com.rideconnect.domain.usecase.auth

import com.rideconnect.domain.repository.AuthRepository
import com.rideconnect.util.Resource
import jakarta.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.logout()
    }
}