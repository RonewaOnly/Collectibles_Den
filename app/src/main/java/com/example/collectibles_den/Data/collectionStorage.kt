package com.example.collectibles_den.Data

import com.example.collectibles_den.R
import java.sql.Timestamp

data class collectionStorage(
    val collectionID: String,
    val userAssigned: String ="",
    val collectionName:String,
    var collectionImage: Int = R.drawable.default_image,//Will be changing this from an int to Uri
    var collectionDescription: List<String> = emptyList(),
    var collectionCategory: String = "",
    var localAreaStorage:  String = "",
    val postedDate: Timestamp
)
