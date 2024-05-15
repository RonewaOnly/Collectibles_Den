package com.example.collectibles_den.Pages

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
import java.util.Calendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collectibles_den.Data.collectionStorage
import com.example.collectibles_den.DefaultValuesClass
import java.sql.Timestamp


@Composable
fun Homepage(){
   var getCollection = DefaultValuesClass()
   Column(
      modifier = Modifier.verticalScroll(rememberScrollState(),true)
   ) {
      Text(
         text = "Recent Additions",
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
      )
      MainSection(getCollection.recentCollection)

      Text(
         text = "Past Collection",
         textAlign = TextAlign.Center,
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
      )

      PastSection(getCollection.recentCollection)
   }
}


@Composable
fun MainSection(recentCollection: List<collectionStorage>) {
   val currentMonthItems = recentCollection.filter { item ->
      item.postedDate > getBeginningOfPastMonth()
   }
   if (currentMonthItems.isNotEmpty()) {
      LazyColumn(
         modifier = Modifier.height(2100.dp)
      ) {
         items(recentCollection) { collect ->
            Row(
               modifier = Modifier
                  .width(450.dp)
                  .border(1.dp, Color.Black),
               verticalAlignment = Alignment.CenterVertically
            ) {
               Image(
                  painter = painterResource(collect.collectionImage),
                  modifier = Modifier
                     .width(200.dp)
                     .height(200.dp), // Ensure the image has a specific height
                  contentScale = ContentScale.Fit,
                  contentDescription = null
               )
               Column {
                  Text(text = collect.collectionName)
                  Text(text = collect.collectionDescription.firstOrNull() ?: "No description")
                  Text(text = "${collect.postedDate}")
               }

            }
            Spacer(modifier = Modifier.padding(10.dp))
         }
      }
   }else{
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
fun PastSection(lateCollection: List<collectionStorage>){

   val pastMonthItems = lateCollection.filter { item ->
      item.postedDate < getBeginningOfPastMonth()
   }

   if (pastMonthItems.isNotEmpty()) {
      LazyColumn(
         modifier = Modifier.height(2100.dp)
      ) {
         items(pastMonthItems) { collect ->
            Row(
               modifier = Modifier
                  .width(450.dp)
                  .border(1.dp, Color.Black),
               verticalAlignment = Alignment.CenterVertically
            ) {
               Image(
                  painter = painterResource(collect.collectionImage),
                  modifier = Modifier
                     .width(200.dp)
                     .height(200.dp), // Ensure the image has a specific height
                  contentScale = ContentScale.Fit,
                  contentDescription = null
               )
               Column {
                  Text(text = collect.collectionName)
                  Text(text = collect.collectionDescription.firstOrNull() ?: "No description")
                  Text(text = "${collect.postedDate}")
               }

            }
            Spacer(modifier = Modifier.padding(10.dp))
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

