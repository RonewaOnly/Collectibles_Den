package com.example.collectibles_den.coreFunction_AddCollection

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.storage.StorageReference
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.util.UUID

class ScannerClass {

    @Composable
    fun scannerDocument(
        storageRef: StorageReference, // Add this parameter to pass Firebase Storage reference
        onScanSuccess: (Uri) -> Unit,
        onScanError: (String) -> Unit
    ): Uri? {
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
                        // Call the function to upload the image to Firebase Storage
                        uploadImageToFirebaseStorage(pages[0].imageUri, storageRef, onScanSuccess, onScanError)
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

    private fun uploadImageToFirebaseStorage(
        imageUri: Uri,
        storageRef: StorageReference,
        onUploadSuccess: (Uri) -> Unit,
        onUploadError: (String) -> Unit
    ) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val fileRef = storageRef.child("scanned_documents/$fileName")

        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    onUploadSuccess(downloadUrl)
                }.addOnFailureListener { exception ->
                    onUploadError("Failed to get download URL: ${exception.message}")
                }
            }
            .addOnFailureListener { exception ->
                onUploadError("Failed to upload image: ${exception.message}")
            }
    }
}
