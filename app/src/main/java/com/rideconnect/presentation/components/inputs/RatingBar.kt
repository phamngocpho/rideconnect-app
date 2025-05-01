package com.rideconnect.presentation.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Component hiển thị đánh giá bằng sao (1-5)
 * @param rating Số sao hiện tại (1-5)
 * @param onRatingChanged Callback khi người dùng thay đổi số sao
 * @param starSize Kích thước của mỗi ngôi sao
 * @param starColor Màu của ngôi sao đã chọn
 * @param starSpacing Khoảng cách giữa các ngôi sao
 * @param isEditable Cho phép người dùng thay đổi đánh giá hay không
 */
@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp = 36.dp,
    starColor: Color = MaterialTheme.colorScheme.primary,
    starSpacing: Dp = 4.dp,
    isEditable: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(starSpacing)
    ) {
        for (i in 1..5) {
            IconButton(
                onClick = { if (isEditable) onRatingChanged(i) },
                modifier = Modifier.size(starSize)
            ) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarOutline,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) starColor else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(starSize)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingBarPreview() {
    var currentRating by remember { mutableStateOf(3) }
    MaterialTheme {
        RatingBar(
            rating = currentRating,
            onRatingChanged = { currentRating = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}
