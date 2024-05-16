package com.example.collectibles_den.Pages

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.CoreFunction_AddCollection.FileReaderClass
import com.example.collectibles_den.CoreFunction_AddCollection.ScannerClass
import com.example.collectibles_den.CoreFunction_AddCollection.TakePhotosClass
import com.example.collectibles_den.Data.MakeCollection
import com.example.collectibles_den.Data.NoteData
import com.example.collectibles_den.R

@Composable
fun AddCollections() {
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
               val group =  MakeCollection()
                Spacer(modifier = Modifier.padding(16.dp))
                //Displaying stored Collection
                DisplayCollection(bank = group)
        }
}

@Composable
fun MakeCollection():MutableList<MakeCollection> {
        val context = LocalContext.current
        var mainSwitch by remember { mutableStateOf(false) }
        var isPopClicked by remember { mutableStateOf(false) }
        var isCameraClick by remember {
                mutableStateOf(false)
        }
        var isNotesClick by remember {
                mutableStateOf(false)
        }
        var isScannerClick by remember {
                mutableStateOf(false)
        }
        var isFolderClick by remember {
                mutableStateOf(false)
        }
        val listCollection by remember {
                mutableStateOf(mutableListOf<MakeCollection>())
        }
        val noteClass = FileReaderClass()
        val scanClass = ScannerClass()
        var imageUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the Uri of the selected Image
        var imageCameraUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the Uri of the  Image taken by camera
        var scannedUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the Uri of the scanned Image
        var notesBank by remember {//Storing the note taken
                mutableStateOf<List<NoteData>>(emptyList())
        }
        var attachedFileUri by remember { mutableStateOf<Uri?>(null) }//Will be storing the Uri of the file chosen
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                imageUri = uri
        }

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

        if (mainSwitch){
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
                                onClick = { isCameraClick = true },
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
                        if (isCameraClick) {
                                imageCameraUri = CapturingImages()
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
                        if (isNotesClick){
                                noteClass.NoteForm( onClose = { isNotesClick = false }, onSave = {note ->
                                        //There will be an List that will be storing notes
                                        notesBank = listOf(note)
                                })

                        }
                        Spacer(modifier = Modifier.padding(5.dp))

                        IconButton(
                                onClick = { isScannerClick = true },
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
                        if (isScannerClick){
                                scannedUri = scanClass.scannerDocument()//this return a Uri
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
                                        Icon(
                                                imageVector = Icons.Filled.Attachment,
                                                contentDescription = null
                                        )
                                        Text(text = "Attach File")
                                }
                        }
                        if (isFolderClick){
                                noteClass.FilePickerAndReader{ uri ->
                                        // Handle the selected file URI here
                                        // For example, you can use it to display or process the file
                                        attachedFileUri = uri
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
                                ) {
                                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                                        Text(text = "Create Board")
                                }
                        }

                        if (isPopClicked) {
                                SaveCollection(
                                        getImage = imageUri,
                                        takeImage = imageCameraUri,
                                        notes = notesBank,
                                        scanned = scannedUri,
                                        file = attachedFileUri,
                                        onClose = { isPopClicked = false },
                                        onSave = {
                                                if(imageUri != null){
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionImages = it.makeCollectionImages
                                                                )
                                                        )
                                                        Toast.makeText(context,"${listCollection[0]}",Toast.LENGTH_LONG).show()
                                                        mainSwitch = false

                                                }else if(imageCameraUri != null){
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionCameraImages = it.makeCollectionCameraImages
                                                                )
                                                        )
                                                        Toast.makeText(context,"${listCollection[0]}",Toast.LENGTH_LONG).show()
                                                        mainSwitch = false

                                                }else if(notesBank.isNotEmpty()){
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionNotes = notesBank
                                                                )
                                                        )
                                                        Toast.makeText(context,"${listCollection[0]}",Toast.LENGTH_LONG).show()
                                                        mainSwitch = false

                                                }else if(scannedUri != null){
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionScannedItems = it.makeCollectionScannedItems
                                                                )
                                                        )
                                                        mainSwitch = false

                                                }else if(attachedFileUri != null){
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionFiles = it.makeCollectionFiles
                                                                )
                                                        )
                                                        Toast.makeText(context,"${listCollection[0]}",Toast.LENGTH_LONG).show()
                                                        mainSwitch = false

                                                }else{//Saves everything
                                                        listCollection.add(
                                                                MakeCollection(
                                                                        makeCollectionName = it.makeCollectionName,
                                                                        makeCollectionCategory = it.makeCollectionCategory,
                                                                        makeCollectionImages = it.makeCollectionImages,
                                                                        makeCollectionCameraImages = it.makeCollectionCameraImages,
                                                                        makeCollectionNotes = it.makeCollectionNotes,
                                                                        makeCollectionScannedItems = it.makeCollectionScannedItems,
                                                                        makeCollectionFiles = it.makeCollectionFiles,

                                                                        )
                                                        )
                                                        Toast.makeText(context,"${listCollection[0]}",Toast.LENGTH_LONG).show()
                                                        mainSwitch = false
                                                }

                                        }
                                )
                        }
                }
        }
        return listCollection
}
@Composable
fun DisplayCollection(bank: MutableList<MakeCollection>){
        
        if (bank.isNotEmpty()){
                LazyColumn(
                        modifier = Modifier.height(1500.dp)
                ) {
                        items(bank){teller ->
                                Column(
                                        modifier = Modifier
                                                .width(550.dp)
                                                .border(1.dp, Color.Black),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Image(
                                                painter = 
                                                if(teller.makeCollectionImages[0] != null) rememberAsyncImagePainter(teller.makeCollectionImages[0]) 
                                                else painterResource(R.drawable.default_image), 
                                                contentDescription = null,
                                                modifier = Modifier
                                                        .width(200.dp)
                                                        .height(200.dp), // Ensure the image has a specific height
                                                contentScale = ContentScale.Fit,
                                        )
                                        Text(
                                                text = teller.makeCollectionName, 
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
        }else{
                Text(text = "No Collections Found , You have not added any collection")
        }
}
@Composable
fun SaveCollection(
        getImage: Uri? = Uri.EMPTY,
        takeImage: Uri? = Uri.EMPTY,
        notes: List<NoteData> = emptyList(),
        scanned: Uri? = Uri.EMPTY,
        file: Uri? = null,
        onClose: () -> Unit,
        onSave: (MakeCollection) -> Unit
) {
        var collectionName by remember { mutableStateOf("") }
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
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                TextField(
                                        value = collectionName,
                                        onValueChange = { collectionName = it },
                                        label = { Text(text = "Enter Board Name:") }
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
                                                        onSave(
                                                                MakeCollection(
                                                                        makeCollectionName = collectionName,
                                                                        makeCollectionCategory = collectionCategory,
                                                                        makeCollectionFiles = listOf(file),
                                                                        makeCollectionImages =  listOf(getImage) ,
                                                                        makeCollectionCameraImages = listOf(takeImage),
                                                                        makeCollectionNotes = notes,
                                                                        makeCollectionScannedItems = listOf(scanned)
                                                                )
                                                        )
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
@Composable
fun CapturingImages():Uri {
        val context = LocalContext.current
        var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

        val get = TakePhotosClass()

        val (cameraLauncher, permissionLauncher) = get.setupCameraLauncher(
                context = context,
                onImageCaptured = { uri -> capturedImageUri = uri },
                onError = { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
        )

        LaunchedEffect(Unit) {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(capturedImageUri)
                } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                }
        }
        return capturedImageUri
}

