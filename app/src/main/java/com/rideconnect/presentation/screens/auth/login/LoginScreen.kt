package com.rideconnect.presentation.screens.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.domain.model.user.UserRole

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToCustomerDashboard: () -> Unit,
    onNavigateToDriverDashboard: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đăng nhập",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Phone number field
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.onEvent(LoginEvent.PhoneNumberChanged(it)) },
                label = { Text("Số điện thoại") },
                isError = state.phoneNumberError != null,
                supportingText = {
                    state.phoneNumberError?.let {
                        Text(text = it)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text("Mật khẩu") },
                isError = state.passwordError != null,
                supportingText = {
                    state.passwordError?.let {
                        Text(text = it)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.onEvent(LoginEvent.Login) }
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // "Forgot Password" text
            Text(
                text = "Quên mật khẩu?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* Handle forgot password */ }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = { viewModel.onEvent(LoginEvent.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Đăng nhập")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chưa có tài khoản? ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Đăng ký",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }

    // Handle navigation on successful login
    LaunchedEffect(state.isLoggedIn, state.userRole) {
        if (state.isLoggedIn) {
            when (state.userRole) {
                UserRole.DRIVER -> onNavigateToDriverDashboard()
                UserRole.CUSTOMER -> onNavigateToCustomerDashboard()
//                UserRole.ADMIN -> onNavigateToCustomerDashboard()
                else -> onNavigateToCustomerDashboard() // Mặc định
            }
        }
    }

    // Show error dialog if needed
    if (state.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(LoginEvent.DismissError) },
            title = { Text("Lỗi") },
            text = { Text(state.error ?: "Đã xảy ra lỗi, vui lòng thử lại") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(LoginEvent.DismissError) }) {
                    Text("Đóng")
                }
            }
        )
    }
}
