package com.example.collectibles_den.Pages

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.Data.Storyboard_Stories
import com.example.collectibles_den.Data.collectionStorage
import com.example.collectibles_den.DefaultValuesClass
import com.example.collectibles_den.R

@Composable
fun  Storyboard() {
    Column {
        //Text(text = "Storyboard.")
        MaxSection()
    }
}

@Preview
@Composable
fun MaxSection() {
    val context = LocalContext.current
    var isPopVisible by remember { mutableStateOf(false) }
    var isExpended by remember { mutableStateOf(false) }
    val selectedCollection = remember { mutableStateListOf<Storyboard_Stories.StoryboardLine>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { isPopVisible = true }) {
            Text(text = "Create your storyboard")
        }
        if (isPopVisible) {
            CustomPopup(
                onClose = { isPopVisible = false },
                onSave = { storyboard ->
                    selectedCollection.add(//This will be storing the storyboard created
                        Storyboard_Stories.StoryboardLine(
                           storyName =  storyboard.storyName, 
                            storyDescription = storyboard.storyDescription,
                           storyItems =  storyboard.storyItems,
                           storyCategory =  storyboard.storyCategory,
                           storyCovers =  storyboard.storyCovers
                        )
                    )
                    Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    HorizontalDivider(modifier = Modifier.padding(10.dp))

    if (selectedCollection.isNotEmpty()) {
        LazyColumn {
            items(selectedCollection) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val imagePainter = if (item.storyCovers.isNotEmpty() && item.storyCovers[0] != null) {
                        rememberAsyncImagePainter(item.storyCovers[0])
                    } else {
                        painterResource(R.drawable.default_image)
                    }
                    if(isExpended){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(onClick = {
                                isExpended = false
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                            }
                            IconButton(onClick = {
                                selectedCollection.remove(item)
                                isExpended = false
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                            IconButton(onClick = {
                                isExpended = false
                            }) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null)
                            }
                            IconButton(onClick = { isPopVisible = true },
                                modifier = Modifier.width(140.dp)
                                ) {
                                Column {
                                    Icon(imageVector = Icons.Default.BorderAll, contentDescription = null)
                                    Text(text = "Set Goal")
                                }
                            }
                        }
                        if (isPopVisible){
                            SetGoal(
                                story = selectedCollection,
                                storyID = item.storyID,
                                onClose = { isPopVisible = false },
                                onSave = { updatedItem ->
                                    val index = selectedCollection.indexOfFirst { it.storyID == updatedItem.storyID }
                                    if (index != -1) {
                                        selectedCollection[index] = updatedItem
                                    }
                                })
                        }
                    }else{
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            IconButton(onClick = {
                                isExpended = true
                            }) {
                                Icon(painterResource(R.drawable.default_showbutton), contentDescription = null)
                            }
                        }
                    }
                    Image(
                        painter = imagePainter,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp)
                    )
                    Text(text = item.storyName,modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.LightGray))

                }
            }
        }
    } else {
        Text(
            text = "No storyboard found",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
    }
}
@Composable
fun CustomPopup(
    onClose: () -> Unit,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    var storyName by remember { mutableStateOf("") }
    var storyDescription by  remember { mutableStateOf("") }
    var storyCategory by remember { mutableStateOf("") }
    var coverBook by remember { mutableStateOf<List<Uri?>>(emptyList()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the uri for the image chosen from the device gallery
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri //this set the uri to the imageUri
    }
    val getCollection = DefaultValuesClass()
    var selecteItems by remember {
        mutableStateOf<List<collectionStorage?>>(emptyList())
    }
    Dialog(onDismissRequest = onClose) {
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
                    value = storyName,
                    onValueChange = { storyName = it },
                    label = { Text("Enter storyboardName: ") }
                )
                TextField(
                    value = storyDescription,
                    onValueChange = { storyDescription = it },
                    label = { Text("Enter storyboard Description: ") }
                )
                TextField(
                    value = storyCategory,
                    onValueChange = {storyCategory = it},
                    label = { Text(text = "Enter category name: ")}
                )
                selecteItems = SelectedItem(collection = getCollection.recentCollection)//This will be getting the selected collection
                Row(
                    modifier = Modifier.width(200.dp)
                ) {
                    TextButton(onClick = {
                        launcher.launch("image/*")
                        if(imageUri != null){
                            coverBook = listOf(imageUri)
                        }
                    }) {
                       Icon(imageVector = Icons.Default.FileOpen, contentDescription = "")
                       Text(text = "Select image")
                    }
                }
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
                            onSave(//It will be saving the data entered
                                Storyboard_Stories.StoryboardLine(
                                    storyName = storyName,
                                    storyItems = selecteItems,
                                    storyCategory = storyCategory,
                                    storyDescription = storyDescription,
                                    storyCovers = coverBook
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
fun SelectedItem(collection: List<collectionStorage>): List<collectionStorage?> {
    val isSelected by remember { mutableStateOf(false) }
    val selectedCollection = remember { mutableStateListOf<collectionStorage>() }

    Column {
        collection.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item in selectedCollection,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedCollection.add(item)
                        } else {
                            selectedCollection.remove(item)
                        }
                    }
                )
                // Display the name of the collection
                Text(text = item.collectionName)
            }
        }
    }
    // Return the list of selected collections
    return selectedCollection.toList()
}
@Composable
fun SetGoal(
    story: List<Storyboard_Stories.StoryboardLine>,
    storyID: String,
    onClose: () -> Unit,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    var goalSet by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column {
                story.forEach { item ->
                    if (item.storyID == storyID) {
                        TextField(
                            value = goalSet.toString(),
                            onValueChange = { goalSet = it.toInt() },
                            label = { Text("Set goal: ") }
                        )
                        Button(onClick = {
                            onSave(item.copy(goalSet = goalSet))
                            onClose()
                        }) {
                            Text(text = "Set Goal")
                        }
                    } else {
                        Text(text = "Can't Set a Goal")
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onClose) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}