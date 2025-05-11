package com.rideconnect.presentation.screens.auth.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartAppViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Thêm userRole như thuộc tính của lớp
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                Log.d("StartAppViewModel", "Checking login status...")

                // Kiểm tra token có hợp lệ không
                val isTokenValid = authRepository.isTokenValid()
                Log.d("StartAppViewModel", "Token valid: $isTokenValid")

                // Kiểm tra user hiện tại
                val currentUser = authRepository.getCurrentUser().first()
                Log.d("StartAppViewModel", "Current user: ${currentUser?.id}")

                // Lưu loại người dùng để điều hướng đúng màn hình
                _userRole.value = currentUser?.role?.name

                // Nếu có token hợp lệ và user, coi như đã đăng nhập
                _isLoggedIn.value = isTokenValid && currentUser != null
                Log.d("StartAppViewModel", "Is logged in: ${_isLoggedIn.value}, User role: ${_userRole.value}")
            } catch (e: Exception) {
                Log.e("StartAppViewModel", "Error checking login status: ${e.message}")
                _isLoggedIn.value = false
                _userRole.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}