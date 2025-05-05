package com.rideconnect.presentation.screens.driver.dashboard

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel

class MovelitViewModel : ViewModel() {
    // Personal Details
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var password by mutableStateOf("")
    var gender by mutableStateOf("")
    var city by mutableStateOf("")
    var termsAccepted by mutableStateOf(false)

    // Profile Picture
    private var _profilePictureUri = mutableStateOf<Uri?>(null)
    val profilePictureUri: Uri? get() = _profilePictureUri.value
    private var _profilePicture = mutableStateOf<ImageBitmap?>(null)
    val profilePicture: ImageBitmap? get() = _profilePicture.value

    // Government ID (Mặt trước)
    private var _governmentIdUri = mutableStateOf<Uri?>(null)
    val governmentIdUri: Uri? get() = _governmentIdUri.value
    private var _governmentId = mutableStateOf<ImageBitmap?>(null)
    val governmentId: ImageBitmap? get() = _governmentId.value

    // Government ID Back (Mặt sau)
    private var _governmentIdBackUri = mutableStateOf<Uri?>(null)
    val governmentIdBackUri: Uri? get() = _governmentIdBackUri.value
    private var _governmentIdBack = mutableStateOf<ImageBitmap?>(null)
    val governmentIdBack: ImageBitmap? get() = _governmentIdBack.value

    // Driving License
    private var _drivingLicenseUri = mutableStateOf<Uri?>(null)
    val drivingLicenseUri: Uri? get() = _drivingLicenseUri.value
    private var _drivingLicense = mutableStateOf<ImageBitmap?>(null)
    val drivingLicense: ImageBitmap? get() = _drivingLicense.value

    // Driving License Back
    private var _drivingLicenseBackUri = mutableStateOf<Uri?>(null)
    val drivingLicenseBackUri: Uri? get() = _drivingLicenseBackUri.value
    private var _drivingLicenseBack = mutableStateOf<ImageBitmap?>(null)
    val drivingLicenseBack: ImageBitmap? get() = _drivingLicenseBack.value

    // Setters for Profile Picture
    fun setProfilePictureUri(uri: Uri) {
        _profilePictureUri.value = uri
    }

    fun setProfilePicture(bitmap: ImageBitmap) {
        _profilePicture.value = bitmap
    }

    // Setters for Government ID (Mặt trước)
    fun setGovernmentIdUri(uri: Uri) {
        _governmentIdUri.value = uri
    }

    fun setGovernmentId(bitmap: ImageBitmap) {
        _governmentId.value = bitmap
    }

    // Setters for Government ID Back (Mặt sau)
    fun setGovernmentIdBackUri(uri: Uri) {
        _governmentIdBackUri.value = uri
    }

    fun setGovernmentIdBack(bitmap: ImageBitmap) {
        _governmentIdBack.value = bitmap
    }

    // Setters for Driving License
    fun setDrivingLicenseUri(uri: Uri) {
        _drivingLicenseUri.value = uri
    }

    fun setDrivingLicense(bitmap: ImageBitmap) {
        _drivingLicense.value = bitmap
    }

    // Setters for Driving License Back
    fun setDrivingLicenseBackUri(uri: Uri) {
        _drivingLicenseBackUri.value = uri
    }

    fun setDrivingLicenseBack(bitmap: ImageBitmap) {
        _drivingLicenseBack.value = bitmap
    }

    // Hàm tiện ích để kiểm tra trạng thái hoàn thành của từng bước
    fun isPersonalInfoComplete(): Boolean {
        return name.isNotBlank() && email.isNotBlank() &&
                phoneNumber.isNotBlank() && gender.isNotBlank() &&
                city.isNotBlank() && termsAccepted
    }

    fun isProfilePictureComplete(): Boolean {
        return profilePicture != null || profilePictureUri != null
    }

    fun isGovernmentIdComplete(): Boolean {
        return (governmentId != null || governmentIdUri != null) &&
                (governmentIdBack != null || governmentIdBackUri != null)
    }

    fun isDrivingLicenseComplete(): Boolean {
        return (drivingLicense != null || drivingLicenseUri != null) &&
                (drivingLicenseBack != null || drivingLicenseBackUri != null)
    }
}
