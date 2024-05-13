package com.example.collectibles_den.Pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.collectibles_den.Data.collectionStorage
import java.sql.Timestamp

var recentCollection = listOf(
   collectionStorage(
      collectionID = "laskdk",
      collectionName = "kajsnldpaks",
      collectionDescription = listOf("kdoaopdkso","sdkajsoas"),
      posted_Date = Timestamp(System.currentTimeMillis())
   )
)
@Composable
fun Homepage(){
   Text(text = "Homepage")
}