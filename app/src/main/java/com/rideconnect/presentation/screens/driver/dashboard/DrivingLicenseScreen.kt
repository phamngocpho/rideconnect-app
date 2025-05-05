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
fun DrivingLicenseScreen(
    onDoneClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: MovelitViewModel
) {
    val context = LocalContext.current
    val isUploadingFront = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    // Tạo launcher để chọn ảnh từ thiết bị
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (isUploadingFront.value) {
                viewModel.setDrivingLicenseUri(it)
                // Chuyển đổi Uri thành ImageBitmap
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    viewModel.setDrivingLicense(bitmap.asImageBitmap())
                } catch (e: Exception) {
                    Log.e("DrivingLicenseScreen", "Lỗi khi tải ảnh mặt trước", e)
                }
            } else {
                viewModel.setDrivingLicenseBackUri(it)
                // Chuyển đổi Uri thành ImageBitmap
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    viewModel.setDrivingLicenseBack(bitmap.asImageBitmap())
                } catch (e: Exception) {
                    Log.e("DrivingLicenseScreen", "Lỗi khi tải ảnh mặt sau", e)
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
            title = "Giấy phép lái xe",
            onBackClick = onBackClick
        )

        StepProgressBar(
            currentStep = 4,
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
                text = "Tải lên giấy phép lái xe",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "Vui lòng tải lên giấy phép lái xe để xác minh khả năng lái xe của bạn.",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Mặt trước GPLX
            DocumentUploadBox(
                title = "Mặt trước",
                onUploadClick = {
                    isUploadingFront.value = true
                    filePickerLauncher.launch("image/*")
                },
                image = viewModel.drivingLicense,
                imageUri = viewModel.drivingLicenseUri,
                imageInfo = if (viewModel.drivingLicense != null || viewModel.drivingLicenseUri != null)
                    "JPG • 250 kb" else null,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Mặt sau GPLX
            DocumentUploadBox(
                title = "Mặt sau",
                onUploadClick = {
                    isUploadingFront.value = false
                    filePickerLauncher.launch("image/*")
                },
                image = viewModel.drivingLicenseBack,
                imageUri = viewModel.drivingLicenseBackUri,
                imageInfo = if (viewModel.drivingLicenseBack != null || viewModel.drivingLicenseBackUri != null)
                    "JPG • 250 kb" else null,
                modifier = Modifier.padding(top = 16.dp)
            )

            UploadRequirements(
                requirements = listOf(
                    "Giấy phép lái xe còn hiệu lực",
                    "Ảnh chụp rõ nét, không bị mờ",
                    "Hiển thị đầy đủ thông tin trên giấy phép",
                    "Không chụp ảnh bị cắt xén hoặc chỉnh sửa"
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Lưu ý: Thông tin trên giấy phép lái xe phải trùng khớp với thông tin cá nhân bạn đã cung cấp.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        DriverPrimaryButton(
            text = "Hoàn thành",
            onClick = onDoneClick,
            enabled = (viewModel.drivingLicense != null || viewModel.drivingLicenseUri != null) &&
                    (viewModel.drivingLicenseBack != null || viewModel.drivingLicenseBackUri != null),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
