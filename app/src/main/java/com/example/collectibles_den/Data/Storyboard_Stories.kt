package com.example.collectibles_den.Data

import android.net.Uri
import java.sql.Timestamp

class Storyboard_Stories {

    data class StoryboardLine(
        val storyID: String ="",
        val storyName: String = "",
        val storyItems: List<collectionStorage?> = emptyList(),
        val storyDescription: String = "",
        val storyCovers: List<Uri?> = emptyList(),
        val goalSet: Int = 0,
        val dateOfCreation: Timestamp = Timestamp(System.currentTimeMillis()),
        val tags: List<String> = emptyList(),
        val storyCategory: String = ""
    )

    data class Settings(//This data class will be
        val storyId : StoryboardLine,
        val onEdit: () -> Unit,
        val onDelete: () -> Unit,
        val onShare: () -> Unit,
        val onSave: () -> Unit
    )

}