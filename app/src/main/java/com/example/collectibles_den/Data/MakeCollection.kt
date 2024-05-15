package com.example.collectibles_den.Data

import android.net.Uri
import java.nio.file.Files
import java.sql.Timestamp

data class MakeCollection(
    val makeCollectionID: String ="",
    var makeCollectionName:String,
    var makeCollectionCategory: String,
    var makeCollectionImages: List<Uri?> = emptyList(),
    var makeCollectionCameraImages: List<Uri?> = emptyList(),
    var makeCollectionNotes: List<NoteData> = emptyList(),
    var makeCollectionScannedItems: List<Uri?> = emptyList(),
    var makeCollectionFiles: List<Uri?> = emptyList(),
    var makeCollectionDate : Timestamp = Timestamp(System.currentTimeMillis())
    )
