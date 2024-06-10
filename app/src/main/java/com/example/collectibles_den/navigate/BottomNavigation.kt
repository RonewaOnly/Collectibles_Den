package com.example.collectibles_den.navigate

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertPageBreak
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController


@Composable
fun BottomNavigation(navController: NavController) {
    val context = LocalContext.current
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navList = listOf(
        NavIcons(
            "Home",
            Icons.Filled.Home
        ),
        NavIcons(
            "Collection",
            Icons.Filled.Newspaper
        ),
        NavIcons(
            "Add",
            Icons.Filled.Add
        ),
        NavIcons(
            "Storyboard",
            Icons.Filled.SpaceDashboard
        ),
        NavIcons(
            "Profile",
            Icons.Filled.Person
        )
    )

    NavigationBar {
        navList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    when (index) {
                        0 -> navController.navigate("homepage")
                        1 -> {
                            Toast.makeText(context, "Collection", Toast.LENGTH_SHORT).show()
                            navController.navigate("personalCollection")
                        }
                        2 -> {
                            Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show()
                            navController.navigate("addCollections")
                        }
                        3 -> {
                            Toast.makeText(context, "Storyboard", Toast.LENGTH_SHORT).show()
                            navController.navigate("storyboard")
                        }
                        4 -> {
                            Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show()
                            navController.navigate("profileAccount")
                        }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, tint = Color.Red, contentDescription = item.title)
                }
            )
        }
    }
}

data class NavIcons(
    var title: String = "",
    var icon: ImageVector = Icons.Default.InsertPageBreak
)
