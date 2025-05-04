package com.rideconnect.domain.model.customer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.user.User

data class Customer(
    val id: String,
    val user: User,
    val homeAddress: String?,
    val workAddress: String?,
    val favoriteLocations: List<FavoriteLocation> = emptyList(),
    val defaultPaymentMethod: PaymentMethod? = null
) {
    fun hasDefaultPaymentMethod(): Boolean = defaultPaymentMethod != null

    fun getAddressOptions(): List<AddressOption> {
        val options = mutableListOf<AddressOption>()

        homeAddress?.let {
            options.add(AddressOption("Home", it, Icons.Default.Home))
        }

        workAddress?.let {
            options.add(AddressOption("Work", it, Icons.Default.Work))
        }

        favoriteLocations.forEach {
            options.add(AddressOption(it.name, it.address, Icons.Default.Favorite))
        }

        return options
    }
}

data class FavoriteLocation(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class AddressOption(
    val name: String,
    val address: String,
    val icon: ImageVector
)

// UI State cho Customer Profile Screen
data class CustomerProfileUiState(
    val customer: Customer? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false
)
