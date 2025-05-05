package com.rideconnect.presentation.screens.auth.login

gimport androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rideconnect.R
import com.rideconnect.presentation.screens.driver.dashboard.MovelitViewModel
import com.rideconnect.presentation.theme.LightBlack
import com.rideconnect.presentation.theme.LightGreen

@Composable
fun Login(
    onForgotClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovelitViewModel
) {
    // State hoisting - move these to ViewModel in a real app
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        LoginHeader(modifier = Modifier.padding(horizontal = 15.dp))

        Spacer(modifier = Modifier.height(20.dp))

        LoginForm(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            password = password,
            onPasswordChange = { password = it },
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = it },
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        LoginOptions(
            rememberMe = rememberMe,
            onRememberMeChange = { rememberMe = it },
            onForgotClick = onForgotClick
        )

        Spacer(modifier = Modifier.height(50.dp))

        LoginButton(
            onClick = onLoginClick,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        SocialLoginSection(
            onGoogleClick = onGoogleClick,
            onFacebookClick = onFacebookClick,
            onAppleClick = onAppleClick,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        SignUpPrompt(
            onSignUpClick = onSignUpClick,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
        }
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 35.sp,
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
fun LoginForm(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PhoneNumberField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(
                "Phone Number",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = LightBlack
            )
        },
        placeholder = {
            Text(
                "0123456789",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 20.sp
            )
        },
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "+84",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = LightBlack,
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Select country code"
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
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
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(
                "Password",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = LightBlack
            )
        },
        placeholder = {
            Text(
                "Enter Your Password",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 20.sp
            )
        },
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange(!isPasswordVisible) }) {
                Icon(
                    if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    "Toggle password visibility"
                )
            }
        },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
fun LoginOptions(
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    onForgotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val (checkboxRef, textRememberRef, forgotPasswordRef) = createRefs()

        Checkbox(
            checked = rememberMe,
            onCheckedChange = onRememberMeChange,
            modifier = Modifier.constrainAs(checkboxRef) {
                start.linkTo(parent.start)
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
                start.linkTo(checkboxRef.end, margin = 4.dp)
                centerVerticallyTo(checkboxRef)
            }
        )

        TextButton(
            onClick = onForgotClick,
            modifier = Modifier.constrainAs(forgotPasswordRef) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }
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
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGreen
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Text(
            text = "Login",
            fontSize = 23.sp
        )
    }
}

@Composable
fun SocialLoginSection(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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

        Spacer(modifier = Modifier.height(30.dp))

        SocialLoginButtons(
            onGoogleClick = onGoogleClick,
            onFacebookClick = onFacebookClick,
            onAppleClick = onAppleClick
        )
    }
}

@Composable
fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialButton(
            onClick = onAppleClick,
            iconResId = R.drawable.apple,
            contentDescription = "Apple icon"
        )

        Spacer(modifier = Modifier.width(16.dp))

        SocialButton(
            onClick = onGoogleClick,
            iconResId = R.drawable.gg,
            contentDescription = "Google icon",
            iconSize = 24.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        SocialButton(
            onClick = onFacebookClick,
            iconResId = R.drawable.fb,
            contentDescription = "Facebook icon",
            iconSize = 24.dp
        )
    }
}

@Composable
fun SocialButton(
    onClick: () -> Unit,
    iconResId: Int,
    contentDescription: String,
    iconSize: Dp = 50.dp
) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        modifier = Modifier.size(80.dp),
        color = Color.White
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun SignUpPrompt(
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(80.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ")
            TextButton(onClick = onSignUpClick) {
                Text(
                    "Sign Up",
                    color = LightGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

