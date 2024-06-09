package com.example.collectibles_den.coreFunction_AddCollection

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

@Composable
fun UploadImages(filePickerLauncher: ActivityResultLauncher<String>,selectedFileUri:Uri? ,onUploadSuccess: (String) -> Unit, storageRef: StorageReference ) {
    val databaseRef = FirebaseDatabase.getInstance().getReference("uploads")
    //var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf("") }
    var uploadedImageUrl by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        selectedFileUri?.let { uri ->
            Button(onClick = {
                val fileName = uri.lastPathSegment ?: "unknown"
                val fileRef = storageRef.child("uploads/$fileName")

                // Upload image to Firebase Storage
                val uploadTask = fileRef.putFile(uri)

                // Handle upload success
                uploadTask.addOnSuccessListener { _ ->
                    // Get the download URL of the uploaded image
                    fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Save the download URL to Firebase Realtime Database
                        val fileId = databaseRef.push().key // Generate a unique key for the file
                        fileId?.let {
                            val fileData = mapOf(
                                "fileName" to fileName,
                                "downloadUrl" to downloadUrl.toString()
                            )
                            databaseRef.child(it).setValue(fileData)

                            // Update UI
                            uploadStatus = "Upload successful! URL: $downloadUrl"
                            uploadedImageUrl = downloadUrl.toString()
                            onUploadSuccess(downloadUrl.toString()) // Call the callback with the URL
                        }
                    }
                }.addOnFailureListener { exception ->
                    // Handle upload failure
                    uploadStatus = "Upload failed: ${exception.message}"
                }
            }) {
                Text(text = "Upload Image")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}