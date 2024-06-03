package com.example.collectibles_den.coreFunction_AddCollection

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collectibles_den.logic.TakePhotosViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TakePhotosClass {

    @Composable
    fun setupCameraLauncher(
        viewModel: TakePhotosViewModel = viewModel(),
        onImageCaptured: (Uri) -> Unit,
        onError: (String) -> Unit
    ): Pair<ActivityResultLauncher<Uri>, ActivityResultLauncher<String>> {
        val context = LocalContext.current

        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.imageUri.value?.let(onImageCaptured) ?: onError("Image capture failed")
            } else {
                onError("Image capture failed")
            }
        }

        val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                val uri = createImageUri(context)
                if (uri != null) {
                    viewModel.setImageUri(uri)
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
