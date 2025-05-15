package com.rideconnect.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

fun formatDateTime(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return "N/A"

    return try {
        // Parse chuỗi ISO 8601 thành ZonedDateTime
        val zonedDateTime = ZonedDateTime.parse(timestamp)

        // Chuyển đổi sang Date để sử dụng với SimpleDateFormat
        val date = Date.from(zonedDateTime.toInstant())

        // Format theo định dạng mong muốn
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN"))
        dateFormat.format(date)
    } catch (e: DateTimeParseException) {
        // Nếu không parse được, trả về "N/A"
        "N/A"
    } catch (e: Exception) {
        // Bắt các lỗi khác
        "N/A"
    }
}

fun formatPrice(price: BigDecimal?): String {
    if (price == null) return "N/A"
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    return formatter.format(price)
}
