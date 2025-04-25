package com.rideconnect.presentation.screens.auth.forgetpassword


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import com.rideconnect.presentation.theme.*



@Composable
fun ForgotPasswordSceen(
    onBackClick: () -> Unit,
    onSendCodeClick: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 50.dp)
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack
                ,contentDescription = "Back",
                modifier = Modifier.size(50.dp))
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Forgot password?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "It happens! Just confirm your phone number and we'll send you a code to create a new password.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    color = LightBlack,
                    fontWeight = FontWeight.Bold
                ) },
                placeholder = {
                    Text(
                        "0123456789",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "+84", style =MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
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

                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSendCodeClick(phoneNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen
                )
            ) {
                Text(
                    text = "Send the Code",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ForgotPasswordSceen(
                onBackClick = { },
                onSendCodeClick = { }
            )
        }
    }
}
