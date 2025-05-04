package com.rideconnect.presentation.screens.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R
import com.rideconnect.presentation.theme.*

@Composable
fun Register() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderComponent()
        Spacer(modifier = Modifier.height(12.dp))
        InputFieldsComponent()
        Spacer(modifier = Modifier.height(25.dp))
        SignUpButtonComponent()
        Spacer(modifier = Modifier.height(24.dp))
        SocialLoginComponent()
        Spacer(modifier = Modifier.height(16.dp))
        BottomComponent()
    }
}

@Composable
private fun HeaderComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Register",
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
private fun InputFieldsComponent() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        CustomTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name",
            placeholder = "Enter your FullName",
            leadingIcon = { Icon(Icons.Default.Person, "Name") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            placeholder = "Enter your Email",
            leadingIcon = { Icon(Icons.Default.Email, "Email") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Phone Number",
            placeholder = "0123456789",
            leadingIcon = {
                Text(
                    "+84 -",
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter your Password",
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(Icons.Default.Visibility, "Toggle password visibility")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            placeholder = "Enter your Password again",
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(Icons.Default.Visibility, "Toggle password visibility")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = LightBlack,
            )
        },
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 18.sp
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun SignUpButtonComponent() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "Sign Up",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun SocialLoginComponent() {
    Column {
        DividerWithText()
        Spacer(modifier = Modifier.height(20.dp))
        SocialButtons()
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
            color = Color.Gray
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
    }
}

@Composable
private fun SocialButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialButton(
            icon = R.drawable.apple,
            contentDescription = "Apple icon",
            iconSize = 50.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        SocialButton(
            icon = R.drawable.gg,
            contentDescription = "Google icon",
            iconSize = 24.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        SocialButton(
            icon = R.drawable.fb,
            contentDescription = "Facebook icon",
            iconSize = 24.dp
        )
    }
}

@Composable
private fun SocialButton(
    icon: Int,
    contentDescription: String,
    iconSize: Dp
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
private fun BottomComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Don't have an account? ")
        TextButton(onClick = {}) {
            Text(
                "Sign Up",
                color = LightGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

// Previews for each component
@Preview(showBackground = true)
@Composable
fun HeaderComponentPreview() {
    MaterialTheme {
        HeaderComponent()
    }
}

@Preview(showBackground = true)
@Composable
fun InputFieldsComponentPreview() {
    MaterialTheme {
        InputFieldsComponent()
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpButtonComponentPreview() {
    MaterialTheme {
        SignUpButtonComponent()
    }
}

@Preview(showBackground = true)
@Composable
fun SocialLoginComponentPreview() {
    MaterialTheme {
        SocialLoginComponent()
    }
}

@Preview(showBackground = true)
@Composable
fun BottomComponentPreview() {
    MaterialTheme {
        BottomComponent()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Register()
        }
    }
}
