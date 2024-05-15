package com.example.collectibles_den.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun Welcome(){

    Column (
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {



        Text(text = "Collectors Den", fontSize = 40.sp)

    }

}


