package com.example.collectibles_den.coreFunction_AddCollection

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.collectibles_den.data.NoteData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class FileReaderClass {

    @Composable
    fun NoteForm(onClose: () -> Unit, onSave: (NoteData) -> Unit) {
        val context = LocalContext.current
        var formTitle by remember { mutableStateOf("") }
        var formContent by remember { mutableStateOf("") }

        Dialog(onDismissRequest = onClose) {
            Surface(
                modifier = Modifier
                    .width(500.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column {
                    TextField(
                        value = formTitle,
                        onValueChange = { formTitle = it },
                        minLines = 1,
                        maxLines = 2,
                        label = { Text("Title") }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextField(
                        value = formContent,
                        onValueChange = { formContent = it },
                        minLines = 30,
                        maxLines = 90,
                        label = { Text("Content") }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClose) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                WriteToFileAndUpload(context, formTitle, formContent)
                                onSave(NoteData(formTitle, formContent))
                                onClose()
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    private fun WriteToFileAndUpload(context: Context, title: String, content: String) {
        val file = File(context.filesDir, "$title.txt")
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
            outputStreamWriter.use {
                it.write(content)
            }
            uploadFileToFirebase(file, title, context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadFileToFirebase(file: File, title: String, context: Context) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("Notes/$title.txt")
        val uri = Uri.fromFile(file)
        storageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(context, "File Uploaded Successfully", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "File Upload Failed", Toast.LENGTH_LONG).show()
            }
    }

    @Composable
    fun FilePickerAndReader(onFileSelected: (Uri) -> Unit) {
        var fileUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current

        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                uploadAttachedFileToFirebase(it, context) { downloadUri ->
                    // Set the fileUri to the download URL returned from Firebase
                    fileUri = downloadUri
                    // Call the onFileSelected callback with the download URL
                    onFileSelected(downloadUri)
                }
            }
        }

        Column {
            Button(onClick = { filePickerLauncher.launch("*/*") }) {
                Text("Select a File")
            }
            fileUri?.let { uri ->
                Text(text = "File selected: $uri")
            }
        }
    }

    private fun uploadAttachedFileToFirebase(uri: Uri, context: Context, onComplete: (Uri) -> Unit) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("Files/${uri.lastPathSegment}")

        storageRef.putFile(uri)
            .addOnSuccessListener { _ ->
                // Get the download URL of the uploaded file
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Call onComplete with the download URL
                    onComplete(downloadUri)
                    Toast.makeText(context, "File Uploaded Successfully $downloadUri", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to get download URL", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "File Upload Failed", Toast.LENGTH_LONG).show()
            }
    }

}
