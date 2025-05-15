import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rideconnect.presentation.screens.driver.document.DocumentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DocumentScannerViewModel : ViewModel() {
    private val TAG = "DocumentScannerVM"

    private val _recognizedTexts = MutableStateFlow<Map<DocumentType, String>>(emptyMap())
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _extractedInfo = MutableStateFlow<Map<String, String>>(emptyMap())
    val extractedInfo: StateFlow<Map<String, String>> = _extractedInfo

    fun scanMultipleDocuments(
        drivingLicenseUri: Uri?,
        vehicleRegistrationUri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Bắt đầu quét giấy tờ")
            _extractedInfo.value = emptyMap()

            try {
                if (drivingLicenseUri != null) {
                    Log.d(TAG, "Quét bằng lái xe: $drivingLicenseUri")
                    val drivingLicenseText = recognizeTextSuspend(drivingLicenseUri, context)
                    _recognizedTexts.update { it + (DocumentType.DRIVING_LICENSE to drivingLicenseText) }

                    Log.d(TAG, "Văn bản bằng lái xe:\n$drivingLicenseText")
                    val licenseNumber = extractLicenseNumber(drivingLicenseText)
                    if (licenseNumber != null) {
                        Log.d(TAG, "Đã trích xuất số bằng lái xe: $licenseNumber")
                        _extractedInfo.update { it + ("Số bằng lái xe" to licenseNumber) }
                    } else {
                        Log.w(TAG, "Không tìm thấy số bằng lái xe")
                    }
                }

                if (vehicleRegistrationUri != null) {
                    Log.d(TAG, "Quét giấy tờ xe: $vehicleRegistrationUri")
                    val vehicleRegistrationText = recognizeTextSuspend(vehicleRegistrationUri, context)
                    _recognizedTexts.update { it + (DocumentType.VEHICLE_REGISTRATION to vehicleRegistrationText) }

                    Log.d(TAG, "Văn bản giấy tờ xe:\n$vehicleRegistrationText")
                    logTextLines(vehicleRegistrationText)

                    val brand = extractVehicleBrand(vehicleRegistrationText)
                    if (brand != null) {
                        Log.d(TAG, "Đã trích xuất nhãn hiệu: $brand")
                        _extractedInfo.update { it + ("Nhãn hiệu" to brand) }
                    } else {
                        Log.w(TAG, "Không tìm thấy nhãn hiệu xe")
                        val brandByKeyword = findBrandByKeyword(vehicleRegistrationText)
                        if (brandByKeyword != null) {
                            Log.d(TAG, "Đã tìm thấy nhãn hiệu bằng từ khóa: $brandByKeyword")
                            _extractedInfo.update { it + ("Nhãn hiệu" to brandByKeyword) }
                        } else {
                            val brandByLine = findBrandByLine(vehicleRegistrationText)
                            if (brandByLine != null) {
                                Log.d(TAG, "Đã tìm thấy nhãn hiệu bằng phân tích dòng: $brandByLine")
                                _extractedInfo.update { it + ("Nhãn hiệu" to brandByLine) }
                            }
                        }
                    }

                    val licensePlate = extractLicensePlate(vehicleRegistrationText)
                    if (licensePlate != null) {
                        Log.d(TAG, "Đã trích xuất biển số xe: $licensePlate")
                        _extractedInfo.update { it + ("Biển số xe" to licensePlate) }
                    } else {
                        Log.w(TAG, "Không tìm thấy biển số xe")
                        val plateByPattern = findLicensePlateByPattern(vehicleRegistrationText)
                        if (plateByPattern != null) {
                            Log.d(TAG, "Đã tìm thấy biển số xe bằng pattern: $plateByPattern")
                            _extractedInfo.update { it + ("Biển số xe" to plateByPattern) }
                        } else {
                            val plateByLine = findPlateByLine(vehicleRegistrationText)
                            if (plateByLine != null) {
                                Log.d(TAG, "Đã tìm thấy biển số xe bằng phân tích dòng: $plateByLine")
                                _extractedInfo.update { it + ("Biển số xe" to plateByLine) }
                            }
                        }
                    }

                    val color = extractVehicleColor(vehicleRegistrationText)
                    if (color != null) {
                        Log.d(TAG, "Đã trích xuất màu xe: $color")
                        _extractedInfo.update { it + ("Màu xe" to color) }
                    } else {
                        Log.w(TAG, "Không tìm thấy màu xe")
                    }
                }

                Log.d(TAG, "Thông tin đã trích xuất: ${_extractedInfo.value}")

            } finally {
                _isLoading.value = false
                Log.d(TAG, "Kết thúc quét giấy tờ")
            }
        }
    }

    private suspend fun recognizeTextSuspend(uri: Uri, context: Context): String = suspendCoroutine { continuation ->
        try {
            val image = InputImage.fromFilePath(context, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(visionText.text)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Lỗi nhận dạng văn bản: ${e.message}", e)
                    continuation.resume("Lỗi nhận dạng: ${e.message}")
                }
        } catch (e: IOException) {
            Log.e(TAG, "Lỗi đọc ảnh: ${e.message}", e)
            continuation.resume("Lỗi đọc ảnh: ${e.message}")
        }
    }

    private fun extractVehicleBrand(text: String): String? {
        Log.d(TAG, "Đang tìm nhãn hiệu xe...")

        val brandPatterns = listOf(
            Regex("(?:Nhãn hiệu|Nhãn hiệu xe|Nhãn|Hiệu xe|Hiệu|Loại xe|Loại)(?:\\s*|\\(Brand\\))?\\s*:?\\s*([A-Za-z0-9\\s\\-\\.]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            Regex("(?:MAKE|BRAND|TYPE|MODEL)\\s*:?\\s*([A-Za-z0-9\\s\\-\\.]+?)(?:\\n|$)", RegexOption.IGNORE_CASE)
        )

        for (pattern in brandPatterns) {
            val matchResult = pattern.find(text)
            if (matchResult != null && matchResult.groupValues.size > 1) {
                val brand = matchResult.groupValues[1].trim()
                Log.d(TAG, "Tìm thấy nhãn hiệu với pattern ${pattern.pattern}: $brand")

                if (brand.length <= 2) {
                    Log.w(TAG, "Nhãn hiệu quá ngắn, có thể không chính xác: $brand")
                    continue
                }

                return brand
            }
        }

        return null
    }

    private fun findBrandByLine(text: String): String? {
        val lines = text.split("\\n")
        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.contains("Nhãn hiệu", ignoreCase = true) ||
                trimmedLine.contains("Brand", ignoreCase = true)) {

                Log.d(TAG, "Dòng chứa thông tin nhãn hiệu: $trimmedLine")

                val colonIndex = trimmedLine.indexOf(":")
                if (colonIndex >= 0 && colonIndex < trimmedLine.length - 1) {
                    val brandValue = trimmedLine.substring(colonIndex + 1).trim()
                    Log.d(TAG, "Trích xuất nhãn hiệu từ dòng: $brandValue")
                    return brandValue
                }

                val commonBrands = listOf(
                    "TOYOTA", "HONDA", "MAZDA", "KIA", "HYUNDAI", "FORD", "MITSUBISHI", "SUZUKI",
                    "MERCEDES", "BMW", "AUDI", "LEXUS", "VINFAST", "CHEVROLET", "NISSAN", "ISUZU"
                )

                for (brand in commonBrands) {
                    if (trimmedLine.contains(brand, ignoreCase = true)) {
                        Log.d(TAG, "Tìm thấy nhãn hiệu '$brand' trong dòng")
                        return brand
                    }
                }
            }
        }

        return null
    }

    private fun findBrandByKeyword(text: String): String? {
        val commonBrands = listOf(
            "TOYOTA", "HONDA", "MAZDA", "KIA", "HYUNDAI", "FORD", "MITSUBISHI", "SUZUKI",
            "MERCEDES", "BMW", "AUDI", "LEXUS", "VINFAST", "CHEVROLET", "NISSAN", "ISUZU"
        )

        for (brand in commonBrands) {
            if (text.contains(brand, ignoreCase = true)) {
                Log.d(TAG, "Tìm thấy nhãn hiệu bằng từ khóa: $brand")

                val lines = text.split("\\n")
                for (line in lines) {
                    if (line.contains(brand, ignoreCase = true)) {
                        val trimmedLine = line.trim()
                        Log.d(TAG, "Dòng chứa nhãn hiệu: $trimmedLine")

                        if (trimmedLine.length > 30) {
                            val brandIndex = trimmedLine.indexOf(brand, ignoreCase = true)
                            val startIndex = maxOf(0, brandIndex - 5)
                            val endIndex = minOf(trimmedLine.length, brandIndex + brand.length + 15)
                            return trimmedLine.substring(startIndex, endIndex).trim()
                        }
                        return trimmedLine
                    }
                }

                return brand
            }
        }

        return null
    }

    private fun extractLicensePlate(text: String): String? {
        Log.d(TAG, "Đang tìm biển số xe...")

        val platePatterns = listOf(
            Regex("(?:Biển số|Biển số xe|Biển đăng ký|Số đăng ký|Biển kiểm soát|BKS|Biền số)(?:\\s*|\\(N° plate\\))?\\s*:?\\s*([A-Za-z0-9\\-\\.\\s]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            Regex("(?:LICENSE PLATE|PLATE NUMBER|REG NUMBER)\\s*:?\\s*([A-Za-z0-9\\-\\.\\s]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            Regex("\\b(\\d{2}[A-Z]\\d?-\\d{3}\\.\\d{2})\\b")
        )

        for (pattern in platePatterns) {
            val matchResult = pattern.find(text)
            if (matchResult != null && matchResult.groupValues.size > 1) {
                val plate = matchResult.groupValues[1].trim()
                Log.d(TAG, "Tìm thấy biển số xe với pattern ${pattern.pattern}: $plate")
                return plate
            }
        }

        val directPlatePattern = Regex("\\b\\d{2}[A-Z]\\d?-\\d{3}\\.\\d{2}\\b")
        val directMatch = directPlatePattern.find(text)
        if (directMatch != null) {
            val plate = directMatch.value
            Log.d(TAG, "Tìm thấy biển số xe trực tiếp: $plate")
            return plate
        }

        return null
    }

    private fun findPlateByLine(text: String): String? {
        val lines = text.split("\\n")
        for (i in lines.indices) {
            val line = lines[i].trim()

            if (line.contains("Biển số", ignoreCase = true) ||
                line.contains("Biền số", ignoreCase = true) ||
                line.contains("plate", ignoreCase = true)) {

                Log.d(TAG, "Dòng chứa thông tin biển số: $line")

                val colonIndex = line.indexOf(":")
                if (colonIndex >= 0 && colonIndex < line.length - 1) {
                    val plateValue = line.substring(colonIndex + 1).trim()
                    if (plateValue.isNotEmpty()) {
                        Log.d(TAG, "Trích xuất biển số từ dòng hiện tại: $plateValue")
                        return plateValue
                    }
                }

                for (j in 1..3) {
                    if (i + j < lines.size) {
                        val nextLine = lines[i + j].trim()
                        val platePattern = Regex("\\b\\d{2}[A-Z]\\d?-?\\d{3}(\\.\\d{2})?\\b")
                        val match = platePattern.find(nextLine)
                        if (match != null) {
                            Log.d(TAG, "Trích xuất biển số từ dòng tiếp theo: ${match.value}")
                            return match.value
                        }

                        if (nextLine.matches(Regex(".*\\d+.*")) && !nextLine.contains(":")) {
                            Log.d(TAG, "Trích xuất biển số từ dòng tiếp theo: $nextLine")
                            return nextLine
                        }
                    }
                }
            }

            val platePattern = Regex("\\b\\d{2}[A-Z]\\d?-\\d{3}\\.\\d{2}\\b")
            val match = platePattern.find(line)
            if (match != null) {
                Log.d(TAG, "Tìm thấy biển số xe trực tiếp trong dòng: ${match.value}")
                return match.value
            }
        }

        return null
    }

    private fun findLicensePlateByPattern(text: String): String? {
        val vietnamesePlatePatterns = listOf(
            Regex("\\b\\d{2}[A-Z]\\d{4,5}\\b"),
            Regex("\\b\\d{2}[A-Z][\\.-]\\d{3,5}\\b"),
            Regex("\\b\\d{2}\\s*[A-Z]\\s*\\d{4,5}\\b"),
            Regex("\\b\\d{2}[A-Z]\\d?-\\d{3}\\.\\d{2}\\b")
        )

        for (pattern in vietnamesePlatePatterns) {
            val matchResult = pattern.find(text)
            if (matchResult != null) {
                val plate = matchResult.value.trim()
                Log.d(TAG, "Tìm thấy biển số xe với pattern Việt Nam ${pattern.pattern}: $plate")
                return plate
            }
        }

        return null
    }

    private fun extractLicenseNumber(text: String): String? {
        Log.d(TAG, "Đang tìm số bằng lái xe...")

        val licenseNumberPatterns = listOf(
            // Match the specific format in Vietnamese driving licenses
            Regex("(?:Só/No:|Số/No:|Số:|No:)\\s*([A-Za-z0-9\\-\\s]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            // Match the specific format with GPLX
            Regex("(?:Số GPLX|Số giấy phép lái xe)\\s*:?\\s*([A-Za-z0-9\\-\\s]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            // Match English formats
            Regex("(?:LICENSE NUMBER|DL NUMBER|LICENSE NO)\\s*:?\\s*([A-Za-z0-9\\-\\s]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            // Match common license number formats (9-12 digits, possibly ending with a letter)
            Regex("\\b(\\d{9,12}[A-Z]?)\\b")
        )

        for (pattern in licenseNumberPatterns) {
            val matchResult = pattern.find(text)
            if (matchResult != null) {
                val licenseNumber = if (matchResult.groupValues.size > 1) {
                    matchResult.groupValues[1].trim()
                } else {
                    matchResult.value.trim()
                }

                // Additional validation - license numbers are typically longer than 5 characters
                if (licenseNumber.length >= 5) {
                    Log.d(TAG, "Tìm thấy số bằng lái xe với pattern ${pattern.pattern}: $licenseNumber")
                    return licenseNumber
                } else {
                    Log.d(TAG, "Bỏ qua kết quả quá ngắn: $licenseNumber")
                }
            }
        }

        // Special case for Vietnamese format - look for "Só/No:" or similar patterns
        val specificPattern = Regex("Só/No:\\s*(\\S+)", RegexOption.IGNORE_CASE)
        val specificMatch = specificPattern.find(text)
        if (specificMatch != null && specificMatch.groupValues.size > 1) {
            val licenseNumber = specificMatch.groupValues[1].trim()
            Log.d(TAG, "Tìm thấy số bằng lái xe với pattern đặc biệt: $licenseNumber")
            return licenseNumber
        }

        return null
    }

    private fun extractVehicleColor(text: String): String? {
        Log.d(TAG, "Đang tìm màu xe...")

        val colorPatterns = listOf(
            Regex("(?:Màu sắc|Màu xe|Màu|Màu sơn)(?:\\s*:?\\s*)([A-Za-z0-9\\s\\-]+?)(?:\\n|$)", RegexOption.IGNORE_CASE),
            Regex("(?:COLOR|VEHICLE COLOR|PAINT)(?:\\s*:?\\s*)([A-Za-z0-9\\s\\-]+?)(?:\\n|$)", RegexOption.IGNORE_CASE)
        )

        for (pattern in colorPatterns) {
            val matchResult = pattern.find(text)
            if (matchResult != null && matchResult.groupValues.size > 1) {
                val color = matchResult.groupValues[1].trim()
                Log.d(TAG, "Tìm thấy màu xe với pattern ${pattern.pattern}: $color")
                return color
            }
        }

        val commonColors = listOf(
            "Đỏ", "Xanh", "Trắng", "Đen", "Vàng", "Bạc", "Xám", "Nâu", "Cam", "Hồng", "Tím", "Bạc Đen"
        )
        val lines = text.split("\\n")
        for (line in lines) {
            val trimmedLine = line.trim()
            for (color in commonColors) {
                if (trimmedLine.contains(color, ignoreCase = true)) {
                    Log.d(TAG, "Tìm thấy màu xe trong dòng: $trimmedLine")
                    return color
                }
            }
        }

        return null
    }

    private fun logTextLines(text: String) {
        val lines = text.split("\\n")
        Log.d(TAG, "Phân tích ${lines.size} dòng văn bản:")

        for ((index, line) in lines.withIndex()) {
            val trimmedLine = line.trim()
            if (trimmedLine.isNotEmpty()) {
                Log.d(TAG, "Dòng $index: '$trimmedLine'")
            }
        }
    }

    fun saveDocumentInfo(info: Map<String, String>, vehicleType: String) {
        Log.d(TAG, "Lưu thông tin giấy tờ: $info, $vehicleType")
        // Lưu thông tin vào cơ sở dữ liệu hoặc SharedPreferences
    }
}
