package com.rideconnect.presentation.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.user.UserRole
import com.rideconnect.domain.usecase.auth.LoginUseCase
import com.rideconnect.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.PhoneNumberChanged -> {
                _state.value = _state.value.copy(
                    phoneNumber = event.value,
                    phoneNumberError = null
                )
            }
            is LoginEvent.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = event.value,
                    passwordError = null
                )
            }
            is LoginEvent.Login -> {
                login()
            }
            is LoginEvent.DismissError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun login() {
        // Validate inputs
        val phoneNumberResult = validatePhoneNumber(_state.value.phoneNumber)
        val passwordResult = validatePassword(_state.value.password)

        val hasError = listOf(phoneNumberResult, passwordResult).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                phoneNumberError = if (!phoneNumberResult.successful) phoneNumberResult.errorMessage else null,
                passwordError = if (!passwordResult.successful) passwordResult.errorMessage else null
            )
            return
        }

        // Proceed with login
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val result = loginUseCase(
                    phoneNumber = _state.value.phoneNumber,
                    password = _state.value.password
                )

                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userRole = result.data?.role
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "Đăng nhập thất bại"
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Idle -> {}
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định"
                )
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Số điện thoại không được để trống"
            )
        }
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Số điện thoại không hợp lệ"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Mật khẩu không được để trống"
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = "Mật khẩu phải có ít nhất 6 ký tự"
            )
        }
        return ValidationResult(successful = true)
    }
}

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val phoneNumberError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userRole: UserRole? = null,
    val error: String? = null
)

sealed class LoginEvent {
    data class PhoneNumberChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data object Login : LoginEvent()
    data object DismissError : LoginEvent()
}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
