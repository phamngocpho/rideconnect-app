package com.rideconnect.presentation.screens.driver.dashboard

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@Composable
fun ProfilePictureScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: MovelitViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Tạo launcher để chọn ảnh từ thiết bị
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setProfilePictureUri(it)
            // Chuyển đổi Uri thành ImageBitmap
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                viewModel.setProfilePicture(bitmap.asImageBitmap())
            } catch (e: Exception) {
                Log.e("ProfilePictureScreen", "Lỗi khi tải ảnh", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DriverAppBar(
            title = "Ảnh đại diện",
            onBackClick = onBackClick
        )

        StepProgressBar(
            currentStep = 2,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Tải lên ảnh đại diện của bạn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Ảnh đại diện sẽ được hiển thị trên hồ sơ của bạn và giúp người dùng nhận diện bạn dễ dàng hơn.",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        DocumentUploadBox(
            title = "Ảnh đại diện",
            onUploadClick = {
                filePickerLauncher.launch("image/*")
            },
            image = viewModel.profilePicture,
            imageUri = viewModel.profilePictureUri,
            imageInfo = if (viewModel.profilePicture != null || viewModel.profilePictureUri != null)
                "JPG • 250 kb" else null,
            modifier = Modifier.padding(top = 16.dp)
        )

        UploadRequirements(
            requirements = listOf(
                "Ảnh chụp rõ nét khuôn mặt",
                "Không sử dụng ảnh có nhiều người",
                "Không sử dụng ảnh có logo hoặc văn bản",
                "Kích thước tối thiểu 500x500 pixel"
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        DriverPrimaryButton(
            text = "Tiếp tục",
            onClick = {
                coroutineScope.launch {
                    onNextClick()
                }
            },
            enabled = viewModel.profilePicture != null || viewModel.profilePictureUri != null,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
