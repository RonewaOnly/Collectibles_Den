package com.example.collectibles_den.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.example.collectibles_den.collectiblesDenApp

@Preview
@Composable
fun PersonalCollection(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))){
        //Text(text = "Personal Collection")
        val context = LocalContext.current
        val userID = collectiblesDenApp.getUserID()
        val collectionsState = remember { mutableStateOf<List<MakeCollection>>(emptyList()) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(userID) {
                userID?.let { uid ->
                        viewModel.getCollections(uid) { collections ->
                                collectionsState.value = collections
                        }
                }
        }
        //val getCollections = DefaultValuesClass()
        ShowCollectionByCategories(collectionsState.value)
}


@Composable

fun ShowCollectionByCategories(collection: List<MakeCollection>) {
        LazyColumn(
                modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxHeight(),
        ) {
                items(collection) { item ->
                        Column(
                                modifier = Modifier
                                        .width(450.dp)
                                        .border(1.dp, Color.Black),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                val imageUrl = if (item.makeCollectionCameraImages.isNotEmpty()) {
                                        item.makeCollectionCameraImages[0]
                                } else {
                                        "https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="
                                }
                                Image(
                                        painter = rememberAsyncImagePainter(imageUrl),
                                        modifier = Modifier
                                                .width(200.dp)
                                                .height(200.dp),
                                        contentScale = ContentScale.FillWidth,
                                        contentDescription = null
                                )
                                Text(
                                        text = if (item.makeCollectionCategory.isEmpty()) "Unknown" else item.makeCollectionCategory,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth().background(Color.LightGray)
                                )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                }
        }
}



