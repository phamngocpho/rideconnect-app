package com.rideconnect.presentation.screens.driver.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.rideconnect.presentation.components.buttons.DriverAppBar
import com.rideconnect.presentation.components.buttons.DriverPrimaryButton
import com.rideconnect.presentation.components.buttons.DriverSecondaryButton
import com.rideconnect.presentation.components.buttons.StepProgressBar
import com.rideconnect.presentation.components.inputs.DriverPhoneTextField
import com.rideconnect.presentation.components.inputs.DriverTextField
import com.rideconnect.presentation.components.inputs.GenderSelectionField

@Composable
fun PersonalDetailsScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: MovelitViewModel
) {
    // Xử lý nút Back của hệ thống
    BackHandler {
        onBackClick()
    }

    // State để hiển thị thông báo lỗi
    val showError = remember { mutableStateOf(false) }

    // State để hiển thị/ẩn mật khẩu
    var passwordVisible by remember { mutableStateOf(false) }

    // Scroll state để cho phép cuộn khi có nhiều trường nhập liệu
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DriverAppBar(
            title = "Movelit",
            onBackClick = onBackClick
        )

        StepProgressBar(
            numberOfSteps = 4,
            currentStep = 1
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Thông tin cá nhân",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            DriverTextField(
                value = viewModel.name,
                onValueChange = {
                    viewModel.name = it
                    showError.value = false
                },
                label = "Họ và tên",
                placeholder = "Nhập họ và tên của bạn",
                isError = showError.value && viewModel.name.isEmpty()
            )

            DriverTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.email = it
                    showError.value = false
                },
                label = "Email",
                placeholder = "Nhập email của bạn",
                isError = showError.value && viewModel.email.isEmpty()
            )

            DriverPhoneTextField(
                value = viewModel.phoneNumber,
                onValueChange = {
                    viewModel.phoneNumber = it
                    showError.value = false
                },
                isError = showError.value && viewModel.phoneNumber.isEmpty()
            )

            // Thêm trường mật khẩu mới
            DriverTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.password = it
                    showError.value = false
                },
                label = "Mật khẩu",
                placeholder = "Nhập mật khẩu của bạn",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu"
                        )
                    }
                },
                isError = showError.value && viewModel.password.isEmpty()
            )

            GenderSelectionField(
                value = viewModel.gender,
                onValueChange = {
                    viewModel.gender = it
                    showError.value = false
                },
                isError = showError.value && viewModel.gender.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            DriverTextField(
                value = viewModel.city,
                onValueChange = {
                    viewModel.city = it
                    showError.value = false
                },
                label = "Thành phố",
                placeholder = "",
                trailingIcon = { Icon(Icons.Default.Check, "Thành phố đã chọn", tint = Color(0xFF4CAF50)) },
                isError = showError.value && viewModel.city.isEmpty()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = viewModel.termsAccepted,
                    onCheckedChange = {
                        viewModel.termsAccepted = it
                        showError.value = false
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )

                Text(
                    text = "Bằng cách chấp nhận, bạn đồng ý với ",
                    fontSize = 14.sp
                )

                Text(
                    text = "Điều khoản & Điều kiện",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.clickable { /* Mở điều khoản và điều kiện */ }
                )
            }

            // Hiển thị thông báo lỗi nếu cần
            if (showError.value) {
                Text(
                    text = "Vui lòng điền đầy đủ thông tin",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Nút Back và Next
        Row(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            DriverSecondaryButton(
                text = "Quay lại",
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            DriverPrimaryButton(
                text = "Tiếp theo",
                onClick = {
                    if (validateInputs(viewModel)) {
                        onNextClick()
                    } else {
                        showError.value = true
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
    }
}

private fun validateInputs(viewModel: MovelitViewModel): Boolean {
    return viewModel.name.isNotEmpty() &&
            viewModel.email.isNotEmpty() &&
            viewModel.phoneNumber.isNotEmpty() &&
            viewModel.password.isNotEmpty() &&
            viewModel.gender.isNotEmpty() &&
            viewModel.city.isNotEmpty() &&
            viewModel.termsAccepted
}
