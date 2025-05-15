package com.rideconnect.presentation.screens.driver.document

import DocumentScannerViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.rideconnect.presentation.navigation.Screen
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

enum class DocumentType {
    VEHICLE_REGISTRATION,
    DRIVING_LICENSE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScannerScreen(
    navController: NavController,
    viewModel: DocumentScannerViewModel = viewModel()
) {
    val context = LocalContext.current

    val drivingLicenseUri = remember { mutableStateOf<Uri?>(null) }
    val vehicleRegistrationUri = remember { mutableStateOf<Uri?>(null) }

    val currentDocumentType = remember { mutableStateOf(DocumentType.DRIVING_LICENSE) }

    val isLoading = viewModel.isLoading.collectAsState()
    val extractedInfo = viewModel.extractedInfo.collectAsState()

    var vehicleType by remember { mutableStateOf("") }


    val tempImageFile = remember {
        File.createTempFile(
            "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}_",
            ".jpg",
            context.cacheDir
        ).apply {
            deleteOnExit()
        }
    }

    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (currentDocumentType.value == DocumentType.DRIVING_LICENSE) {
                drivingLicenseUri.value = it
            } else {
                vehicleRegistrationUri.value = it
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            if (currentDocumentType.value == DocumentType.DRIVING_LICENSE) {
                drivingLicenseUri.value = tempImageUri
            } else {
                vehicleRegistrationUri.value = tempImageUri
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(tempImageUri)
        } else {
            Log.e("DocumentScannerScreen", "Không có quyền camera")
        }
    }

    fun checkCameraPermissionAndLaunch() {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(tempImageUri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun scanBothDocuments() {
        viewModel.scanMultipleDocuments(
            drivingLicenseUri = drivingLicenseUri.value,
            vehicleRegistrationUri = vehicleRegistrationUri.value,
            context = context
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quét giấy tờ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Phần 1: Bằng lái xe
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Bằng lái xe",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (drivingLicenseUri.value != null && extractedInfo.value.containsKey("Số bằng lái xe")) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.Green
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (drivingLicenseUri.value != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(drivingLicenseUri.value),
                                contentDescription = "Ảnh bằng lái xe",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        extractedInfo.value["Số bằng lái xe"]?.let { licenseNumber ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Số bằng lái xe:", fontWeight = FontWeight.Bold)
                                Text(text = licenseNumber)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    currentDocumentType.value = DocumentType.DRIVING_LICENSE
                                    checkCameraPermissionAndLaunch()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chụp ảnh")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                onClick = {
                                    currentDocumentType.value = DocumentType.DRIVING_LICENSE
                                    galleryLauncher.launch("image/*")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chọn ảnh")
                            }
                        }
                    }
                }
            }

            // Phần 2: Giấy tờ xe
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Giấy tờ xe",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (vehicleRegistrationUri.value != null &&
                            (extractedInfo.value.containsKey("Nhãn hiệu") || extractedInfo.value.containsKey("Biển số xe"))) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.Green
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (vehicleRegistrationUri.value != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(vehicleRegistrationUri.value),
                                contentDescription = "Ảnh giấy tờ xe",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column {
                            extractedInfo.value["Nhãn hiệu"]?.let { brand ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Nhãn hiệu:", fontWeight = FontWeight.Bold)
                                    Text(text = brand)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            extractedInfo.value["Biển số xe"]?.let { licensePlate ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Biển số xe:", fontWeight = FontWeight.Bold)
                                    Text(text = licensePlate)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            extractedInfo.value["Màu xe"]?.let { color ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Màu xe:", fontWeight = FontWeight.Bold)
                                    Text(text = color)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    currentDocumentType.value = DocumentType.VEHICLE_REGISTRATION
                                    checkCameraPermissionAndLaunch()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chụp ảnh")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                onClick = {
                                    currentDocumentType.value = DocumentType.VEHICLE_REGISTRATION
                                    galleryLauncher.launch("image/*")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chọn ảnh")
                            }
                        }
                    }
                }
            }

            if (drivingLicenseUri.value != null && vehicleRegistrationUri.value != null) {
                Button(
                    onClick = { scanBothDocuments() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Quét và trích xuất thông tin từ cả hai giấy tờ")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoading.value) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            if ((extractedInfo.value.containsKey("Số bằng lái xe") ||
                        extractedInfo.value.containsKey("Nhãn hiệu") ||
                        extractedInfo.value.containsKey("Biển số xe") ||
                        extractedInfo.value.containsKey("Màu xe")) &&
                !isLoading.value) {

                OutlinedTextField(
                    value = vehicleType,
                    onValueChange = { vehicleType = it },
                    label = { Text("Loại xe") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.saveDocumentInfo(extractedInfo.value, vehicleType)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lưu thông tin giấy tờ")
                }
            }
        }
    }
}


