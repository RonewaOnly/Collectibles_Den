package com.example.collectibles_den.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.example.collectibles_den.collectiblesDenApp
import java.sql.Timestamp
import java.util.Calendar

@Preview
@Composable
fun Homepage(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
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
   //val getCollection = DefaultValuesClass()
   Column(
      modifier = Modifier.verticalScroll(rememberScrollState(),true)
   ) {
      Text(
         text = "Recent Additions",
         textAlign = TextAlign.Center,
         fontSize = 40.sp,
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)

      )
      MainSection(collectionsState.value)

      Text(
         text = "Past Collection",
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
      )

      PastSection(collectionsState.value)
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
         modifier = Modifier.height(2100.dp)
      ) {
         items(currentMonthItems) { collect -> // Use currentMonthItems here
            Row(
               modifier = Modifier
                  .width(450.dp)
                  .border(1.dp, Color.Black),
               verticalAlignment = Alignment.CenterVertically
            ) {
               Image(
                  painter = if (collect.makeCollectionCameraImages.isNotEmpty()) rememberAsyncImagePainter(Uri.parse(collect.makeCollectionCameraImages[0])) else rememberAsyncImagePainter(model = Uri.parse("https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="))
                  ,
                  modifier = Modifier
                     .width(200.dp)
                     .height(200.dp), // Ensure the image has a specific height
                  contentScale = ContentScale.FillHeight,
                  contentDescription = null
               )
               Column {
                  Text(text = collect.makeCollectionName)
                  Text(text = collect.makeCollectionDescription.ifEmpty { "No description" })
                  Text(text = "${collect.makeCollectionDate}")
               }

            }
            Spacer(modifier = Modifier.padding(10.dp))
         }
      }
   }else{
      Text(
         text = "No Records Found :) ",
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
      )
   }
}

@Composable
fun PastSection(lateCollection: List<MakeCollection>){

   val pastMonthItems = lateCollection.filter { item ->
      convertServerTimestampToTimestamp(item.makeCollectionDate)?.let { timestamp ->
         timestamp < getBeginningOfPastMonth()
      } ?: false
   }

   if (pastMonthItems.isNotEmpty()) {
      LazyColumn(
         modifier = Modifier.height(2100.dp)
      ) {
         items(pastMonthItems) { collect -> // Use pastMonthItems here
            Row(
               modifier = Modifier
                  .width(450.dp)
                  .border(1.dp, Color.Black),
               verticalAlignment = Alignment.CenterVertically
            ) {
               Image(
                  painter = if (collect.makeCollectionCameraImages.isNotEmpty()) rememberAsyncImagePainter(Uri.parse(collect.makeCollectionCameraImages[0])) else rememberAsyncImagePainter(model = Uri.parse("https://media.istockphoto.com/id/1550540247/photo/decision-thinking-and-asian-man-in-studio-with-glasses-questions-and-brainstorming-on-grey.jpg?s=1024x1024&w=is&k=20&c=M4QZ9PB4fVixyNIrWTgJjIQNPgr2TxX1wlYbyRK40dE="))
                  ,
                  modifier = Modifier
                     .width(200.dp)
                     .height(200.dp), // Ensure the image has a specific height
                  contentScale = ContentScale.FillHeight,
                  contentDescription = null
               )
               Column {
                  Text(text = collect.makeCollectionName)
                  Text(text = collect.makeCollectionDescription.ifEmpty { "No description" })
                  Text(text = "${collect.makeCollectionDate}")
               }

            }
            Spacer(modifier = Modifier.padding(10.dp))
         }
      }
   } else {
      Text(
         text = "No Records Found :) ",
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
      )
   }
}



fun getBeginningOfPastMonth(): Timestamp {//This will be comparing the date if there are old collections
   // Get the current timestamp
   val currentTimestamp = Timestamp(System.currentTimeMillis())

   // Convert to Calendar
   val calendar = Calendar.getInstance()
   calendar.timeInMillis = currentTimestamp.time

   // Subtract one month
   calendar.add(Calendar.MONTH, -1)

   // Set day to 1
   calendar.set(Calendar.DAY_OF_MONTH, 1)

   // Convert Calendar back to timestamp
   return Timestamp(calendar.timeInMillis)
}

// Function to convert ServerValue.TIMESTAMP to Timestamp
fun convertServerTimestampToTimestamp(serverTimestamp: Any): Timestamp? {
   return when (serverTimestamp) {
      is Long -> Timestamp(serverTimestamp) // ServerValue.TIMESTAMP is represented as a Long
      is HashMap<*, *> -> {
         val timestamp = serverTimestamp["timestamp"] as? Long
         if (timestamp != null) {
            Timestamp(timestamp)
         } else {
            null
         }
      }
      else -> null // Handle other types if necessary
   }
}

