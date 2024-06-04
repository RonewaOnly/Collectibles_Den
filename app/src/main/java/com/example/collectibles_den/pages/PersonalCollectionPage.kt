package com.example.collectibles_den.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun PersonalCollection(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))){
        //Text(text = "Personal Collection")
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
        //val getCollections = DefaultValuesClass()
        Column(
                modifier = Modifier.verticalScroll(rememberScrollState(),true)
        ) {
                //ShowCollectionByCategories(collectionsState)
                CollectSameCategories(collection = collectionsState)
        }
}


@Composable
fun ShowCollectionByCategories(collection: List<MakeCollection>) {
        LazyColumn(
                modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(3000.dp)
        ) {
                items(collection) { item ->
                        Column(
                                modifier = Modifier
                                        .width(450.dp)
                                        .border(1.dp, Color.Blue),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                val imageUrl = if (item.makeCollectionCameraImages.isNotEmpty()) {
                                        item.makeCollectionCameraImages[0]
                                } else if (item.makeCollectionCameraImages.isNotEmpty()){
                                        item.makeCollectionImages[0]
                                }

                                else {
                                        "https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="
                                }
                                Image(
                                        painter = rememberAsyncImagePainter(imageUrl),
                                        modifier = Modifier
                                                .width(200.dp)
                                                .height(200.dp),
                                        contentScale = ContentScale.FillBounds,
                                        contentDescription = null
                                )
                                Text(
                                        text = if (item.makeCollectionCategory.isEmpty()) "Unknown" else item.makeCollectionCategory,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.LightGray)
                                )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                }
        }
}

@Composable
fun CollectSameCategories(collection: List<MakeCollection>) {
        var expandedCategories by remember { mutableStateOf<Set<String>>(emptySet()) }
        val categoryGroups = collection.groupBy { it.makeCollectionCategory }

        LazyColumn(modifier = Modifier.height(20000.dp)) {
                categoryGroups.forEach { (category, items) ->
                        item {
                                val isExpanded = category in expandedCategories

                                Column(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                                .clickable {
                                                        expandedCategories = if (isExpanded) {
                                                                expandedCategories - category
                                                        } else {
                                                                expandedCategories + category
                                                        }
                                                }
                                                .border(1.dp, Color.Blue)
                                ) {
                                        Text(
                                                "Category: " + category.uppercase(),
                                                fontSize = 15.sp,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(8.dp)
                                        )

                                        if (isExpanded) {
                                                items.forEach { item ->
                                                        Column(
                                                                modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .padding(8.dp)
                                                                        .border(
                                                                                1.dp,
                                                                                Color.Blue
                                                                        )
                                                        ) {
                                                                Text(text = item.makeCollectionName.uppercase(), style = MaterialTheme.typography.bodySmall)
                                                                Text(
                                                                        text = item.makeCollectionDescription.ifEmpty { "No description" },
                                                                        style = MaterialTheme.typography.bodyMedium
                                                                )
                                                                if (item.makeCollectionImages.isNotEmpty()) {
                                                                        Image(
                                                                                painter = rememberAsyncImagePainter(
                                                                                        Uri.parse(item.makeCollectionImages[0])),
                                                                                contentDescription = null,
                                                                                modifier = Modifier
                                                                                        .fillMaxWidth()
                                                                                        .height(200.dp),
                                                                                contentScale = ContentScale.Crop
                                                                        )
                                                                }
                                                                Spacer(modifier = Modifier.height(8.dp))
                                                        }
                                                }
                                        } else {
                                                val item = items.first()
                                                var image = remember{ mutableStateOf("") }
                                                Column(
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(8.dp)
                                                                .border(1.dp, Color.Blue)
                                                ) {
                                                        Text(text = item.makeCollectionName, style = MaterialTheme.typography.bodySmall)
                                                        Text(
                                                                text ="Description ${item.makeCollectionDescription.ifEmpty { "No description" }}",
                                                                style = MaterialTheme.typography.bodyMedium
                                                        )
                                                        if (item.makeCollectionCameraImages.isNotEmpty()) {
                                                                Image(
                                                                        painter = rememberAsyncImagePainter(Uri.parse(item.makeCollectionCameraImages[0])),
                                                                        contentDescription = null,
                                                                        modifier = Modifier
                                                                                .fillMaxWidth()
                                                                                .height(200.dp),
                                                                        contentScale = ContentScale.Crop
                                                                )
                                                        }
                                                }
                                        }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                        }
                }
        }
}

