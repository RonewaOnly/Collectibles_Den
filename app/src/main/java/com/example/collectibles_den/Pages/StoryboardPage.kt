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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.Data.MakeCollection
import com.example.collectibles_den.Data.Storyboard_Stories
import com.example.collectibles_den.Logic.DatabaseViewModel
import com.example.collectibles_den.Logic.DatabaseViewModelFactory
import com.example.collectibles_den.R
import com.example.collectibles_den.collectiblesDenApp

@Composable
fun Storyboard(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
    val userID = collectiblesDenApp.getUserID()
    val collectionsState = remember { mutableStateOf<List<Storyboard_Stories.StoryboardLine>>(emptyList()) }
    // Load data from Firebase
    LaunchedEffect(Unit) {
        userID?.let {id ->
            viewModel.getStoryboard(id){storyboardLines ->
                collectionsState.value = storyboardLines

            }

        }
    }
    Column {
        MaxSection(collectionsState.value, viewModel, userID)
    }
}

@Composable
fun MaxSection(
    stories: List<Storyboard_Stories.StoryboardLine>,
    viewModel: DatabaseViewModel,
    userID: String?
) {
    val context = LocalContext.current
    var isPopVisible by remember { mutableStateOf(false) }
    val selectedCollection = remember { mutableStateListOf<Storyboard_Stories.StoryboardLine>() }
    val toggleStates = remember { mutableStateMapOf<String, Boolean>() }

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
                userID,
                onSave = { storyboard ->
                    try {
                        viewModel.setStoryboard(
                            storyboardLine = storyboard,
                            onSave = { newStoryboard ->
                                selectedCollection.add(newStoryboard)
                            }
                        )
                        Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error saving storyboard: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    HorizontalDivider(modifier = Modifier.padding(10.dp))

    LazyColumn {
        items(if (selectedCollection.isNotEmpty()) selectedCollection else stories) { item ->
            StoryboardItem(item, selectedCollection, toggleStates,viewModel, userID)
        }

        if (stories.isEmpty() && selectedCollection.isEmpty()) {
            item {
                Text(
                    text = "No storyboard found",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
            }
        }
    }
}



@Composable
fun CustomPopup(
    onClose: () -> Unit,
    userID: String?,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    val context = LocalContext.current
    var storyName by remember { mutableStateOf("") }
    var storyDescription by remember { mutableStateOf("") }
    var storyCategory by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    var selectedItems by remember { mutableStateOf<List<MakeCollection?>>(emptyList()) }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = storyName,
                    onValueChange = { storyName = it },
                    label = { Text("Enter storyboard name: ") }
                )
                TextField(
                    value = storyDescription,
                    onValueChange = { storyDescription = it },
                    label = { Text("Enter storyboard description: ") }
                )
                TextField(
                    value = storyCategory,
                    onValueChange = { storyCategory = it },
                    label = { Text("Enter category name: ") }
                )

                Row(modifier = Modifier.width(200.dp)) {
                    TextButton(onClick = {
                        launcher.launch("image/*")
                    }) {
                        Icon(imageVector = Icons.Default.FileOpen, contentDescription = null)
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
                            try {
                                userID?.let {
                                    Storyboard_Stories.StoryboardLine(
                                        storyName = storyName,
                                        storyItems = selectedItems,
                                        storyCategory = storyCategory,
                                        storyDescription = storyDescription,
                                        storyCovers = if (imageUri != null) listOf(imageUri.toString()) else emptyList(),
                                        showGoalDialog = false,
                                        goalSet = 0,
                                        currentProgress = 0,
                                        user = it
                                    )
                                }?.let {
                                    onSave(
                                        it
                                    )
                                }
                                onClose()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error saving storyboard: ${e.message}", Toast.LENGTH_LONG).show()
                            }
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
fun StoryboardItem(
    item: Storyboard_Stories.StoryboardLine,
    selectedCollection: SnapshotStateList<Storyboard_Stories.StoryboardLine>,
    toggleStates: SnapshotStateMap<String, Boolean>,
    viewModel: DatabaseViewModel,
    userID: String?
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

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

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { isExpanded = false }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = {
                    try {
                        selectedCollection.remove(item)
                        if (userID != null) {
                            viewModel.deleteStoryboard(userID, item.storyID,
                                onSuccess = {
                                    // Handle success if needed
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, "Error deleting item: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                        isExpanded = false
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error deleting item: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                IconButton(onClick = { isExpanded = false }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        toggleStates[item.storyID] = toggleStates[item.storyID]?.not() ?: true
                    },
                    modifier = Modifier.width(140.dp)
                ) {
                    Column {
                        Icon(imageVector = Icons.Default.BorderAll, contentDescription = null)
                        Text(text = "Set Goal")
                    }
                }
            }

            if (toggleStates[item.storyID] == true) {
                SetGoal(
                    story = selectedCollection,
                    storyID = item.storyID,
                    toggle = toggleStates[item.storyID] ?: false,
                    onClose = { toggleStates[item.storyID] = false },
                    onSave = { updatedItem ->
                        try {
                            val index = selectedCollection.indexOfFirst { it.storyID == updatedItem.storyID }
                            if (index != -1) {
                                selectedCollection[index] = updatedItem
                                if (userID != null) {
                                    viewModel.updateStoryboard(userID, updatedItem,
                                        onSuccess = {
                                            // Handle success if needed
                                        },
                                        onError = { errorMessage ->
                                            Toast.makeText(context, "Error updating item: $errorMessage", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error setting goal: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { isExpanded = true }) {
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
        Text(text = item.storyName, modifier = Modifier
            .fillMaxHeight()
            .background(Color.LightGray))

        if (item.goalSet == 0) {
            // Display nothing
        } else {
            // Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Progress towards goal")
                LinearProgressIndicator(
                    progress = { item.getProgress() },
                )
                Text(text = "${item.currentProgress} / ${item.goalSet}")
            }
        }
    }
    Spacer(modifier = Modifier.padding(12.dp))
}


@Composable
fun SetGoal(
    story: List<Storyboard_Stories.StoryboardLine>,
    storyID: String,
    toggle: Boolean = false,
    onClose: () -> Unit,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    val context = LocalContext.current
    var goalSet by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                story.forEach { item ->

                    if (item.storyID == storyID && toggle) {
                        TextField(
                            value = goalSet.toString(),
                            onValueChange = {
                                try {
                                    goalSet = it.toInt()
                                } catch (e: NumberFormatException) {
                                    Toast.makeText(context, "Invalid input: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            },
                            label = { Text("Set goal: ") }
                        )
                        Button(onClick = {
                            try {
                                onSave(item.copy(goalSet = goalSet))
                                Toast.makeText(context, item.storyName, Toast.LENGTH_LONG).show()
                                onClose()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error setting goal: ${e.message}", Toast.LENGTH_LONG).show()
                            }
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

