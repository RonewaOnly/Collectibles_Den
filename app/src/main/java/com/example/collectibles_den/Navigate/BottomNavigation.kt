package com.example.collectibles_den.Navigate

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun BottomNavigation(navController:NavController){
    val context = LocalContext.current
    var count by remember{ mutableIntStateOf(1) }
    val navList = listOf(
        navIcons(
            "Home",
            Icons.Filled.Home
        ),
        navIcons(
            "Collection",
            Icons.Filled.Newspaper
        ),
        navIcons(
            "Add",
            Icons.Filled.Add
        ),
        navIcons(
            "Storyboard",
            Icons.Filled.SpaceDashboard
        ),
        navIcons(
            "Profile",
            Icons.Filled.Person
        )
    )

    NavigationBar {
        navList.forEachIndexed{ index, item ->
                NavigationBarItem(
                    selected = index == count ,
                    onClick = {
                        when (index) {
                            0 -> {
                                navController.navigate("homepage")
                            }
                            1 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                navController.navigate("personalCollection")
                                count++


                            }
                            2 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                navController.navigate("addCollections")
                                count++


                            }
                            3 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                navController.navigate("storyboard")
                                count++
                            }
                            4 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                navController.navigate("profileAccount")
                                count++
                            }
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    }
                )
        }
    }
}

data class navIcons (
    var title: String = "",
    var icon: ImageVector = Icons.Default.InsertPageBreak
)
