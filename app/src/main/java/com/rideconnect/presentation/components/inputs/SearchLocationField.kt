package com.rideconnect.presentation.components.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchLocationField(
    value: String,
    onValueChange: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Tìm kiếm địa điểm",
    suggestions: List<String> = emptyList()
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Search field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = { Text(text = placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Xóa",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        // Suggestions
        AnimatedVisibility(visible = isFocused && value.isNotEmpty() && suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(suggestions.filter {
                        it.contains(value, ignoreCase = true)
                    }) { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            onItemClick = {
                                onLocationSelected(suggestion)
                                onValueChange(suggestion)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    suggestion: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = suggestion,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchLocationFieldPreview() {
    var searchText by remember { mutableStateOf("") }
    val sampleLocations = listOf(
        "Hà Nội, Việt Nam",
        "Hồ Chí Minh, Việt Nam",
        "Đà Nẵng, Việt Nam",
        "Nha Trang, Khánh Hòa",
        "Đà Lạt, Lâm Đồng",
        "Huế, Thừa Thiên Huế",
        "Hạ Long, Quảng Ninh"
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SearchLocationField(
                value = searchText,
                onValueChange = { searchText = it },
                onLocationSelected = { },
                suggestions = sampleLocations
            )
        }
    }
}
