package com.example.collectibles_den.data

import com.google.firebase.database.ServerValue
import java.sql.Timestamp

data class MakeCollection(
    val makeCollectionID: String = "",
    val makeCollectionName: String = "",
    val makeCollectionDescription: String = "",
    val makeCollectionCategory: String = "",
    val makeCollectionImages: List<String> = emptyList(),
    val makeCollectionCameraImages: List<String> = emptyList(),
    val makeCollectionNotes: List<NoteData> = emptyList(),
    val makeCollectionScannedItems: List<String> = emptyList(),
    val makeCollectionFiles: List<String> = emptyList(),
    val userAssigned: String = "",
    val makeCollectionDate: Any = ServerValue.TIMESTAMP
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