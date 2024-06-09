package com.example.collectibles_den.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun PersonalCollection(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
        val context = LocalContext.current
        val userID = CollectiblesDenApp.getUserID()
        var collectionsState by remember { mutableStateOf<List<MakeCollection>>(emptyList()) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(userID) {
                userID?.let { uid ->
                        coroutineScope.launch(Dispatchers.IO) {
                                viewModel.getCollections(uid) { collections ->
                                        collectionsState = collections
                                }
                        }
                }
        }

        Column(
                modifier = Modifier.verticalScroll(rememberScrollState(), true)
        ) {
                CollectSameCategories(collection = collectionsState)
        }
}

@Composable
fun CollectSameCategories(collection: List<MakeCollection>) {
        var expandedCategories by remember { mutableStateOf<Set<String>>(emptySet()) }
        val categoryGroups = collection.groupBy { it.makeCollectionCategory }
        var placeholderImage = "https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="

        LazyColumn(modifier = Modifier.fillMaxWidth().height(3200.dp).padding(8.dp)) {
                categoryGroups.forEach { (category, items) ->
                        item {
                                val isExpanded = category in expandedCategories

                                Column(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                        expandedCategories = if (isExpanded) {
                                                                expandedCategories - category
                                                        } else {
                                                                expandedCategories + category
                                                        }
                                                }
                                                .border(1.dp, Color.Gray)
                                ) {
                                        Text(
                                                text = category,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color.LightGray)
                                                        .padding(8.dp)
                                        )

                                        if (isExpanded) {
                                                items.forEach { item ->
                                                        CollectionItem(item = item, image = item.makeCollectionCover)
                                                }
                                        } else {
                                                CollectionItem(item = items.first(), image = items[0].makeCollectionCover)
                                        }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                        }
                }
        }
}

@Composable
fun CollectionItem(item: MakeCollection, image: String) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.LightGray)
        ) {
                Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = null,
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        contentScale = ContentScale.Crop
                )
                Text(
                        text = item.makeCollectionName,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                )
                Text(
                        text = "Description: ${item.makeCollectionDescription.ifEmpty { "No description" }}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
        }
}


