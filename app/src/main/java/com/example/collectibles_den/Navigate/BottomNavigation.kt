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
    var navList = listOf(
        {
            navIcons.title = "Home"
            navIcons.icon = Icons.Filled.Home
        },
        {
            navIcons.title = "Collection"
            navIcons.icon = Icons.Filled.Newspaper
        },
        {
            navIcons.title = "Add"
            navIcons.icon = Icons.Filled.Add
        },
        {
            navIcons.title = "Storyboard"
            navIcons.icon = Icons.Filled.SpaceDashboard
        },
        {
            navIcons.title = "Profile"
            navIcons.icon = Icons.Filled.Person
        }
    )

    NavigationBar {
        navList.forEachIndexed{ index,item ->
                NavigationBarItem(
                    selected = index == count ,
                    onClick = {
                        when (index) {
                            1 -> {
                                navController.navigate("homepage")
                            }
                            2 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                count++
                                navController.navigate("personalCollection")


                            }
                            3 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                count++
                                navController.navigate("addCollections")


                            }
                            4 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                count++
                                navController.navigate("storyboard")


                            }
                            5 -> {
                                Toast.makeText(context,"$index", Toast.LENGTH_SHORT).show()
                                count++
                                navController.navigate("profileAccount")

                            }
                        }
                    },
                    icon = {
                        Icon(imageVector = navIcons.icon, contentDescription = navIcons.title)
                    })
        }


    }
}

object navIcons {
    var title: String = ""
    var icon: ImageVector = Icons.Default.InsertPageBreak
}