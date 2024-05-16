package com.example.collectibles_den.CoreFunction_AddCollection

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

class ScannerClass {

    @Composable
    fun ScannerDocument(
        onScanSuccess: (Uri) -> Unit,
        onScanError: (String) -> Unit
    ):Uri? {
        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(false)
            .setPageLimit(2)
            .setResultFormats(
                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
            )
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        var scannedImageUri by remember { mutableStateOf<Uri?>(null) }

        val scanner = GmsDocumentScanning.getClient(options)

        val scannerLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                scanningResult?.pages?.let { pages ->
                    if (pages.isNotEmpty()) {
                        scannedImageUri = pages[0].imageUri
                        onScanSuccess(pages[0].imageUri)
                    } else {
                        onScanError("No pages scanned")
                    }
                }
            } else {
                onScanError("Scanning cancelled or failed")
            }
        }

        val context = LocalContext.current
        val activity = context as Activity

        LaunchedEffect(Unit) {
            scanner.getStartScanIntent(activity)
                .addOnSuccessListener { intentSender ->
                    scannerLauncher.launch(
                        IntentSenderRequest.Builder(intentSender).build()
                    )
                }
                .addOnFailureListener { exception ->
                    Log.e("ScannerClass", exception.message.toString())
                    onScanError("Failed to start scanner: ${exception.message}")
                }
        }
        return scannedImageUri
    }

}
