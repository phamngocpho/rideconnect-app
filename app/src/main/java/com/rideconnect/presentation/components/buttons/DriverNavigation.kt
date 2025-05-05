package com.rideconnect.presentation.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// DriverAppBar
@Composable
fun DriverAppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = if (showBackButton) 0.dp else 16.dp)
        )
    }
}

// StepProgressBar
@Composable
fun StepProgressBar(
    modifier: Modifier = Modifier,
    numberOfSteps: Int = 4,
    currentStep: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (step in 1..numberOfSteps) {
            StepCircle(
                modifier = Modifier,
                isCompleted = step < currentStep,
                isCurrent = step == currentStep,
                stepNumber = step
            )

            if (step < numberOfSteps) {
                StepConnector(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp),
                    isActive = step < currentStep
                )
            }
        }
    }
}

@Composable
fun StepCircle(
    modifier: Modifier = Modifier,
    isCompleted: Boolean,
    isCurrent: Boolean,
    stepNumber: Int
) {
    val color = when {
        isCompleted || isCurrent -> Color(0xFF4CAF50)
        else -> Color.LightGray
    }

    Box(
        modifier = modifier
            .size(32.dp)
            .background(color, shape = CircleShape)
            .border(width = 1.dp, color = color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isCompleted) "✓" else stepNumber.toString(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StepConnector(
    modifier: Modifier = Modifier,
    isActive: Boolean
) {
    Divider(
        modifier = modifier,
        color = if (isActive) Color(0xFF4CAF50) else Color.LightGray,
        thickness = 2.dp
    )
}
