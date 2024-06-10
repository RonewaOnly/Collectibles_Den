package com.example.collectibles_den.data

data class NoteData(
    val noteId: String = "",
    val title: String,
    val notes: String = "",
    val downloadLink: String ="",
    val assignedCollection: String = "",
    val assignedCollectionLocation: String = ""
)
