package com.rideconnect.presentation.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rideconnect.presentation.components.buttons.PrimaryButton

/**
 * Form nhập phản hồi sau chuyến đi
 * @param rating Số sao đánh giá hiện tại
 * @param feedback Nội dung phản hồi hiện tại
 * @param onRatingChanged Callback khi thay đổi đánh giá
 * @param onFeedbackChanged Callback khi thay đổi nội dung phản hồi
 * @param onSubmit Callback khi người dùng gửi phản hồi
 */
@Composable
fun FeedbackForm(
    rating: Int,
    feedback: String,
    onRatingChanged: (Int) -> Unit,
    onFeedbackChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Đánh giá chuyến đi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bạn cảm thấy chuyến đi thế nào?",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingBar(
                rating = rating,
                onRatingChanged = onRatingChanged,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedback,
                onValueChange = onFeedbackChanged,
                label = { Text("Chia sẻ trải nghiệm của bạn (không bắt buộc)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Gửi đánh giá",
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackFormPreview() {
    var rating by remember { mutableStateOf(4) }
    var feedback by remember { mutableStateOf("") }

    MaterialTheme {
        FeedbackForm(
            rating = rating,
            feedback = feedback,
            onRatingChanged = { rating = it },
            onFeedbackChanged = { feedback = it },
            onSubmit = { /* Handle submission */ },
            modifier = Modifier.padding(16.dp)
        )
    }
}
