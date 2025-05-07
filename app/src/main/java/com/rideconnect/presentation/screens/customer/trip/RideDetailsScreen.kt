package com.rideconnect.presentation.screens.customer.trip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.R

data class RideOptionData(
    val id: String,
    val title: String,
    val iconResId: Int,
    val price: String,
    val time: String,
    val rating: String
)

data class PaymentMethodData(
    val id: String,
    val title: String,
    val iconResId: Int
)

@Composable
fun RideDetailsScreen(
    onBackClick: () -> Unit = {},
    onBookRideClick: (String) -> Unit = {}
) {
    // Danh sách các tùy chọn xe
    val rideOptions = listOf(
        RideOptionData(
            id = "saver",
            title = "Saver",
            iconResId = R.drawable.ic_car_saver,
            price = "EGP 40.25",
            time = "2 mins away",
            rating = "4.4"
        ),
        RideOptionData(
            id = "motor",
            title = "Motor",
            iconResId = R.drawable.ic_motorcycle,
            price = "EGP 30.15",
            time = "2 mins away",
            rating = "4.1"
        ),
        RideOptionData(
            id = "comfort",
            title = "Comfort",
            iconResId = R.drawable.ic_car_comfort,
            price = "EGP 70.45",
            time = "3 mins away",
            rating = "4.4"
        ),
        RideOptionData(
            id = "premium",
            title = "Premium",
            iconResId = R.drawable.ic_car_comfort,
            price = "EGP 90.75",
            time = "5 mins away",
            rating = "4.8"
        )
    )

    // Danh sách các phương thức thanh toán
    val paymentMethods = listOf(
        PaymentMethodData(
            id = "cash",
            title = "Cash",
            iconResId = R.drawable.ic_cash
        ),
        PaymentMethodData(
            id = "mastercard",
            title = "Mastercard",
            iconResId = R.drawable.ic_mastercard
        ),
        PaymentMethodData(
            id = "paypal",
            title = "PayPal",
            iconResId = R.drawable.ic_paypal
        ),
        PaymentMethodData(
            id = "add",
            title = "Add new",
            iconResId = R.drawable.ic_add_payment
        )
    )

    // Trạng thái cho loại xe được chọn
    var selectedRideOption by remember { mutableStateOf(rideOptions[0].id) }

    // Trạng thái cho việc hiển thị phần phương thức thanh toán
    var showPaymentMethodSection by remember { mutableStateOf(false) }

    // Trạng thái cho việc hiển thị phần nhập mã khuyến mãi
    var showPromoCodeSection by remember { mutableStateOf(false) }

    // Trạng thái cho phương thức thanh toán được chọn
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods[0].id) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Map background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Route visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxWidth(0.8f)) {
                    val startX = 0f
                    val endX = size.width
                    val y = size.height / 2

                    // Draw route line
                    drawLine(
                        color = Color(0xFF4CAF50),
                        start = Offset(startX, y),
                        end = Offset(endX, y),
                        strokeWidth = 4.dp.toPx()
                    )

                    // Draw start point
                    drawCircle(
                        color = Color.Red,
                        radius = 8.dp.toPx(),
                        center = Offset(startX + 20.dp.toPx(), y)
                    )

                    // Draw end point
                    drawCircle(
                        color = Color(0xFF2196F3),
                        radius = 8.dp.toPx(),
                        center = Offset(endX - 20.dp.toPx(), y)
                    )
                }
            }
        }

        // Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.LightGray
                    )
                }

                // Ride Details Title
                Text(
                    text = "Ride Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Danh sách các tùy chọn xe
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Hiển thị tất cả các tùy chọn xe
                    rideOptions.forEach { option ->
                        RideOption(
                            title = option.title,
                            iconResId = option.iconResId,
                            price = option.price,
                            time = option.time,
                            rating = option.rating,
                            isSelected = selectedRideOption == option.id,
                            onSelect = { selectedRideOption = option.id }
                        )
                    }
                }

                // Spacer để tạo khoảng cách với phần thanh toán
                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị phần phương thức thanh toán hoặc mã khuyến mãi
                if (showPaymentMethodSection || showPromoCodeSection) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Payment method",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Hiển thị các phương thức thanh toán
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                paymentMethods.forEach { method ->
                                    PaymentMethodItem(
                                        iconResId = method.iconResId,
                                        isSelected = selectedPaymentMethod == method.id,
                                        onClick = { selectedPaymentMethod = method.id }
                                    )
                                }
                            }

                            // Hiển thị thông tin chi tiết của phương thức thanh toán đã chọn
                            // Chỉ hiển thị khi showPaymentMethodSection = true và showPromoCodeSection = false
                            if (showPaymentMethodSection && !showPromoCodeSection) {
                                when (selectedPaymentMethod) {
                                    "cash" -> {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_cash),
                                                contentDescription = "Cash",
                                                tint = Color(0xFF4CAF50),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Cash",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = "Pay with cash after your ride",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                    "mastercard" -> {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_mastercard),
                                                contentDescription = "Mastercard",
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Mastercard ****1234",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = "Expires 12/25",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                    "paypal" -> {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_paypal),
                                                contentDescription = "PayPal",
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "PayPal",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = "Connected to your PayPal account",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                }
                            }

                            // Hiển thị phần nhập mã khuyến mãi khi showPromoCodeSection = true
                            if (showPromoCodeSection) {
                                // Nếu đang hiển thị phần payment method và promo code, thêm đường kẻ ngăn cách
                                if (showPaymentMethodSection) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        color = Color.LightGray
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_coupon),
                                        contentDescription = "Coupons",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Apply Coupon",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(
                                                width = 1.dp,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 12.dp)
                                    ) {
                                        Text(
                                            text = "Enter promo code",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { /* Xử lý kích hoạt mã */ },
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50)
                                        )
                                    ) {
                                        Text(
                                            text = "Apply",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Spacer để tạo khoảng cách với các nút ở dưới
                Spacer(modifier = Modifier.height(16.dp))

                // Payment and Coupon Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (showPaymentMethodSection) 2.dp else 1.dp,
                                color = if (showPaymentMethodSection) Color(0xFF4CAF50) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable {
                                showPaymentMethodSection = !showPaymentMethodSection
                                // Nếu đóng payment, đồng thời đóng promo code
                                if (!showPaymentMethodSection) {
                                    showPromoCodeSection = false
                                }
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cash),
                            contentDescription = "Cash",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cash",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (showPaymentMethodSection) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (showPaymentMethodSection) "Collapse" else "Expand",
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (showPromoCodeSection) 2.dp else 1.dp,
                                color = if (showPromoCodeSection) Color(0xFF4CAF50) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable {
                                showPromoCodeSection = !showPromoCodeSection
                                // Khi nhấn vào Coupons, luôn hiển thị phần payment method
                                // nhưng không hiển thị chi tiết phương thức thanh toán
                                if (showPromoCodeSection) {
                                    showPaymentMethodSection = true
                                }
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_coupon),
                            contentDescription = "Coupons",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Coupons",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (showPromoCodeSection) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (showPromoCodeSection) "Collapse" else "Expand",
                            tint = Color.Gray
                        )
                    }
                }

                // Book button
                val selectedOption = rideOptions.find { it.id == selectedRideOption }
                Button(
                    onClick = { onBookRideClick(selectedRideOption) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "Book a ${selectedOption?.title ?: "Ride"}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF4CAF50) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// Hàm mở rộng để chuyển đổi Float thành Dp
@Composable
private fun Float.toDp(): androidx.compose.ui.unit.Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

@Composable
fun RideOption(
    title: String,
    iconResId: Int,
    price: String,
    time: String,
    rating: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF4CAF50) else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .clickable(onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )

        // Info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "$time • $rating",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Price
        Text(
            text = price,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Radio button
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF4CAF50),
                unselectedColor = Color.LightGray
            )
        )
    }
}