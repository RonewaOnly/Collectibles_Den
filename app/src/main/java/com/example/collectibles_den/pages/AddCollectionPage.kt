package com.example.collectibles_den.pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.sharp.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.coreFunction_AddCollection.FileReaderClass
import com.example.collectibles_den.coreFunction_AddCollection.ScannerClass
import com.example.collectibles_den.coreFunction_AddCollection.TakePhotosClass
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.data.NoteData
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.example.collectibles_den.logic.TakePhotosViewModel
import com.example.collectibles_den.collectiblesDenApp

@Preview
@Composable
fun AddCollections(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
        val userID = collectiblesDenApp.getUserID()
        val collectionsState = remember { mutableStateOf<List<MakeCollection>>(emptyList()) }

        Column(
                modifier = Modifier
                        .width(1200.dp)
                        .height(2500.dp)
                        .border(1.dp, Color.Black)
                        .verticalScroll(rememberScrollState(), true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(text = "Add Collection")
                val group = makeCollection(collectionsState.value, viewModel, userID)
                Spacer(modifier = Modifier.padding(16.dp))
                // Displaying stored Collection
                DisplayCollection(bank = group)
        }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun makeCollection(make: List<MakeCollection>, viewModel: DatabaseViewModel, userID: String?): MutableList<MakeCollection> {
        val context = LocalContext.current
        val takePhotosClass = remember { TakePhotosClass() }
        val PhotoViewModel: TakePhotosViewModel = viewModel()

        var mainSwitch by remember { mutableStateOf(false) }
        var isPopClicked by remember { mutableStateOf(false) }
        var isCameraClick by remember { mutableStateOf(false) }
        var isNotesClick by remember { mutableStateOf(false) }
        var isScannerClick by remember { mutableStateOf(false) }
        var isFolderClick by remember { mutableStateOf(false) }
        val listCollection by remember { mutableStateOf(mutableListOf<MakeCollection>()) }
        val noteClass = FileReaderClass()
        val scanClass = ScannerClass()
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        //var imageCameraUri by remember { mutableStateOf<Uri?>(null) }
        var scannedUri by remember { mutableStateOf<Uri?>(null) }
        var notesBank by remember { mutableStateOf<List<NoteData>>(emptyList()) }
        var attachedFileUri by remember { mutableStateOf<Uri?>(null) }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                imageUri = uri
        }
        // Setup camera and permission launchers
        val (cameraLauncher, permissionLauncher) = takePhotosClass.setupCameraLauncher(
                viewModel = PhotoViewModel,
                onImageCaptured = { uri ->
                        Toast.makeText(context, "Image captured: $uri", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
        )
        var imageCameraUri by remember { mutableStateOf<Uri?>(null) }


        IconButton(
                onClick = { mainSwitch = true },
                modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
                        .width(150.dp)
                        .height(60.dp),
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.inversePrimary)
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        Icon(imageVector = Icons.Sharp.OpenInBrowser, contentDescription = null)
                        Text(text = "Make a Collection")
                }
        }

        if (mainSwitch) {
                Column(
                        modifier = Modifier
                                .width(900.dp)
                                .border(1.dp, Color.Black)
                                .background(Color.DarkGray),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(text = "Add Items", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.padding(12.dp))
                        IconButton(
                                onClick = { launcher.launch("image/*") },
                                modifier = Modifier
                                        .width(200.dp)
                                        .border(1.dp, Color.Transparent, RectangleShape),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                ) {
                                        Icon(imageVector = Icons.Filled.Image, contentDescription = null)
                                        Text(text = "Get Image")
                                }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        IconButton(
                                onClick = {
                                        val permissionCheckResult = ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                        )
                                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                                val uri = takePhotosClass.createImageUri(context)
                                                if (uri != null) {
                                                        PhotoViewModel.setImageUri(uri)
                                                        cameraLauncher.launch(uri)
                                                }
                                        } else {
                                                permissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                },
                                modifier = Modifier
                                        .width(200.dp)
                                        .border(1.dp, Color.Transparent, RectangleShape),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                ) {
                                        Icon(imageVector = Icons.Filled.Camera, contentDescription = null)
                                        Text(text = "Take Image")
                                }
                        }

                        Spacer(modifier = Modifier.padding(5.dp))
                        IconButton(
                                onClick = { isNotesClick = true },
                                modifier = Modifier
                                        .width(200.dp)
                                        .border(1.dp, Color.Transparent, RectangleShape),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                ) {
                                        Icon(imageVector = Icons.Filled.NoteAlt, contentDescription = null)
                                        Text(text = "Take Notes")
                                }
                        }
                        if (isNotesClick) {
                                noteClass.NoteForm(onClose = { isNotesClick = false }, onSave = { note ->
                                        notesBank = notesBank + note
                                })
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        IconButton(
                                onClick = {
                                        isScannerClick = true
                                },
                                modifier = Modifier
                                        .width(200.dp)
                                        .border(1.dp, Color.Transparent, RectangleShape),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                ) {
                                        Icon(imageVector = Icons.Filled.Scanner, contentDescription = null)
                                        Text(text = "Scan")
                                }
                        }
                        if (isScannerClick) {
                                scanClass.ScannerDocument(onScanSuccess = { uri ->
                                        scannedUri = uri
                                }, onScanError = { message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                })
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        IconButton(
                                onClick = { isFolderClick = true },
                                modifier = Modifier
                                        .width(200.dp)
                                        .border(1.dp, Color.Transparent, RectangleShape),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                ) {
                                        Icon(imageVector = Icons.Filled.Attachment, contentDescription = null)
                                        Text(text = "Attach File")
                                }
                        }
                        if (isFolderClick) {
                                noteClass.FilePickerAndReader(onFileSelected = { uri ->
                                        attachedFileUri = uri
                                        val uriString = uri.toString()
                                        if (uriString.isNotEmpty()) {
                                                val parsedUri = Uri.parse(uriString)
                                                // Proceed with using `parsedUri`
                                        } else {
                                                // Handle the case where `uriString` is empty
                                        }
                                })
                                attachedFileUri?.let { uri ->
                                        // Use the selected file URI
                                        Text(text = "Selected file URI: $uri")
                                }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        IconButton(
                                onClick = { isPopClicked = true },
                                modifier = Modifier
                                        .width(120.dp)
                                        .border(1.dp, Color.Transparent, RoundedCornerShape(25)),
                                colors = IconButtonDefaults.iconButtonColors(Color.LightGray)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                                        Text(text = "Create Board")
                                }
                        }
// Display captured image
                        val Uri by PhotoViewModel.imageUri.collectAsState()

                        if (isPopClicked) {
                                SaveCollection(
                                        user = userID,
                                        getImage = imageUri,
                                        takeImage = Uri,
                                        notes = notesBank,
                                        scanned = scannedUri,
                                        file = attachedFileUri,
                                        onClose = { isPopClicked = false },
                                        onSave = { collection ->
                                                if (userID != null) {
                                                        viewModel.setCollections(
                                                                collectionName = collection.makeCollectionName,
                                                                collectionDesc = collection.makeCollectionDescription,
                                                                category = collection.makeCollectionCategory,
                                                                images = listOfNotNull(imageUri),
                                                                cameraImages = listOfNotNull(Uri),
                                                                notes = notesBank,
                                                                scannedItems = listOfNotNull(scannedUri),
                                                                files = listOfNotNull(attachedFileUri),
                                                                user = userID
                                                        ) { savedCollection ->
                                                                listCollection.add(savedCollection)
                                                                Toast.makeText(context, "Collection saved", Toast.LENGTH_LONG).show()
                                                                mainSwitch = false
                                                                isPopClicked = false
                                                        }
                                                }
                                        }
                                )
                        }
                }
        }

        return listCollection
}

