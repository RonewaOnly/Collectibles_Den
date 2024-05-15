package com.example.collectibles_den.Pages

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.sharp.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.collectibles_den.Data.Storyboard_Stories
import java.nio.file.Files

@Composable
fun AddCollections(){
        Column(
                modifier = Modifier
                        .width(1200.dp)
                        .height(2500.dp)
                        .border(1.dp, Color.Black)
                        .verticalScroll(rememberScrollState(), true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(text = "Add Collection")
                MakeCollection()
        }

}

@Composable
fun MakeCollection(){
        var openForm by remember {
                mutableStateOf(false
                )
        }
        var isPopClicked by remember {
                mutableStateOf(false
                )
        }
        var isGalleryClick by remember {
                mutableStateOf(false)
        }
        var isCameraClick by remember {
                mutableStateOf(false)
        }
        var isNotesClick by remember {
                mutableStateOf(false)
        }
        var isScannerClick by remember {
                mutableStateOf(false)
        }
        var isFolderClick by remember {
                mutableStateOf(false)
        }
        var isCreateFolderClick by remember {
                mutableStateOf(false)
        }
        IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
                        .width(150.dp)
                        .height(60.dp),
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.inversePrimary),
                
                ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                ){
                        Icon(imageVector = Icons.Sharp.OpenInBrowser, contentDescription = null)
                        Text(text = "Make a Collection")
                }
        }

        Column(
                modifier = Modifier
                        .width(900.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.DarkGray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(text = "Add Items", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.padding(12.dp))
                IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, Color.Transparent, RectangleShape),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                        ) {
                                Icon(imageVector = Icons.Filled.Image, contentDescription = null)
                                Text(text = "Get Image")
                        }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, Color.Transparent, RectangleShape),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ){
                                Icon(imageVector = Icons.Filled.Camera, contentDescription = null)
                                Text(text = "Take Image")
                        }
                }
                Spacer(modifier = Modifier.padding(5.dp))

                IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, Color.Transparent, RectangleShape),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ) {
                                Icon(imageVector = Icons.Filled.NoteAlt, contentDescription = null)
                                Text(text = "Take Notes")
                        }
                }
                Spacer(modifier = Modifier.padding(5.dp))

                IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, Color.Transparent, RectangleShape),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ) {
                                Icon(imageVector = Icons.Filled.Scanner, contentDescription = null)
                                Text(text = "Scan")
                        }
                }
                Spacer(modifier = Modifier.padding(5.dp))

                IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, Color.Transparent, RectangleShape),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ) {
                                Icon(
                                        imageVector = Icons.Filled.Attachment,
                                        contentDescription = null
                                )
                                Text(text = "Attach File")
                        }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                
                IconButton(
                        onClick = { isPopClicked = true },
                        modifier = Modifier
                                .width(120.dp)
                                .border(1.dp, Color.Transparent, RoundedCornerShape(25)),
                        colors = IconButtonDefaults.iconButtonColors(Color.LightGray)

                ){
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                                Text(text = "Create Board")
                        }
                }

                if (isPopClicked){
                        SaveCollection(
                                onClose = { /*TODO*/ },
                                onSave = {}
                        )
                }
        }
}

@Composable
fun SaveCollection(
        getImage: Uri? = Uri.EMPTY,
        takeImage:Uri? = Uri.EMPTY,
        notes: String? = "",
        scanned: Uri? = Uri.EMPTY,
        file:Files? = null,
        onClose:() -> Unit,
        onSave: () -> Unit
){
        var collectionName by remember {
                mutableStateOf("")
        }
        var collectionCategory by remember {
                mutableStateOf("")
        }


        Dialog(onDismissRequest = { onClose }) {

                Surface(
                        modifier = Modifier
                                .width(300.dp)
                                .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                ) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                TextField(
                                        value = collectionName,
                                        onValueChange = {collectionName = it},
                                        label = { Text(text = "Enter Board Name:")}
                                )
                                Spacer(modifier = Modifier.padding(10.dp))

                                TextField(
                                        value = collectionCategory,
                                        onValueChange = {collectionCategory = it},
                                        label = { Text(text = "Enter Board Category:")}
                                )
                        }

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) {
                                TextButton(onClick = onClose) {
                                        Text("Cancel")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(
                                        onClick = {
                                                onSave()
                                                onClose()
                                        }
                                ) {
                                        Text("Save")
                                }
                        }
                }

        }
}