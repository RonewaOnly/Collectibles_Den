package com.example.collectibles_den.data

import com.example.collectibles_den.CollectiblesDenApp

class Storyboard_Stories {
    //private val userID = CollectiblesDenApp.getUserID()

    data class StoryboardLine(
        val storyID: String ="",
        val storyName: String,
        val storyItems: List<MakeCollection?> = emptyList(),
        val storyCategory: String,
        val storyDescription: String ="",
        val storyCovers: List<String> = emptyList(),
        val showGoalDialog: Boolean,
        val goalSet: Int = 0,
        val currentProgress: Int = if(storyItems.isNotEmpty()) storyItems.count() else 0,
        val user: String? = CollectiblesDenApp.getUserID(),

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