package com.rideconnect.presentation.screens.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.R
import com.rideconnect.presentation.ui.theme.*

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    RegisterContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin
    )

    if (state.error != null) {
        ErrorDialog(
            error = state.error,
            onDismiss = { viewModel.onEvent(RegisterEvent.DismissError) }
        )
    }

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onRegisterSuccess()
        }
    }
}

@Composable
private fun RegisterContent(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            RegisterHeader()

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Form Section
                RegisterForm(state = state, onEvent = onEvent)

                // Button and Social Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    RegisterButton(
                        isLoading = state.isLoading,
                        onRegister = { onEvent(RegisterEvent.Register) }
                    )

                    DividerWithText()

                    SocialButton()
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom Section
                LoginPrompt(onNavigateToLogin = onNavigateToLogin)
            }
        }
    }
}

@Composable
private fun RegisterHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(end = 48.dp), // Compensate for the back button
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegisterForm(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomTextField(
            value = state.fullName,
            onValueChange = { onEvent(RegisterEvent.FullNameChanged(it)) },
            label = "Full Name",
            placeholder = "Enter your FullName",
            isError = state.fullNameError != null,
            errorMessage = state.fullNameError,
            leadingIcon = { Icon(Icons.Default.Person, "Person") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        CustomTextField(
            value = state.phoneNumber,
            onValueChange = { onEvent(RegisterEvent.PhoneNumberChanged(it)) },
            label = "Phone Number",
            placeholder = "0123456789",
            isError = state.phoneNumberError != null,
            errorMessage = state.phoneNumberError,
            leadingIcon = { Icon(Icons.Default.Phone, "Phone") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        CustomTextField(
            value = state.email,
            onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
            label = "Email Address",
            placeholder = "Enter your Email",
            isError = state.emailError != null,
            errorMessage = state.emailError,
            leadingIcon = { Icon(Icons.Default.Email, "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        PasswordFields(state = state, onEvent = onEvent)
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LightBlack
            )
        },
        isError = isError,
        supportingText = {
            errorMessage?.let { Text(text = it) }
        },
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 18.sp
            )
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation
    )
}

@Composable
private fun PasswordFields(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = state.password,
            onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "Enter your Password",
            isError = state.passwordError != null,
            errorMessage = state.passwordError,
            leadingIcon = { Icon(Icons.Default.Lock, "Password") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        CustomTextField(
            value = state.confirmPassword,
            onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
            label = "Confirm Password",
            placeholder = "Enter your Password again",
            isError = state.confirmPasswordError != null,
            errorMessage = state.confirmPasswordError,
            leadingIcon = { Icon(Icons.Default.Lock, "Confirm Password") },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onEvent(RegisterEvent.Register) }
            )
        )
    }
}

@Composable
private fun RegisterButton(
    isLoading: Boolean,
    onRegister: () -> Unit
) {
    Button(
        onClick = onRegister,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(20.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Register",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DividerWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
        Text(
            text = "Or Sign up with",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
    }
}

@Composable
private fun SocialButton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialIconButton(
            icon = R.drawable.apple,
            contentDescription = "Apple icon",
            iconSize = 50.dp
        )

        SocialIconButton(
            icon = R.drawable.gg,
            contentDescription = "Google icon",
            iconSize = 24.dp
        )

        SocialIconButton(
            icon = R.drawable.fb,
            contentDescription = "Facebook icon",
            iconSize = 24.dp
        )
    }
}

@Composable
private fun SocialIconButton(
    icon: Int,
    contentDescription: String,
    iconSize: androidx.compose.ui.unit.Dp
) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        modifier = Modifier.size(80.dp),
        color = Color.White
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun LoginPrompt(onNavigateToLogin: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account? ",
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "Sign In",
                color = LightGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun ErrorDialog(
    error: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(error ?: "An error occurred. Please try again.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
// Previews
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val previewState = RegisterState(
        fullName = "Nguyễn Văn A",
        email = "example@email.com",
        phoneNumber = "0123456789",
        password = "password",
        confirmPassword = "password"
    )

    MaterialTheme {
        RegisterContent(
            state = previewState,
            onEvent = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenErrorPreview() {
    val previewState = RegisterState(
        fullName = "Nguyễn Văn A",
        fullNameError = "Tên không hợp lệ",
        email = "invalid-email",
        emailError = "Email không hợp lệ",
        phoneNumber = "abc",
        phoneNumberError = "Số điện thoại không hợp lệ",
        password = "123",
        passwordError = "Mật khẩu phải có ít nhất 6 ký tự",
        confirmPassword = "456",
        confirmPasswordError = "Mật khẩu không khớp"
    )

    MaterialTheme {
        RegisterContent(
            state = previewState,
            onEvent = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenLoadingPreview() {
    val previewState = RegisterState(
        isLoading = true
    )

    MaterialTheme {
        RegisterContent(
            state = previewState,
            onEvent = {},
            onNavigateToLogin = {}
        )
    }
}
