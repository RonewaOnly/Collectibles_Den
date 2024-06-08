package com.example.collectibles_den.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.R
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Calendar

@Preview
@Composable
fun Homepage(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
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
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
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
         modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
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
         modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
      )
   }
}

@Composable
fun CollectionItem(collect: MakeCollection) {
   Row(
      modifier = Modifier
         .fillMaxWidth()
         .padding(8.dp)
         .border(1.dp, Color.Black),
      verticalAlignment = Alignment.CenterVertically
   ) {
      Image(
         painter = rememberAsyncImagePainter(collect.makeCollectionCameraImages.firstOrNull() ?: "https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="),
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
