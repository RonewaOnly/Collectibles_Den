package com.example.collectibles_den.Pages

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.collectibles_den.Data.UserData
import com.example.collectibles_den.DefaultValuesClass

var getData = DefaultValuesClass()//This variable used to call the list with the data
@Preview(showBackground = true)
@Composable
fun ProfileAccount(){
        Column(
                modifier = Modifier.verticalScroll(rememberScrollState(),true)
        ) {
                Text(text = "Account")
                ProfileSection()
                //FullPersonalProfile(user = getData.userDummy, userID = getData.userDummy[0].customerId)
        }
}
@Composable
fun ProfileSection(){
        var isPopProfile by remember {
                mutableStateOf(false)
        } 
        
        if (!isPopProfile){
                Column(
                        modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray)
                                .padding(10.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                ) {//Mini Version for personal Details
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                                Column(
                                        modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .border(2.dp, Color.White)
                                                .width(55.dp)
                                                .height(55.dp)
                                                .background(
                                                        Color.LightGray
                                                ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {//The circle
                                        Text(
                                                text = "Us",
                                                color = Color.White,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                        )
                                }
                                Column {
                                        Text(text = "Username",
                                                color = Color.White,
                                        )
                                        Text(
                                                text = "Email",
                                                color = Color.White,
                                                modifier = Modifier.fillMaxWidth(0.7f)
                                        )
                                }

                                Icon(
                                        imageVector = Icons.Default.SubdirectoryArrowRight,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier =  Modifier.clickable {
                                                isPopProfile = true
                                        }
                                )
                        }

                        HorizontalDivider(modifier = Modifier.padding(10.dp))

                        Text(text = "Extra memory")
                }
                Spacer(modifier = Modifier.padding(12.dp))
                //Contact Form
                Column(
                        modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray)
                                .padding(10.dp)
                        ,
                        horizontalAlignment = Alignment.Start
                ) {
                        Text(text = "Contact",color=Color.White,modifier = Modifier.fillMaxWidth())
                        HorizontalDivider(modifier = Modifier.padding(10.dp))

                        TextButton(onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(25),
                                border = BorderStroke(1.dp, Color.Black),
                                colors = ButtonDefaults.buttonColors(Color.LightGray),
                                modifier = Modifier.width(160.dp)
                        ) {
                                Text(text = "Rate Our app")
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        TextButton(onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(25),
                                border = BorderStroke(1.dp, Color.Black),
                                colors = ButtonDefaults.buttonColors(Color.LightGray),
                                modifier = Modifier.width(160.dp)
                        ) {
                                Text(text = "Help")
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        TextButton(onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(25),
                                border = BorderStroke(1.dp, Color.Black),
                                colors = ButtonDefaults.buttonColors(Color.LightGray),
                                modifier = Modifier.width(160.dp)
                        ) {
                                Text(text = "Report an Issue")
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        TextButton(onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(25),
                                border = BorderStroke(1.dp, Color.Black),
                                colors = ButtonDefaults.buttonColors(Color.LightGray),
                                modifier = Modifier.width(160.dp)
                        ) {
                                Text(text = "Term and Condition")
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                }
                Spacer(modifier = Modifier.padding(12.dp))
                Column (
                        modifier = Modifier.fillMaxWidth(),

                        ){
                        Text(text = "Guild line Video's ", fontWeight = FontWeight.Bold)

                        Text(text = "Video's will be coming soon")
                }
        }else{
           //Text(text = "Clicked")
                FullPersonalProfile(user = getData.userDummy, userID = getData.userDummy[0].customerId, onClose = {
                        isPopProfile = false
                })
        }
        
}

@Composable
fun FullPersonalProfile(user: List<UserData>,userID:String,onClose: () ->Unit){//This function will be displaying the profile of the user..
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        var isEnable by remember { mutableStateOf(false) } //Will enable for changing details
        val context = LocalContext.current
        Column(
                modifier = Modifier
                        .width(650.dp)
                        .background(Color.LightGray),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
        ) {
                Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                                .align(Alignment.Start).clickable {
                                        onClose()
                                }
                )//Close
                Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                        isEnable = true
                                }
                )//Live Edit
                LazyColumn(
                        modifier = Modifier
                                .height(400.dp)
                                .width(400.dp)
                                .padding(10.dp)
                ) {
                        items(getData.userDummy) { user ->
                                TextField(
                                        value = if (!isEnable) user.firstname else firstname,
                                        onValueChange = {
                                                if (isEnable) firstname = it else user.firstname =
                                                        it
                                        }, enabled = isEnable,
                                        label = { Text(text = "Enter Firstname: ")}
                                )

                                TextField(
                                        value = if (!isEnable) user.lastname else lastname,
                                        onValueChange = {
                                                if (isEnable) lastname = it else user.lastname = it
                                        }, enabled = isEnable,
                                        label = { Text(text = "Enter Lastname: ")}
                                )
                                TextField(
                                        value = if (!isEnable) user.username else username,
                                        onValueChange = {
                                                if (isEnable) username = it else user.username = it
                                        }, enabled = isEnable,
                                        label = { Text(text = "Enter Username: ")}
                                )
                                TextField(
                                        value = if (!isEnable) user.email else email,
                                        onValueChange = {
                                                if (isEnable) email = it else user.email = it
                                        }, enabled = isEnable,
                                        label = { Text(text = "Enter Email: ")}
                                )
                                TextField(
                                        value = if (!isEnable) user.password else password,
                                        onValueChange = {
                                                if (isEnable) password = it else user.password = it
                                        }, enabled = isEnable,
                                        label = { Text(text = "Enter Password: ")},
                                        minLines = 2
                                )

                                Button(onClick = {
                                        Toast.makeText(context,"Not save yet",Toast.LENGTH_LONG).show()
                                }) {
                                        Text(text = "Save")
                                }


                        }
                }
        }
}
fun updateUserProfile(//This function will be used to update the user profile
        userId: String,
        newFirstName: String? = null,
        newLastName: String? = null,
        newEmail: String? = null,
        newPassword: String?=null

): List<UserData> {
        return getData.userDummy.map { profile ->
                if (profile.customerId == userId) {
                        profile.copy(
                                firstname = newFirstName ?: profile.firstname,
                                lastname = newLastName ?: profile.lastname,
                                email = newEmail ?: profile.email,
                                password = newPassword?: profile.password

                        )
                } else {
                        profile
                }
        }
}
/*//This is an example of a card
@Composable
fun CardExample() {
        Card(
                modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
                Column(
                        modifier = Modifier
                                .background(Color.White)
                                .padding(16.dp)
                ) {
                        Image(
                                painter = rememberAsyncImagePainter("https://www.example.com/image.jpg"),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = "Card Title",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                                text = "Card content goes here. This is an example of a card in Jetpack Compose.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) {
                                TextButton(onClick = { /* Perform some action */ }) {
                                        Text(text = "ACTION 1")
                                }
                                TextButton(onClick = { /* Perform some action */ }) {
                                        Text(text = "ACTION 2")
                                }
                        }
                }
        }
}
*/
