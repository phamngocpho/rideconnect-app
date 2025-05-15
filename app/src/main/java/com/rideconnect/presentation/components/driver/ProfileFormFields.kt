package com.example.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideconnect.presentation.components.driver.FormField
import com.rideconnect.presentation.components.driver.FormFieldWithAction
import com.rideconnect.presentation.components.driver.FormFieldWithDropdown
import com.rideconnect.presentation.components.driver.FormFieldWithNavigation

@Composable
fun ProfileFormFields(
    name: String = "Ahmed Aboelhassan",
    phoneNumber: String = "01552882189",
    email: String = "aboelhassan54@gmail.com",
    city: String = "Smouha ,Sidi bishr, Alexandria",
    onChangePhoneClick: () -> Unit = {},
    onCityDropdownClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Name Field
        FormField(label = "Name", value = name)

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Field
        FormFieldWithAction(
            label = "Phone Number",
            value = phoneNumber,
            actionText = "Change",
            onActionClick = onChangePhoneClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        FormField(label = "Email", value = email)

        Spacer(modifier = Modifier.height(16.dp))

        // City Field
        FormFieldWithDropdown(
            label = "City You Drive In",
            value = city,
            onDropdownClick = onCityDropdownClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Documents Field
        FormFieldWithNavigation(
            label = "Documents",
            value = "Update Document Details",
            onNavigateClick = onDocumentsClick
        )
    }
}
