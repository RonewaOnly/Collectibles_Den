package com.example.collectibles_den.CoreFunction_AddCollection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakePhotosClass {

    @Composable
    fun setupCameraLauncher(
        context: Context,
        onImageCaptured: (Uri) -> Unit,
        onError: (String) -> Unit
    ): Pair<ActivityResultLauncher<Uri>, ActivityResultLauncher<String>> {
        var captureImageUri by remember {
            mutableStateOf<Uri>(Uri.EMPTY)
        }

        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onImageCaptured(captureImageUri)
            } else {
                onError("Image capture failed")
            }
        }

        val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                val uri = createImageUri(context)
                if (uri != null) {
                    captureImageUri = uri
                    cameraLauncher.launch(uri)
                } else {
                    onError("Failed to create image file")
                }
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                onError("Permission denied")
            }
        }

        return Pair(cameraLauncher, permissionLauncher)
    }

    fun createImageUri(context: Context): Uri? {
        val file = createImageFile(context)
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )
    }

    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_${timestamp}_"
        return File.createTempFile(
            imageFileName,
            ".jpg",
            context.getExternalFilesDir(null)
        )
    }
}
