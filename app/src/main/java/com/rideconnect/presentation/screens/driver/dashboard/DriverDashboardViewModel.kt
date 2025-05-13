package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.usecase.driver.UpdateDriverStatusUseCase
import com.rideconnect.service.LocationUpdateService
import com.rideconnect.util.Resource
import com.rideconnect.util.preferences.PreferenceKeys
import com.rideconnect.util.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDashboardViewModel @Inject constructor(
    private val updateDriverStatusUseCase: UpdateDriverStatusUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Thêm state để lưu trạng thái đang cố gắng chuyển sang
    private val _pendingStatus = MutableStateFlow<String?>(null)
    val pendingStatus: StateFlow<String?> = _pendingStatus.asStateFlow()

    init {
        // Khởi tạo trạng thái từ local storage
        viewModelScope.launch {
            preferenceManager.getPreference(PreferenceKeys.DRIVER_ONLINE_STATUS, false).collect { status ->
                _isOnline.value = status
            }
        }
    }

    fun toggleOnlineStatus(context: Context, online: Boolean) {
        if (_isLoading.value) return // Tránh gọi nhiều lần khi đang loading

        // Chuyển đổi Boolean thành String status
        val status = if (online) "online" else "offline"

        viewModelScope.launch {
            _isLoading.value = true
            _pendingStatus.value = status // Lưu trạng thái đang cố gắng chuyển sang

            updateDriverStatusUseCase(status).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _isOnline.value = online
                        preferenceManager.setPreference(PreferenceKeys.DRIVER_ONLINE_STATUS, online)

                        if (online) {
                            val serviceIntent = Intent(context, LocationUpdateService::class.java)
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.stopService(Intent(context, LocationUpdateService::class.java))
                        }
                        _isLoading.value = false
                        _pendingStatus.value = null // Xóa trạng thái đang chờ
                    }
                    is Resource.Error -> {
                        // Xử lý message lỗi trống
                        val errorMsg = result.message?.takeIf { it.isNotBlank() }
                            ?: "Không thể cập nhật trạng thái. Lỗi máy chủ (500)."

                        _errorMessage.value = errorMsg
                        Log.e("DriverDashboardVM", "Lỗi cập nhật trạng thái: $errorMsg")
                        _isLoading.value = false
                        // Không xóa _pendingStatus để có thể hiển thị nút thử lại
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }

    // Thêm hàm thử lại
    fun retryStatusUpdate(context: Context) {
        _pendingStatus.value?.let { status ->
            val online = status == "online"
            toggleOnlineStatus(context, online)
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Hàm hủy thao tác đang chờ
    fun cancelPendingStatusUpdate() {
        _pendingStatus.value = null
        _isLoading.value = false
    }
}
