package com.example.collectibles_den

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collectibles_den.Navigate.BottomNavigation
import com.example.collectibles_den.Pages.AddCollections
import com.example.collectibles_den.Pages.Homepage
import com.example.collectibles_den.Pages.PersonalCollection
import com.example.collectibles_den.Pages.ProfileAccount
import com.example.collectibles_den.Pages.Storyboard
import com.example.collectibles_den.ui.theme.Collectibles_DenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}
/*fun ComponentActivity.enableEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
}*/
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
            Divider(
                color = Color.DarkGray,
                thickness = 2.dp,
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
