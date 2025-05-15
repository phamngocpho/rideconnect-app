package com.rideconnect.util.document

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

class TextRecognizer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun recognizeText(imageUri: Uri, context: Context): String {
        return withContext(Dispatchers.IO) {
            try {
                val image = InputImage.fromFilePath(context, imageUri)
                val result = recognizer.process(image).await()
                result.text
            } catch (e: IOException) {
                Log.e("TextRecognizer", "Error reading image", e)
                throw e
            } catch (e: Exception) {
                Log.e("TextRecognizer", "Error processing image", e)
                throw e
            }
        }
    }
}
