package com.example.collectibles_den.Pages

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.collectibles_den.Data.collectionStorage
import com.example.collectibles_den.DefaultValuesClass

@Composable
fun PersonalCollection(){
        //Text(text = "Personal Collection")
        val getCollections = DefaultValuesClass()
        ShowCollectionByCategories(collection = getCollections.recentCollection)
}

@Composable
fun ShowCollectionByCategories(collection: List<collectionStorage>){

        LazyColumn(
                modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxHeight(),
        ) {
                items(collection){item ->
                        Column(
                                modifier = Modifier
                                        .width(450.dp)
                                        .border(1.dp, Color.Black),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Image(
                                        painter = painterResource(item.collectionImage),
                                        modifier = Modifier
                                                .width(200.dp)
                                                .height(200.dp), // Ensure the image has a specific height
                                        contentScale = ContentScale.FillWidth,
                                        contentDescription = null
                                )
                                Text(
                                        text = if(item.collectionCategory.isEmpty())"Unknown" else item.collectionCategory,
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



