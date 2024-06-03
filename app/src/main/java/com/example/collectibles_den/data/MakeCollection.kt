package com.example.collectibles_den.data

import com.google.firebase.database.ServerValue
import java.sql.Timestamp

data class MakeCollection(
    val makeCollectionID: String = "",
    val makeCollectionName: String,
    val makeCollectionDescription: String,
    val makeCollectionCategory: String,
    val makeCollectionImages: List<String> = emptyList(), // Changed to List<String>
    val makeCollectionCameraImages: List<String> = emptyList(), // Changed to List<String>
    val makeCollectionNotes: List<NoteData> = emptyList(),
    val makeCollectionScannedItems: List<String> = emptyList(), // Changed to List<String>
    val makeCollectionFiles: List<String> = emptyList(), // Changed to List<String>
    val userAssigned: String,
    var makeCollectionDate: Any = ServerValue.TIMESTAMP // Use ServerValue.TIMESTAMP here
    )
{

        // No-argument constructor
        constructor() : this(
        "",
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        "",
        Timestamp(System.currentTimeMillis())
        )

}