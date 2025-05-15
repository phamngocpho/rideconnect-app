package com.rideconnect.presentation.screens.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.R
import com.rideconnect.domain.model.user.UserRole
import com.rideconnect.presentation.ui.theme.LightBlack
import com.rideconnect.presentation.ui.theme.LightGreen
import androidx.constraintlayout.compose.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToCustomerDashboard: () -> Unit,
    onNavigateToDriverDashboard: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LoginContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToRegister = onNavigateToRegister
    )

    // Handle navigation on successful login
    LaunchedEffect(state.isLoggedIn, state.userRole) {
        if (state.isLoggedIn) {
            when (state.userRole) {
                UserRole.DRIVER -> onNavigateToDriverDashboard()
                UserRole.CUSTOMER -> onNavigateToCustomerDashboard()
                else -> onNavigateToCustomerDashboard()
            }
        }
    }

    // Show error dialog if needed
    if (state.error != null) {
        ErrorDialog(
            error = state.error,
            onDismiss = { viewModel.onEvent(LoginEvent.DismissError) }
        )
    }
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(32.dp))

            LoginForm(
                state = state,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(5.dp))
            ForgotPasswordText(onForgotClick = {})

            Spacer(modifier = Modifier.height(80.dp))

            LoginButton(
                isLoading = state.isLoading,
                onLogin = { onEvent(LoginEvent.Login) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "Or Sign up With",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )
                Divider(modifier = Modifier.weight(1f))
            }


            Spacer(modifier = Modifier.height(16.dp))
            SocialButton(
                onGoogleClick = {  },
                onFacebookClick ={}
            ) { }
            Spacer(modifier = Modifier.height(90.dp))
            RegisterPrompt(onNavigateToRegister = onNavigateToRegister)
        }
    }
}
@Composable
private fun SocialButton(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            modifier = Modifier.size(80.dp),
            color = Color.White
        ) {
            IconButton(
                onClick = onAppleClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.apple),
                    contentDescription = "Apple icon",
                    modifier = Modifier
                        .size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))


        Surface(
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            modifier = Modifier.size(80.dp),
            color = Color.White
        ) {
            IconButton(
                onClick = onGoogleClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gg),
                    contentDescription = "Google icon",
                    modifier = Modifier
                        .size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))


        Surface(
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            modifier = Modifier.size(80.dp),
            color = Color.White
        ) {
            IconButton(
                onClick = onGoogleClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fb),
                    contentDescription = "Google icon",
                    modifier = Modifier
                        .size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun LoginHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 40.dp)
                .weight(1f)
                .offset(x = (-37).dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginForm(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Phone number field
        PhoneNumberField(
            phoneNumber = state.phoneNumber,
            error = state.phoneNumberError,
            onPhoneNumberChange = { onEvent(LoginEvent.PhoneNumberChanged(it)) }
        )

        // Password field
        PasswordField(
            password = state.password,
            error = state.passwordError,
            onPasswordChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            onDone = { onEvent(LoginEvent.Login) }
        )

        // Forgot Password

    }
}

@Composable
private fun PhoneNumberField(
    phoneNumber: String,
    error: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    OutlinedTextField(
        value = phoneNumber,
        onValueChange = onPhoneNumberChange,
        label = { Text("Phone Number",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = LightBlack,
        ) },
        placeholder = {
            Text(
                "0123456789",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 20.sp
            )
        },
        isError = error != null,
        supportingText = { error?.let { Text(text = it) } },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "+84", style =MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                        color = LightBlack,
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select country code"
                    )
                }
            },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
            focusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = Color.Black,
            cursorColor = Color.Black,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.Black,
            disabledLabelColor = Color.Black,
            disabledLeadingIconColor = Color.Black
        )
    )
}

@Composable
private fun PasswordField(
    password: String,
    error: String?,
    onPasswordChange: (String) -> Unit,
    onDone: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = LightBlack,
        ) },
        placeholder = {
            Text(
                "Enter Your Password",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 18.sp
            )
        },
        isError = error != null,
        supportingText = { error?.let { Text(text = it) } },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
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
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
            focusedPlaceholderColor = Color.Black.copy(alpha = 0.5f),
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = Color.Black,
            cursorColor = Color.Black,
            unfocusedLeadingIconColor = Color.Black,
            focusedLeadingIconColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.Black,
            disabledLabelColor = Color.Black,
            disabledLeadingIconColor = Color.Black
        )

    )
}

@Composable
private fun ForgotPasswordText(
    onForgotClick: () -> Unit
) {
    var rememberMe by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Tạo references cho các component
        val (checkboxRef, textRememberRef, forgotPasswordRef) = createRefs()
        Checkbox(
            checked = rememberMe,
            onCheckedChange = { rememberMe = it },
            modifier = Modifier.constrainAs(checkboxRef) {
                start.linkTo(parent.start,  margin = (-12).dp)
                centerVerticallyTo(parent)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = LightGreen,
                checkmarkColor = Color.White
            )
        )

        Text(
            text = "Keep me logged in",
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(textRememberRef) {
                start.linkTo(checkboxRef.end, margin = (-4).dp)
                centerVerticallyTo(checkboxRef)
            }
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextButton(
            onClick = onForgotClick,
            modifier = Modifier.constrainAs(forgotPasswordRef) {
                end.linkTo(parent.end, margin = (-3).dp)
                centerVerticallyTo(parent)
            },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Forgot Password?",
                color = LightGreen,
                fontSize = 18.sp,
            )
        }
    }
}


@Composable
private fun LoginButton(
    isLoading: Boolean,
    onLogin: () -> Unit
) {
    Button(
        onClick = onLogin,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGreen
        ),
        shape = RoundedCornerShape(25.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text = "Đăng nhập")
        }
    }
}

@Composable
private fun RegisterPrompt(
    onNavigateToRegister: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account? ",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Sign Up",
            color = LightGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.clickable(onClick = onNavigateToRegister)
        )
    }
}

@Composable
private fun ErrorDialog(
    error: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Lỗi") },
        text = { Text(error ?: "Đã xảy ra lỗi, vui lòng thử lại") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

// Previews
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginState(
                phoneNumber = "0123456789",
                password = "password"
            ),
            onEvent = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginState(
                phoneNumber = "abc",
                phoneNumberError = "Số điện thoại không hợp lệ",
                password = "123",
                passwordError = "Mật khẩu phải có ít nhất 6 ký tự"
            ),
            onEvent = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginState(isLoading = true),
            onEvent = {},
            onNavigateToRegister = {}
        )
    }
}