@Composable
fun DisplayCollection(bank: MutableList<MakeCollection>) {
        if (bank.isNotEmpty()) {
                LazyColumn(
                        modifier = Modifier.height(1500.dp)
                ) {
                        items(bank) { collection ->
                                Column(
                                        modifier = Modifier
                                                .width(550.dp)
                                                .border(1.dp, Color.Black),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        val imageUriString = collection.makeCollectionImages.getOrNull(0)
                                        val scannedUriString = collection.makeCollectionScannedItems.getOrNull(0)
                                        val painter = when {
                                                !imageUriString.isNullOrEmpty() -> rememberAsyncImagePainter(Uri.parse(imageUriString))
                                                !scannedUriString.isNullOrEmpty() -> rememberAsyncImagePainter(Uri.parse(scannedUriString))
                                                else -> rememberAsyncImagePainter(Uri.parse("https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE=")) // or use a placeholder image
                                        }

                                        Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier
                                                        .width(200.dp)
                                                        .height(200.dp),
                                                contentScale = ContentScale.Fit,
                                        )
                                        Text(
                                                text = collection.makeCollectionName,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color.LightGray)
                                                        .height(50.dp)
                                                        .padding(top = 15.dp)
                                        )
                                }
                                Spacer(modifier = Modifier.padding(12.dp))
                        }
                }
        } else {
                Text(text = "No Collections Found, You have not added any collection")
        }
}

@Composable
fun SaveCollection(
        user: String?,
        getImage: Uri? = null,
        takeImage: Uri? = null,
        notes: List<NoteData> = emptyList(),
        scanned: Uri? = null,
        file: Uri? = null,
        onClose: () -> Unit,
        onSave: (MakeCollection) -> Unit
) {
        var collectionName by remember { mutableStateOf("") }
        var collectionDescription by remember { mutableStateOf("") }
        var collectionCategory by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { onClose() }) {
                Surface(
                        modifier = Modifier
                                .width(300.dp)
                                .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                ) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement =  Arrangement.spacedBy(8.dp)
                        ) {
                                TextField(
                                        value = collectionName,
                                        onValueChange = { collectionName = it },
                                        label = { Text(text = "Enter Board Name:") }
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                TextField(
                                        value = collectionDescription,
                                        onValueChange = { collectionDescription = it },
                                        label = { Text(text = "Enter Board Description:") }
                                )
                                Spacer(modifier = Modifier.padding(10.dp))

                                TextField(
                                        value = collectionCategory,
                                        onValueChange = { collectionCategory = it },
                                        label = { Text(text = "Enter Board Category:") }
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
                                                        user?.let {
                                                                MakeCollection(
                                                                        makeCollectionName = collectionName,
                                                                        makeCollectionDescription = collectionDescription,
                                                                        makeCollectionCategory = collectionCategory,
                                                                        makeCollectionFiles = listOfNotNull(file.toString()),
                                                                        makeCollectionImages = listOfNotNull(getImage.toString()),
                                                                        makeCollectionCameraImages = listOfNotNull(takeImage.toString()),
                                                                        makeCollectionNotes = notes.toMutableList(),
                                                                        makeCollectionScannedItems = listOfNotNull(scanned.toString()),
                                                                        userAssigned = it
                                                                )
                                                        }?.let {
                                                                onSave(
                                                                        it
                                                                )
                                                        }
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

