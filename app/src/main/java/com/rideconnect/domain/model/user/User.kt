package com.rideconnect.domain.model.user

import java.time.ZonedDateTime

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val profilePicture: String?,
    val role: UserRole,
    val isActive: Boolean,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime?
) {
    fun getInitials(): String {
        return fullName.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().toString() }
            .uppercase()
    }

    fun getDisplayName(): String {
        return fullName.ifEmpty { "User-${id.take(5)}" }
    }

    fun getFormattedPhoneNumber(): String {
        return if (phoneNumber.startsWith("+84")) {
            "0${phoneNumber.substring(3)}"
        } else {
            phoneNumber
        }
    }

    fun isCustomer(): Boolean = role == UserRole.CUSTOMER
    fun isDriver(): Boolean = role == UserRole.DRIVER
    fun isAdmin(): Boolean = role == UserRole.ADMIN
}

enum class UserRole {
    CUSTOMER, DRIVER, ADMIN, UNKNOWN;

    companion object {
        fun fromString(value: String?): UserRole {
            return when (value?.uppercase()) {
                "CUSTOMER" -> CUSTOMER
                "DRIVER" -> DRIVER
                "ADMIN" -> ADMIN
                else -> UNKNOWN
            }
        }
    }
}

// UI State cho User Screen
data class UserUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
