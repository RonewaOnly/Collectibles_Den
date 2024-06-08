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
        val showGoalDialog: Boolean = false,
        val goalSet: String,
        val currentProgress: String = if(storyItems.isNotEmpty()) storyItems.count().toString() else "0",
        val user: String? = CollectiblesDenApp.getUserID(),
        ) {
        fun getProgress(): Float {
            return if (goalSet.toInt() > 0) currentProgress.toInt() / goalSet.toFloat() else 0f
        }
    }
}