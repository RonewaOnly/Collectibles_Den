package com.example.collectibles_den.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.collectibles_den.Data.collectionStorage
import com.example.collectibles_den.DefaultValuesClass
import com.example.collectibles_den.R

@Composable
fun Storyboard(){
    Text(text = "Story board")
    val getCollection = DefaultValuesClass()
    //CollectionStoryboard(collections = getCollection.recentCollection)
}

/*
@Composable
fun CollectionStoryboard(collections: List<collectionStorage>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(collections) { collection ->
            CollectionItem(collection = collection)
        }
    }
}

@Composable
private fun CollectionItem(collection: collectionStorage) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CollectionImage(imageResId = R.drawable.default_image)            
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = collection.collectionName, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = collection.collectionDescription.firstOrNull() ?: "", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Composable
private fun CollectionImage(imageResId: Int?, modifier: Modifier = Modifier) {
    imageResId?.let {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = modifier
                .width(100.dp)
                .height(100.dp)
                .background(color = Color.LightGray),
            contentScale = ContentScale.Crop
        )
    }
}
 */