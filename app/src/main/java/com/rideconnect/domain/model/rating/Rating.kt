package com.rideconnect.domain.model.rating

import com.rideconnect.domain.model.user.User
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Rating(
    val id: String,
    val tripId: String,
    val fromUserId: String,
    val toUserId: String,
    val rating: Float, // 1-5 stars
    val comment: String?,
    val createdAt: ZonedDateTime,
    val fromUser: User? = null,
    val toUser: User? = null
) {
    fun getFormattedRating(): String {
        return String.format(Locale.US, "%.1f", rating)
    }

    fun getStarRating(): Float {
        return rating
    }

    fun getFormattedDate(): String {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    fun hasComment(): Boolean {
        return !comment.isNullOrEmpty()
    }

    fun isPositive(): Boolean {
        return rating >= 4.0f
    }

    fun isNeutral(): Boolean {
        return rating >= 3.0f && rating < 4.0f
    }

    fun isNegative(): Boolean {
        return rating < 3.0f
    }
}

// UI State cho Rating Screen
data class RatingUiState(
    val tripId: String = "",
    val toUserId: String = "",
    val ratingValue: Float = 0f,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val isSubmitted: Boolean = false
)

// UI State cho Rating List Screen
data class RatingListUiState(
    val ratings: List<Rating> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val averageRating: Float = 0f
)
