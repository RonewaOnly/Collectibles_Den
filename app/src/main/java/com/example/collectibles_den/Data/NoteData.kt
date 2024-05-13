package com.example.collectibles_den.Data

data class NoteData(
    val noteId: String,
    val title: String,
    val notes: List<String> = emptyList(),
    val assignedCollection: String = "",
    val assignedCollectionLocation: String = ""
)
