package com.example.collectibles_den.Data

import android.net.Uri
import java.sql.Timestamp

class Storyboard_Stories {

    data class StoryboardLine(
        val storyID: String ="",
        val storyName: String,
        val storyItems: List<collectionStorage?> = emptyList(),
        val storyCategory: String,
        val storyDescription: String ="",
        val storyCovers: List<Uri?> = emptyList(),
        val showGoalDialog: Boolean,
        val goalSet: Int = 0,
        val currentProgress: Int = if(storyItems.isNotEmpty()) storyItems.count() else 0
    ) {
        fun getProgress(): Float {
            return if (goalSet > 0) currentProgress / goalSet.toFloat() else 0f
        }
    }


    data class Settings(//This data class will be
        val storyId : StoryboardLine,
        val onEdit: () -> Unit,
        val onDelete: () -> Unit,
        val onShare: () -> Unit,
        val onSave: () -> Unit
    )

}