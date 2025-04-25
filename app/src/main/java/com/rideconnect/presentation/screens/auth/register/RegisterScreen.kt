package com.rideconnect.presentation.screens.auth.register



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full Name",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LightBlack,
                fontWeight = FontWeight.Bold
            ) },
            placeholder = {
                Text(
                    "Enter your FullName",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            },
            leadingIcon = { Icon(Icons.Default.Person, "Name") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email Address",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LightBlack,
                fontWeight = FontWeight.Bold
            ) },
            placeholder = {
                Text(
                    "Enter your Email",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            },
            leadingIcon = { Icon(Icons.Default.Email, "Email") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone Number",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = LightBlack,
                fontWeight = FontWeight.Bold
            ) },
            placeholder = {
                Text(
                    "0123456789",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 20.sp
                )
            },
            leadingIcon = { Text("+84 -",
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ) },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LightBlack,
                fontWeight = FontWeight.Bold
            ) },
            placeholder = {
                Text(
                    "Enter your Password",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            },
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Visibility, "Toggle password visibility")
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Cofirm Password",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = LightBlack,
                fontWeight = FontWeight.Bold
            ) },
            placeholder = {
                Text(
                    "Enter your Password again",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            },
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Visibility, "Toggle password visibility")
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

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
                    onClick = {  },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Apple icon",
                        modifier = Modifier
                            .size(50.dp) ,
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
                    onClick = {  },
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
                    onClick = {  },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fb),
                        contentDescription = "Facebook icon",
                        modifier = Modifier
                            .size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Register()
        }
    }
}


