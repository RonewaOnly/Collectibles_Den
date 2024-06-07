package com.example.collectibles_den.pages

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.R
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.data.Storyboard_Stories
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory

@Composable
fun Storyboard(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
    val userID = CollectiblesDenApp.getUserID()
    val collectionsState = remember { mutableStateOf<List<Storyboard_Stories.StoryboardLine>>(emptyList()) }
    val getCollectionsState = remember { mutableStateOf<List<MakeCollection>>(emptyList()) }

    // Load data from Firebase
    LaunchedEffect(Unit) {
        userID?.let { id ->
            viewModel.getStoryboard(id) { storyboardLines ->
                collectionsState.value = storyboardLines
            }
            viewModel.getCollections(id){
                getCollectionsState.value = it
            }
        }
    }

    Column {
        MaxSection(collectionsState.value, getCollectionsState.value ,viewModel, userID)
    }
}

@Composable
fun MaxSection(
    stories: List<Storyboard_Stories.StoryboardLine>,
    collections: List<MakeCollection>,
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
                collections,
                onClose = { isPopVisible = false },
                userID = userID,
                onSave = { storyboard ->
                    // Check if the storyboard already exists
                    val existingStoryboard = selectedCollection.firstOrNull { it.storyName == storyboard.storyName }
                    if (existingStoryboard != null) {
                        // Update the existing storyboard
                        viewModel.updateStoryboard(existingStoryboard.storyID, storyboard,
                            onSuccess = {
                                selectedCollection[selectedCollection.indexOf(existingStoryboard)] = storyboard
                                Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, "Error updating item: $errorMessage", Toast.LENGTH_LONG).show()
                            }
                        )
                    } else {
                        // Save the new storyboard
                        viewModel.setStoryboard(
                            storyboardLine = storyboard,
                            onSave = { newStoryboard ->
                                selectedCollection.add(newStoryboard)
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                }
            )
        }
    }

    HorizontalDivider(modifier = Modifier.padding(10.dp))

    LazyColumn {
        items(selectedCollection.ifEmpty { stories }) { item ->
            StoryboardItem(item, collections,selectedCollection, toggleStates, viewModel, userID)
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
    collections: List<MakeCollection>,
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
                val relevantCollections = collections.filter { it.makeCollectionCategory == storyCategory }
                relevantCollections.forEach { collection ->
                    Row {
                        Checkbox(
                            checked = selectedItems.any { it?.makeCollectionID == collection.makeCollectionID },
                            onCheckedChange = { checked ->
                                val newSelectedItems = if (checked) {
                                    selectedItems + collection
                                } else {
                                    selectedItems.filterNot { it?.makeCollectionID == collection.makeCollectionID }
                                }
                                selectedItems = newSelectedItems
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Text(text = collection.makeCollectionName)
                    }
                }

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
                                val storyboard = userID?.let {
                                    Storyboard_Stories.StoryboardLine(
                                        storyName = storyName,
                                        storyItems = selectedItems.filterNotNull(),
                                        storyCategory = storyCategory,
                                        storyDescription = storyDescription,
                                        storyCovers = if (imageUri != null) listOf(imageUri.toString()) else listOf("https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="),
                                        user = it
                                    )
                                }
                                if (storyboard != null) {
                                    onSave(storyboard)
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
    collections: List<MakeCollection>,
    selectedCollection: SnapshotStateList<Storyboard_Stories.StoryboardLine>,
    toggleStates: SnapshotStateMap<String, Boolean>,
    viewModel: DatabaseViewModel,
    userID: String?
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    val editView = remember { mutableStateOf(false) }
    val shareView = remember { mutableStateOf(false) }

    val selectedStoryId = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imagePainter = if (item.storyCovers.isNotEmpty()) {
            item.storyCovers[0]
        } else {
            "https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="
        }

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    editView.value = true
                    selectedStoryId.value = item.storyID
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = {
                    try {
                        selectedCollection.remove(item)
                        if (userID != null) {
                            viewModel.deleteStoryboard(item.storyID,
                                onSuccess = {
                                    Toast.makeText(context, "Deleted :) ", Toast.LENGTH_LONG).show()
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
                IconButton(onClick = { shareView.value = true }) {
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
                    viewModel,
                    onClose = { toggleStates[item.storyID] = false },
                    onSave = { updatedItem ->
                        try {
                            val index = selectedCollection.indexOfFirst { it.storyID == updatedItem.storyID }
                            if (index != -1) {
                                selectedCollection[index] = updatedItem
                                if (userID != null) {
                                    viewModel.updateStoryboard(item.storyID, updatedItem,
                                        onSuccess = {
                                            Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                                        },
                                        onError = { errorMessage ->
                                            Toast.makeText(context, "Error updating item: $errorMessage", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Item not found in collection", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error setting goal: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            } else if (editView.value && selectedStoryId.value == item.storyID) {
                EditBoard(
                    storyID = item.storyID,
                    collections,
                    viewModel,
                    storyboardItem = item,
                    onSave = { updatedItem ->
                        try {
                            viewModel.updateStoryboard(item.storyID, updatedItem,
                                onSuccess = {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, "Error updating item: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error updating storyboard: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    },
                    onClose = { editView.value = false }
                )
            } else if (shareView.value && selectedStoryId.value == item.storyID) {
                ShareStoryboard(item, item.storyID)
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
        AsyncImage(
            model = imagePainter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )
        Text(text = item.storyName, modifier = Modifier
            .fillMaxHeight()
            .background(Color.LightGray))

        if (item.goalSet > 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Progress towards goal")
                LinearProgressIndicator(
                    progress = item.getProgress(),
                )
                Text(text = "${item.currentProgress} / ${item.goalSet}")
            }
        }
    }
    Spacer(modifier = Modifier.padding(12.dp))
}


@Composable
fun SetGoal(
    story: SnapshotStateList<Storyboard_Stories.StoryboardLine>,
    storyID: String,
    toggle: Boolean = false,
    viewModel: DatabaseViewModel,
    onClose: () -> Unit,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    val context = LocalContext.current
    var goalSet by remember { mutableStateOf("") }

    // Find the item in the list with the specified storyID
    val item = story.firstOrNull { it.storyID == storyID }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Check if toggle is true and item is not null
                if (toggle && item != null) {
                    if (item.goalSet > 0) {
                        Text(text = "Current goal: ${item.goalSet}")
                    } else {
                        TextField(
                            value = goalSet,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    goalSet = newValue
                                }
                            },
                            label = { Text("Set goal: ") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Button(onClick = {
                            val goalValue = goalSet.toIntOrNull()
                            if (goalValue != null) {
                                try {
                                    viewModel.updateGoalSet(item.storyID, goalValue)
                                    onSave(item.copy(goalSet = goalValue))

                                    Toast.makeText(context, "Goal set for ${item.storyName}", Toast.LENGTH_LONG).show()
                                    onClose()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error setting goal: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(context, "Invalid goal value", Toast.LENGTH_LONG).show()
                            }
                        }, modifier = Modifier.align(Alignment.End)) {
                            Text(text = "Set Goal")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClose) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun EditBoard(
    storyID: String,
    collections: List<MakeCollection>,
    viewModel: DatabaseViewModel,
    storyboardItem: Storyboard_Stories.StoryboardLine,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var newStoryName by remember { mutableStateOf(storyboardItem.storyName) }
    var newStoryDescription by remember { mutableStateOf(storyboardItem.storyDescription) }
    var newStoryCategory by remember { mutableStateOf(storyboardItem.storyCategory) }

    // Initialize selectedItems with items from storyboardItem
    // Assuming storyboardItem.storyItems is a list of MakeCollection
    val selectedItems = remember {
        mutableStateListOf<String>().apply {
            addAll(storyboardItem.storyItems.mapNotNull { it?.makeCollectionName })
        }
    }


    // Filter collections to display only those of the same category and not already selected
    val filteredCollections = collections.filter {
        it.makeCollectionCategory == storyboardItem.storyCategory && !selectedItems.contains(it.makeCollectionName)
    }

    Dialog(onDismissRequest = { onClose() }) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                TextField(
                    value = newStoryName,
                    onValueChange = { newStoryName = it },
                    label = { Text("Storyboard Name") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = newStoryDescription,
                    onValueChange = { newStoryDescription = it },
                    label = { Text("Description") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = newStoryCategory,
                    onValueChange = { newStoryCategory = it },
                    label = { Text("Category") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Display filtered collections
                if (filteredCollections.isNotEmpty()) {
                    Text("Available Collections:")
                    filteredCollections.forEach { collection ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(collection.makeCollectionName),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedItems.add(collection.makeCollectionName)
                                    } else {
                                        selectedItems.remove(collection.makeCollectionName)
                                    }
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = collection.makeCollectionName)
                        }
                    }
                } else {
                    Text("No available collections.")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        val updatedStoryboardItem = storyboardItem.copy(
                            storyName = newStoryName,
                            storyDescription = newStoryDescription,
                            storyCategory = newStoryCategory,
                            storyItems = selectedItems.map { itemName ->
                                collections.find { it.makeCollectionName == itemName }
                            }.filterNotNull() // Remove nulls resulting from find // Update storyItems with selectedItems
                        )
                        viewModel.updateStoryboard(
                            storyboardItem.storyID,
                            updatedStoryboardItem,
                            onSuccess = {
                                Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show()
                                onSave(updatedStoryboardItem)
                                onClose()
                            },
                            onError = {
                                Toast.makeText(context, "Problem saving changes", Toast.LENGTH_LONG).show()
                            }
                        )
                    }) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onClose) {
                        Text("Close")
                    }
                }
            }
        }
    }
}



@Composable
fun ShareStoryboard(selectedCollection: Storyboard_Stories.StoryboardLine, storyID: String) {
    val context = LocalContext.current

    val storyboardToShare = listOf(selectedCollection)
    storyboardToShare.forEach { 
        if(it.storyID == storyID){
            val content = it.storyName+"\n"+it.storyCategory+"\n"+it.storyDescription
            val sendIntent = remember {
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, content)
                    type = "text/plain"
                }
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }
}
