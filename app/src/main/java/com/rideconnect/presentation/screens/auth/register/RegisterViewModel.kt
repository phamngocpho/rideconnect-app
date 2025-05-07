package com.rideconnect.presentation.screens.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.domain.model.user.User
import com.rideconnect.domain.usecase.auth.RegisterUseCase
import com.rideconnect.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FullNameChanged -> {
                _state.value = _state.value.copy(
                    fullName = event.value,
                    fullNameError = null
                )
            }
            is RegisterEvent.PhoneNumberChanged -> {
                _state.value = _state.value.copy(
                    phoneNumber = event.value,
                    phoneNumberError = null
                )
            }
            is RegisterEvent.EmailChanged -> {
                _state.value = _state.value.copy(
                    email = event.value,
                    emailError = null
                )
            }
            is RegisterEvent.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = event.value,
                    passwordError = null
                )
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _state.value = _state.value.copy(
                    confirmPassword = event.value,
                    confirmPasswordError = null
                )
            }
            is RegisterEvent.Register -> {
                register()
            }
            is RegisterEvent.DismissError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun register() {
        val fullNameResult = validateFullName(_state.value.fullName)
        val phoneNumberResult = validatePhoneNumber(_state.value.phoneNumber)
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(_state.value.password)
        val confirmPasswordResult = validateConfirmPassword(
            _state.value.password,
            _state.value.confirmPassword
        )

        val hasError = listOf(
            fullNameResult,
            phoneNumberResult,
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                fullNameError = if (!fullNameResult.successful) fullNameResult.errorMessage else null,
                phoneNumberError = if (!phoneNumberResult.successful) phoneNumberResult.errorMessage else null,
                emailError = if (!emailResult.successful) emailResult.errorMessage else null,
                passwordError = if (!passwordResult.successful) passwordResult.errorMessage else null,
                confirmPasswordError = if (!confirmPasswordResult.successful) confirmPasswordResult.errorMessage else null
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val result = registerUseCase(
                    fullName = _state.value.fullName,
                    phoneNumber = _state.value.phoneNumber,
                    email = _state.value.email,
                    password = _state.value.password
                )

                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isRegistered = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "Đăng ký thất bại"
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định"
                )
            }
        }
    }

    private fun validateFullName(fullName: String): ValidationResult {
        if (fullName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Họ tên không được để trống"
            )
        }
        return ValidationResult(successful = true)
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

    private fun validateEmail(email: String): ValidationResult {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email không hợp lệ"
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

    private fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Vui lòng xác nhận mật khẩu"
            )
        }
        if (password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Mật khẩu xác nhận không khớp"
            )
        }
        return ValidationResult(successful = true)
    }
}

data class RegisterState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullNameError: String? = null,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val error: String? = null
)

sealed class RegisterEvent {
    data class FullNameChanged(val value: String) : RegisterEvent()
    data class PhoneNumberChanged(val value: String) : RegisterEvent()
    data class EmailChanged(val value: String) : RegisterEvent()
    data class PasswordChanged(val value: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent()
    data object Register : RegisterEvent()
    data object DismissError : RegisterEvent()
}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
