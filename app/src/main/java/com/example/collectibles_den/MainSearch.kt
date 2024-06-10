package com.example.collectibles_den

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.data.Storyboard_Stories
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*


@Composable
fun MainSearch(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
    val context = LocalContext.current
    val userID = CollectiblesDenApp.getUserID()
    var collectionsState by remember { mutableStateOf<List<MakeCollection>>(emptyList()) }
    var storyboardState by remember { mutableStateOf<List<Storyboard_Stories.StoryboardLine>>(emptyList()) }
    var searchValue by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Any>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userID) {
        userID?.let { uid ->
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.getCollections(uid) { collections ->
                    collectionsState = collections
                }

                viewModel.getStoryboard(uid) { story ->
                    storyboardState = story
                }
            }
        }
    }

    fun performSearch(query: String) {
        val collectionResults = collectionsState.filter { it.makeCollectionName.contains(query, ignoreCase = true) }
        val storyboardResults = storyboardState.filter { it.storyName.contains(query, ignoreCase = true) }
        searchResults = collectionResults + storyboardResults
        showDialog = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Logo
            Column(
                modifier = Modifier.width(130.dp).border(1.dp, Color.Blue).height(60.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_removebg),
                    contentDescription = "Collection Den Logo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(200.dp).height(60.dp)
                )
            }
            // Search input
            Row(
                modifier = Modifier
                    .width(350.dp)
                    .border(1.dp, Color.Blue, RectangleShape),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { performSearch(searchValue) },
                    modifier = Modifier.border(1.dp, Color.Blue, RectangleShape).height(60.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
                BasicTextField(
                    value = searchValue,
                    onValueChange = { searchValue = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(
                        1.dp, Color.Blue, RectangleShape
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchValue.isEmpty()) {
                                Text(text = "Search Collection", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }

    if (showDialog) {
        ResultsDialog(results = searchResults) { showDialog = false }
    }
}

@Composable
fun ResultsDialog(results: List<Any>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        text = {
            Column {
                results.forEach { result ->
                    when (result) {
                        is MakeCollection -> {
                            Text("Collection: ${result.makeCollectionName}")
                        }
                        is Storyboard_Stories.StoryboardLine -> {
                            Text("Storyboard: ${result.storyName}")
                        }
                    }
                }
            }
        }
    )
}
