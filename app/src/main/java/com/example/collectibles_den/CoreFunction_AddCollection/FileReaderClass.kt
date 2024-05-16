package com.example.collectibles_den.CoreFunction_AddCollection

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
import androidx.navigation.compose.composable
import com.example.collectibles_den.Data.NoteData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class FileReaderClass {


    @Composable
    fun NoteForm(onClose: () -> Unit,onSave:(NoteData) -> Unit){
        val context = LocalContext.current
        var formTitle by remember {
            mutableStateOf("")
        }
        var formContent by remember {
            mutableStateOf("")
        }

        Dialog(onDismissRequest = { onClose }) {
            Surface(
                modifier = Modifier
                    .width(500.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column {
                    TextField(
                        value = formTitle,
                        onValueChange = {formTitle = it},
                        minLines = 1,
                        maxLines = 2
                    )//Title of the note
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextField(
                        value = formContent,
                        onValueChange = {formContent = it},
                        minLines = 30,
                        maxLines = 90
                    )//Content of the note


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
                                WriteToFile(context,formTitle,formContent)//Writes
                                onSave(// saves for dummy for now
                                    NoteData(
                                        formTitle,
                                        formContent
                                    )
                                )
                                onClose()
                            }
                        ) {
                            Text("Save :)")
                        }
                    }

                }
            }

        }

    }
    fun WriteToFile(context: Context, title:String, content: String){//This writes and creates the file
        val file = File(context.filesDir, title)
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
            outputStreamWriter.use {
                it.write(content)
            }
            Toast.makeText(context,"Saved Successfully", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.cause

        }

    }

    fun ReadFroMFile(){

    }


    @Composable
    fun FilePickerAndReader(onFileSelected: (Uri) -> Unit) {
        var fileUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current

        // Launcher for file picker
        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                fileUri = uri
                onFileSelected(uri)
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

}