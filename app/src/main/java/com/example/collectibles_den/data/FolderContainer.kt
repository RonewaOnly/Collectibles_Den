package com.example.collectibles_den.data

import java.sql.Timestamp

data class FolderContainer(
    val folderName: String,
    val folderContent: List<Any?> = emptyList(),//This will allow me to save multiple different items
    val folderLocation: String,
    val dateOfCreation: Timestamp
)
