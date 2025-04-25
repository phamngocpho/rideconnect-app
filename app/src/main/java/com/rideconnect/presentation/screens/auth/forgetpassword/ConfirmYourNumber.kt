package com.rideconnect.presentation.screens.auth.forgetpassword


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.presentation.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOTP(
    phoneNumber: String,
    onBackClick: () -> Unit,
    onChangeClick: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    var otpValues by remember { mutableStateOf(List(5) { "" }) }
    var currentFocusIndex by remember { mutableStateOf(0) }
    var remainingTime by remember { mutableStateOf(49) }

    // Tạo list các focus requesters cho từng ô
    val focusRequesters = remember {
        List(5) { FocusRequester() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Confirm Your number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Please enter the code we've sent to your phone number $phoneNumber",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                TextButton(
                    onClick = onChangeClick
                ) {
                    Text(
                        text = "Change",
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                otpValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && (newValue.isEmpty() || newValue[0].isDigit())) {
                                val newOtpValues = otpValues.toMutableList()
                                newOtpValues[index] = newValue
                                otpValues = newOtpValues

                                if (newValue.isNotEmpty() && index < 4) {
                                    currentFocusIndex = index + 1
                                    focusRequesters[index + 1].requestFocus()
                                }
                                else if (newValue.isEmpty() && index > 0) {
                                    currentFocusIndex = index - 1
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .aspectRatio(1f)
                            .focusRequester(focusRequesters[index]),
                        singleLine = true,
                        enabled = true,
                        shape = RoundedCornerShape(8.dp),
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Center
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = if (value.isNotEmpty())
                                Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.5f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Resend Code in ${String.format("%02d:%02d", remainingTime / 60, remainingTime % 60)}",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onConfirmClick(otpValues.joinToString(""))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    disabledContainerColor = LightGreen.copy(alpha = 0.5f)
                ),
                enabled = otpValues.all { it.isNotEmpty() }
            ) {
                Text(
                    text = "Confirm",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OtpVerificationScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConfirmOTP(
                phoneNumber = "01013554335",
                onBackClick = { },
                onChangeClick = { },
                onConfirmClick = { }
            )
        }
    }
}
