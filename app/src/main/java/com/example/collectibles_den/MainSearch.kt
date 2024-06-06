package com.example.collectibles_den

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainSearch(){
    var searchValue by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        //Logo
        Column(
            modifier = Modifier.width(130.dp).border(1.dp,Color.Blue).height(60.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_removebg),
                contentDescription = "Collection Den Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.width(200.dp).height(60.dp)
            )
        }
        //Search input
        Row(
            modifier = Modifier
                .width(350.dp)
                .border(1.dp, Color.Blue, RectangleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.border(1.dp, Color.Blue, RectangleShape).height(60.dp)
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
            TextField(
                value = searchValue,
                onValueChange = { searchValue = it },
                placeholder = { Text(text = "Search Collection") },
                modifier = Modifier.fillMaxWidth().height(60.dp).border(
                    1.dp, Color.Blue, RectangleShape
                )
            )
        }
    }

}