package com.rideconnect.presentation.screens.driver.dashboard

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.presentation.components.buttons.DriverAppBar
import com.rideconnect.presentation.components.buttons.DriverPrimaryButton
import com.rideconnect.presentation.components.buttons.StepProgressBar
import com.rideconnect.presentation.components.inputs.DocumentUploadBox
import com.rideconnect.presentation.components.inputs.UploadRequirements

@Composable
fun GovernmentIdScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: MovelitViewModel
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isUploadingFront = remember { mutableStateOf(true) }

    // Tạo launcher để chọn ảnh từ thiết bị
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (isUploadingFront.value) {
                viewModel.setGovernmentIdUri(it)
                // Chuyển đổi Uri thành ImageBitmap
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    viewModel.setGovernmentId(bitmap.asImageBitmap())
                } catch (e: Exception) {
                    Log.e("GovernmentIdScreen", "Lỗi khi tải ảnh mặt trước", e)
                }
            } else {
                viewModel.setGovernmentIdBackUri(it)
                // Chuyển đổi Uri thành ImageBitmap
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    viewModel.setGovernmentIdBack(bitmap.asImageBitmap())
                } catch (e: Exception) {
                    Log.e("GovernmentIdScreen", "Lỗi khi tải ảnh mặt sau", e)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DriverAppBar(
            title = "Giấy tờ tùy thân",
            onBackClick = onBackClick
        )

        StepProgressBar(
            currentStep = 3,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Nội dung có thể cuộn
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Tải lên giấy tờ tùy thân",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "Vui lòng tải lên CMND/CCCD/Hộ chiếu để xác minh danh tính của bạn.",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Mặt trước giấy tờ
            DocumentUploadBox(
                title = "Mặt trước",
                onUploadClick = {
                    isUploadingFront.value = true
                    filePickerLauncher.launch("image/*")
                },
                image = viewModel.governmentId,
                imageUri = viewModel.governmentIdUri,
                imageInfo = if (viewModel.governmentId != null || viewModel.governmentIdUri != null)
                    "JPG • 250 kb" else null,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Mặt sau giấy tờ
            DocumentUploadBox(
                title = "Mặt sau",
                onUploadClick = {
                    isUploadingFront.value = false
                    filePickerLauncher.launch("image/*")
                },
                image = viewModel.governmentIdBack,
                imageUri = viewModel.governmentIdBackUri,
                imageInfo = if (viewModel.governmentIdBack != null || viewModel.governmentIdBackUri != null)
                    "JPG • 250 kb" else null,
                modifier = Modifier.padding(top = 16.dp)
            )

            UploadRequirements(
                requirements = listOf(
                    "Giấy tờ còn hiệu lực",
                    "Ảnh chụp rõ nét, không bị mờ",
                    "Hiển thị đầy đủ thông tin trên giấy tờ",
                    "Không chụp ảnh bị cắt xén hoặc chỉnh sửa"
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Lưu ý: Thông tin trên giấy tờ phải trùng khớp với thông tin cá nhân bạn đã cung cấp.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        DriverPrimaryButton(
            text = "Tiếp tục",
            onClick = onNextClick,
            enabled = (viewModel.governmentId != null || viewModel.governmentIdUri != null) &&
                    (viewModel.governmentIdBack != null || viewModel.governmentIdBackUri != null),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
