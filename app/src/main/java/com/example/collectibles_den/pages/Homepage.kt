@file:Suppress("KotlinConstantConditions")

package com.example.collectibles_den.pages

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.Extras.DocumentViewer
import com.example.collectibles_den.Extras.MusicPlayer
import com.example.collectibles_den.Extras.VideoPlayer
import com.example.collectibles_den.Extras.ViewImage
import com.example.collectibles_den.R
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Calendar
import java.util.Locale

@Preview
@Composable
fun Homepage(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
    val context = LocalContext.current
    val userID = CollectiblesDenApp.getUserID()
    var collectionsState by remember { mutableStateOf<List<MakeCollection>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    @Suppress("ControlFlowWithEmptyBody") val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                // Handle permission not granted case
            }
        }

    LaunchedEffect(userID) {
        userID?.let { uid ->
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.getCollections(uid) { collections ->
                    collectionsState = collections
                }
            }
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    Column(
        modifier = Modifier
           .verticalScroll(rememberScrollState(), true)
           .background(
              brush = Brush.linearGradient(
                 colors = listOf(
                    colorResource(id = R.color.g_1), colorResource(
                       id = R.color.g_2
                    ), colorResource(id = R.color.g_3)
                 ),
                 start = Offset(0f, 0f),
                 end = Offset(1000f, 1000f)
              )
           )
    ) {
        Text(
            text = "Recent Additions",
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
               .fillMaxWidth()
               .height(50.dp)
               .background(Color.Transparent)
               .padding(15.dp)
        )
        MainSection(collectionsState)

        Text(
            text = "Past Collection",
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
               .fillMaxWidth()
               .height(50.dp)
               .padding(15.dp)
               .background(Color.Transparent)
        )
        PastSection(collectionsState)
    }
}

@Composable
fun MainSection(recentCollection: List<MakeCollection>) {
    val currentMonthItems = recentCollection.filter { item ->
        convertServerTimestampToTimestamp(item.makeCollectionDate)?.let { timestamp ->
            timestamp >= getBeginningOfPastMonth()
        } ?: false
    }

    if (currentMonthItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.height(3000.dp)
        ) {
            items(currentMonthItems) { collect ->
                CollectionItem(collect)
            }
        }
    } else {
        Text(
            text = "No Records Found :)",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
               .fillMaxWidth()
               .background(Color.Transparent)
        )
    }
}

@Composable
fun PastSection(lateCollection: List<MakeCollection>) {
    val pastMonthItems = lateCollection.filter { item ->
        convertServerTimestampToTimestamp(item.makeCollectionDate)?.let { timestamp ->
            timestamp < getBeginningOfPastMonth()
        } ?: false
    }

    if (pastMonthItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.height(3000.dp)
        ) {
            items(pastMonthItems) { collect ->
                CollectionItem(collect)
            }
        }
    } else {
        Text(
            text = "No Records Found :)",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
               .fillMaxWidth()
               .background(Color.Transparent)
        )
    }
}

@Composable
fun CollectionItem(collect: MakeCollection) {
    val collectionId = remember { mutableStateOf("") }
    val toggle = remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
           .fillMaxWidth()
           .padding(8.dp)
           .border(1.dp, Color.Black)
           .clickable {
              collectionId.value = collect.makeCollectionID
              toggle.value = true
           },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = collect.makeCollectionCover
            ),
            contentDescription = null,
            modifier = Modifier
               .size(100.dp)
               .border(1.dp, color = Color.Red, shape = RectangleShape)
               .padding(8.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = collect.makeCollectionName.uppercase())
            Text(text = collect.makeCollectionDescription.ifEmpty { "No description" })
            Text(text = "${collect.makeCollectionDate}")
        }
    }

    if (collectionId.value != "" && toggle.value) {
        CollectionPopUp(collect = collect, collectionID = collectionId.value) {
            toggle.value = false
        }
    }
}

