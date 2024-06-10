import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.data.Storyboard_Stories
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.example.collectibles_den.pages.AchievementBlock

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
            viewModel.getCollections(id) {
                getCollectionsState.value = it
            }
        }
    }
    val achievement = AchievementBlock()
    Column(modifier = Modifier
        .width(1200.dp)
        .height(3500.dp)) {
        achievement.ViewPage()
        MaxSection(collectionsState.value, getCollectionsState.value, viewModel, userID)
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
    // Initialize selectedCollection with the existing storyboard items
    val selectedCollection = remember { mutableStateListOf<Storyboard_Stories.StoryboardLine>().apply { addAll(stories) } }
    val toggleStates = remember { mutableStateMapOf<String, Boolean>() }

    // Rest of the code remains the same...
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
                    val existingStoryboard =
                        selectedCollection.firstOrNull { it.storyName == storyboard.storyName }
                    if (existingStoryboard != null) {
                        // Update the existing storyboard
                        viewModel.updateStoryboard(
                            existingStoryboard.storyID, storyboard, // Preserve the existing goalSet
                            onSuccess = {
                                val index = selectedCollection.indexOf(existingStoryboard)
                                if (index != -1) {
                                    selectedCollection[index] = storyboard
                                }
                                Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                            },
                            onError = { errorMessage ->
                                Toast.makeText(
                                    context,
                                    "Error updating item: $errorMessage",
                                    Toast.LENGTH_LONG
                                ).show()
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
            StoryboardItem(item, collections, selectedCollection, toggleStates, viewModel, userID)
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
    var goalSet by remember { mutableStateOf("") }

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
                                        goalSet = goalSet,
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
    selectedCollection: MutableList<Storyboard_Stories.StoryboardLine>,
    toggleStates: MutableMap<String, Boolean>,
    viewModel: DatabaseViewModel,
    userID: String?
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.BorderAll, contentDescription = "Storyboard Item")
                Text(
                    text = item.storyName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            IconButton(onClick = {
                viewModel.deleteStoryboard(
                    item.storyID,
                    onSuccess = {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                        selectedCollection.remove(item)
                    },
                    onError = { errorMessage ->
                        Toast.makeText(context, "Error deleting item: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                )
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
            IconButton(onClick = { showEditDialog = true }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { shareText(context, item.storyName) }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
        }

        Text(
            text = "Category: ${item.storyCategory}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Goal: ${item.goalSet}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        item.storyCovers.forEach { coverUrl ->
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
                    .border(1.dp, Color.Gray)
            )
        }

//        val toggleState = toggleStates[item.storyID] ?: false
//        IconButton(onClick = { toggleStates[item.storyID] = !toggleState }) {
//            Icon(
//                imageVector = if (toggleState) Icons.Default.Edit else Icons.Default.Share,
//                contentDescription = "Toggle"
//            )
//        }
//
//        if (toggleState) {
//            // Show hidden content when toggleState is true
//
//        }

        if (showEditDialog) {
            EditBoard(
                item = item,
                onClose = { showEditDialog = false },
                collections = collections,
                onSave = { updatedStoryboard ->
                    viewModel.updateStoryboard(
                        item.storyID, updatedStoryboard,
                        onSuccess = {
                            val index = selectedCollection.indexOfFirst { it.storyID == item.storyID }
                            if (index != -1) {
                                selectedCollection[index] = updatedStoryboard
                            }
                            Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, "Error updating item: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )
        }
    }
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

@Composable
fun EditBoard(
    item: Storyboard_Stories.StoryboardLine,
    onClose: () -> Unit,
    collections: List<MakeCollection>,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    var storyName by remember { mutableStateOf(item.storyName) }
    var storyDescription by remember { mutableStateOf(item.storyDescription) }
    var storyCategory by remember { mutableStateOf(item.storyCategory) }
    var goalSet by remember { mutableStateOf(item.goalSet.toString()) }
    var imageUri by remember { mutableStateOf<Uri?>(Uri.parse(item.storyCovers.firstOrNull())) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    var selectedItems by remember { mutableStateOf<List<MakeCollection?>>(item.storyItems) }

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
                                val updatedStoryboard = Storyboard_Stories.StoryboardLine(
                                    storyID = item.storyID,
                                    storyName = storyName,
                                    storyItems = selectedItems.filterNotNull(),
                                    storyCategory = storyCategory,
                                    storyDescription = storyDescription,
                                    storyCovers = if (imageUri != null) listOf(imageUri.toString()) else item.storyCovers,
                                    goalSet = goalSet,
                                    user = item.user
                                )
                                onSave(updatedStoryboard)
                                onClose()
                            } catch (e: Exception) {
                                Log.e("EditBoard", "Error saving storyboard: ${e.message}")
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
fun handleSaveStoryboard(
    storyID: String,
    updatedStoryboardItem: Storyboard_Stories.StoryboardLine,
    newGoalSet: Int,
    viewModel: DatabaseViewModel,
    context: Context,
    onClose: () -> Unit,
    onSave: (Storyboard_Stories.StoryboardLine) -> Unit
) {
    val updatedStoryboard = updatedStoryboardItem.copy(goalSet = newGoalSet.toString())

    viewModel.updateStoryboard(
        storyID,
        updatedStoryboard,
        onSuccess = {
            onSave(updatedStoryboard)
            onClose()
            Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show()
        },
        onError = {
            Toast.makeText(context, "Problem saving changes", Toast.LENGTH_LONG).show()
        }
    )
}
