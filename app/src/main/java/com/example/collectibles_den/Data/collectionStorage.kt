package com.example.collectibles_den.Data

import android.net.Uri
import java.sql.Timestamp

data class collectionStorage(
    val collectionID: String,
    val userAssigned: String,
    val collectionName:String,
    var collectionImage: List<Uri?> = emptyList(),
    var collectionDescription: List<String> = emptyList(),
    var localAreaStorage:  String = "",
    val posted_Date: Timestamp
)