@Composable
fun CollectionPopUp(collect: MakeCollection, collectionID: String, onClose: () -> Unit) {
    val find = collect.makeCollectionID == collectionID
    @Suppress("UNUSED_VARIABLE") val context = LocalContext.current
    @Suppress("UNUSED_VARIABLE") var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    var imageBtnToggle by remember { mutableStateOf(false) }
    var cameraBtnToggle by remember { mutableStateOf(false) }
    var filesBtnToggle by remember { mutableStateOf(false) }
    var scannedBtnToggle by remember { mutableStateOf(false) }
    var noteBtnToggle by remember { mutableStateOf(false) }



    if (find) {
        Dialog(onDismissRequest = { onClose() }) {
            Surface(
                modifier = Modifier.width(450.dp),
                shape = RectangleShape
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                collect.makeCollectionCover
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp),
                            contentScale = ContentScale.Crop
                        )
                        @Suppress("DEPRECATION")
                        Text(
                            text = collect.makeCollectionName.toUpperCase(Locale.ROOT),
                            fontSize = 35.sp
                        )
                    }
                    HorizontalDivider(modifier = Modifier.width(350.dp))

                    Text(text = "Description: ${collect.makeCollectionDescription}")
                    val values = mutableListOf<String>()

                    // Check the lists and update values
                    for (search in listOf(collect)) {
                        if (search.makeCollectionCameraImages.isNotEmpty()) {
                            values += "Camera Images"
                        }
                        if (search.makeCollectionImages.isNotEmpty()) {
                            values += "Images"
                        }
                        if (search.makeCollectionScannedItems.isNotEmpty()) {
                            values += "Scanned Images"
                        }
                        if (search.makeCollectionFiles.isNotEmpty()) {
                            values += "Files"
                        }
                        if (search.makeCollectionNotes.isNotEmpty()) {
                            values += "Notes"
                        }
                        break
                    }

                    // Display the content
                    Row {
                        values.forEach { value ->
                            TextButton(
                                onClick = {
                                    when (value) {
                                        "Camera Images" -> {
                                            cameraBtnToggle = !cameraBtnToggle
                                        }

                                        "Images" -> {
                                            imageBtnToggle = !imageBtnToggle
                                        }

                                        "Files" -> {
                                            filesBtnToggle = !filesBtnToggle
                                        }

                                        "Scanned Images" -> {
                                            scannedBtnToggle = !scannedBtnToggle
                                        }

                                        "Notes" -> {
                                            noteBtnToggle = !noteBtnToggle
                                        }
                                    }
                                },
                                modifier = Modifier
                                   .border(
                                      2.dp, Color.Gray,
                                      RoundedCornerShape(15.dp)
                                   )
                                   .height(35.dp)
                            ) {
                                Text(text = value, fontSize = 12.sp, textAlign = TextAlign.Center)
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(2.dp))

                    // Display the corresponding text based on toggles
                    if (imageBtnToggle && find) {
                        Text(text = "Image")
                        //Image(painter = rememberAsyncImagePainter(collect.makeCollectionImages[0]), contentDescription = null )
                        ViewContent(link = collect.makeCollectionImages[0])

                    }
                    if (cameraBtnToggle) {
                        Text(text = "Camera")
                        ViewContent(link = collect.makeCollectionCameraImages[0])

                    }
                    if (filesBtnToggle) {
                        Text(text = "Files")
                        ViewContent(link = collect.makeCollectionFiles[0])

                    }
                    if (scannedBtnToggle) {
                        Text(text = "Scanned Images")
                        //Image(painter = rememberAsyncImagePainter(Uri.parse(collect.makeCollectionScannedItems[0])), contentDescription = "")
                        Text(text = "Key ${collect.makeCollectionCover}")
                        ViewContent(link = collect.makeCollectionScannedItems[0])


                    }
                    if (noteBtnToggle) {
                        Text(text = "Notes")
                        ViewContent(link = collect.makeCollectionNotes[0])
                    }
                }
            }
        }
    }
}


@Composable
fun ViewContent(link: String) {
    var fileType by remember { mutableStateOf("loading") }

    LaunchedEffect(link) {
        checkDocumentType(link) { result ->
            fileType = result
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
           .fillMaxWidth()
           .height(350.dp)
           .verticalScroll(rememberScrollState(), true)
    ) {
        when (fileType) {
            "image" -> ViewImage(link)
            "audio" -> MusicPlayer(link)
            "video" -> VideoPlayer(link)
            "text" -> DocumentViewer(link)
            "application" -> DocumentViewer(link)
            else -> {
                Image(
                    painter = painterResource(R.drawable.ic_unknown),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Unknown file type")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = getFileName(link))
    }
}

// Function to extract file name from link
private fun getFileName(link: String): String {
    // Split the URL string by '/'
    val segments = link.split("/")

    // Get the last segment, which should represent the file name
    val lastSegment = segments.lastOrNull()

    // If the last segment is null or empty, return an empty string
    if (lastSegment.isNullOrEmpty()) {
        return ""
    }

    // If the last segment contains a query parameter, remove it
    val fileName = if (lastSegment.contains("?")) {
        lastSegment.substringBefore("?")
    } else {
        lastSegment
    }

    return fileName
}


//Function to get the MIME type of a file stored in Firebase Storage
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
fun getMimeType(storageRef: StorageReference, onResult: (String?) -> Unit) {
    storageRef.metadata
        .addOnSuccessListener { metadata: StorageMetadata ->
            val mimeType = metadata.contentType
            onResult(mimeType)
        }
        .addOnFailureListener { exception ->
            onResult(null)
        }
}

// Example usage
fun checkDocumentType(uri: String, onResult: (String) -> Unit) {
    // Assuming uri is a valid Firebase Storage URI
    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri)
    getMimeType(storageRef) { mimeType ->
        val fileType = when {
            mimeType == null -> "unknown"
            mimeType.startsWith("image/") -> "image"
            mimeType.startsWith("video/") -> "video"
            mimeType.startsWith("audio/") -> "audio"
            mimeType.startsWith("text/") -> "text"
            mimeType.startsWith("application/") -> "application"
            else -> "unknown"
        }
        onResult(fileType)
    }
}


fun getBeginningOfPastMonth(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return Timestamp(calendar.timeInMillis)
}

fun convertServerTimestampToTimestamp(serverTimestamp: Any): Timestamp? {
    return when (serverTimestamp) {
        is Long -> Timestamp(serverTimestamp)
        is HashMap<*, *> -> {
            val timestamp = serverTimestamp["timestamp"] as? Long
            timestamp?.let { Timestamp(it) }
        }

        else -> null
    }
}

