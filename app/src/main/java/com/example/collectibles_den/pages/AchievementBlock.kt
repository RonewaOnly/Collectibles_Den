package com.example.collectibles_den.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.R
import com.example.collectibles_den.data.Storyboard_Stories
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory

class AchievementBlock {

    @Composable
    fun ViewPage(viewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModelFactory(context = LocalContext.current))) {
        val userID = CollectiblesDenApp.getUserID()
        val collectionsState =
            remember { mutableStateOf<List<Storyboard_Stories.StoryboardLine>>(emptyList()) }

        // Load data from Firebase
        Column(
            modifier = Modifier.width(650.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LaunchedEffect(Unit) {
                userID?.let { id ->
                    viewModel.getStoryboard(id) { storyboardLines ->
                        Log.d("AchievementBlock", "Fetched storyboard lines: $storyboardLines")
                        collectionsState.value = storyboardLines
                    }
                }
            }
            Listings(stories = collectionsState.value, viewModel = viewModel, userID = userID)
        }
    }

    @Composable
    fun Listings(
        stories: List<Storyboard_Stories.StoryboardLine>,
        viewModel: DatabaseViewModel,
        userID: String?
    ) {

        if (stories.isNotEmpty()) {

            LazyRow(
                modifier = Modifier
                    .height(120.dp)
                    .width(450.dp)
                    .padding(10.dp)
                    .border(2.dp, Color.Black)
            ) {
                items(stories) {
                    if (userID == it.user) {
                        Box(it)
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "No Achievement To Read Yet...",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun Box(story: Storyboard_Stories.StoryboardLine) {
        val badge = remember {
            mutableStateOf(0)
        }
        val showDialog = remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .width(250.dp)
                .height(110.dp)
                .padding(15.dp)
                .border(2.dp, Color.LightGray),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = story.storyName)
            LinearProgressIndicator(progress = { story.getProgress() }, color = Color.Red)
            Text(text = story.currentProgress)

            Row(
                modifier = Modifier
                    .width(120.dp)
                    .height(35.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (story.currentProgress) {
                    in listOf("1", "2") -> {
                        // Less than 3 but greater than or equal to 2
                        Text(text = BadgesTitles.Starter)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Icon(painter = painterResource(BadgesIcons.starter),
                            contentDescription = BadgesTitles.Starter,
                            modifier = Modifier.clickable {
                                badge.value = BadgesIcons.starter
                                showDialog.value = true
                            })
                    }

                    in listOf("3", "4", "5", "6", "7", "8", "9") -> {
                        // Greater than or equal to 4 but less than 10
                        Text(text = BadgesTitles.Collector)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Icon(painter = painterResource(BadgesIcons.collector),
                            contentDescription = BadgesTitles.Collector,
                            modifier = Modifier.clickable {
                                badge.value = BadgesIcons.collector
                                showDialog.value = true
                            })
                    }

                    "10" -> {
                        // Equal to 10
                        Text(text = BadgesTitles.Packrat)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Icon(
                            painter = painterResource(BadgesIcons.packrat),
                            contentDescription = BadgesTitles.Packrat,
                            modifier = Modifier.clickable {
                                badge.value = BadgesIcons.packrat
                                showDialog.value = true
                            }
                        )
                    }
                }
            }
        }

        if (showDialog.value && badge.value != 0) {
            ViewBadge(img = badge.value, onClose = { showDialog.value = false })
        }
    }

    @Composable
    fun ViewBadge(img: Int, onClose: () -> Unit) {
        Dialog(onDismissRequest = { onClose() }) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .height(125.dp)
                    .padding(18.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(img),
                    contentDescription = null,
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                        .clickable {
                            onClose()
                        }
                )
            }
        }
    }
}

object BadgesTitles {
    const val Starter = "Starter"
    const val Collector = "Collector"
    const val Packrat = "Packrat"
}

object BadgesIcons {
    val starter = R.drawable.starterachievement
    val collector = R.drawable.collectorachievement
    val packrat = R.drawable.packratachievement
}