package com.example.collectibles_den.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collectibles_den.MainSearch
import com.example.collectibles_den.navigate.BottomNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MainScreen()
           /* val startingScreen = StartActivity()
            startingScreen.getStarted()*/

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        topBar = { MainSearch() },
        bottomBar = { BottomNavigation(navController = navController)}
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                color = Color.DarkGray
            )
            NavHost(navController = navController, startDestination = "homepage") {
                composable("homepage") {
                    Homepage()
                }
                composable("personalCollection") {
                    PersonalCollection()
                }
                composable("addCollections") {
                    AddCollections()
                }
                composable("storyboard") {
                    Storyboard()
                }
                composable("profileAccount") {
                    ProfileAccount()
                }
            }
        }
    }
}