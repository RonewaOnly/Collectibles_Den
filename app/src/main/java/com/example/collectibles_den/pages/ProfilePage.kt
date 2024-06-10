package com.example.collectibles_den.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.DefaultValuesClass
import com.example.collectibles_den.R
import com.example.collectibles_den.data.UserData
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory

@Suppress("unused")
var getData = DefaultValuesClass()//This variable used to call the list with the data

@Preview(showBackground = true)
@Composable
fun ProfileAccount(
    viewModel: DatabaseViewModel = viewModel(
        factory = DatabaseViewModelFactory(
            context = LocalContext.current
        )
    )
) {
    val userID = CollectiblesDenApp.getUserID()
    val collectionsState = remember { mutableStateOf<List<UserData>>(emptyList()) }

    LaunchedEffect(userID) {
        userID?.let { uid ->
            viewModel.getUser(uid) { user ->
                collectionsState.value = user
            }
        }
    }

    Text(
        text = "Account",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(15.dp)
    )
    Spacer(modifier = Modifier.padding(5.dp))
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState(), true),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.padding(25.dp))
        ProfileSection(collectionsState.value)
    }
}

@Composable
fun ProfileSection(users: List<UserData>) {
    var isPopProfile by remember { mutableStateOf(false) }
    var isRate by remember { mutableStateOf(false) }
    var isHelp by remember { mutableStateOf(false) }
    var iaIssue by remember { mutableStateOf(false) }
    var isTerm by remember { mutableStateOf(false) }

    if (!isPopProfile) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .background(
                    colorResource(id = R.color.Thistle), shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp)
                .border(1.dp, Color.Transparent, RoundedCornerShape(10.dp))


        ) {//Mini Version for personal Details
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(users) { user ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .border(2.dp, Color.Red)
                            .width(55.dp)
                            .height(55.dp)
                            .background(Color.LightGray),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {//The circle
                        user.username?.let { username ->
                            Text(
                                text = username.substring(0, 2),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.clip(RoundedCornerShape(50))
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(280.dp)

                    ) {
                        user.firstname?.let { username ->
                            Text(text = username, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        user.email?.let { email ->
                            Text(
                                text = email,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )
                        }
                    }

                    Icon(imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .clickable {
                                isPopProfile = true
                            }
                            .align(Alignment.Start))
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Red)
                    .border(2.dp, Color.Red)
                    .fillMaxWidth()
            )
            Text(
                text = "Extra memory",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp, 0.dp, 0.dp)
            )
        }


        Spacer(modifier = Modifier.padding(12.dp))
        //Contact Form
        Column(
            modifier = Modifier
                .width(400.dp)
                .background(colorResource(id = R.color.Thistle), shape = RoundedCornerShape(15.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,

            ) {
            Text(
                text = "Contact",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 2.dp, 0.dp, 0.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color.Red)
            )

            TextButton(
                onClick = { isRate = true },
                shape = RoundedCornerShape(25),
                border = BorderStroke(1.dp, Color.Transparent),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text)),
                modifier = Modifier
                    .width(160.dp)
                    .padding(10.dp, 0.dp)
            ) {
                Text(text = "Rate Our app", color = Color.White)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            TextButton(
                onClick = { isHelp = true },
                shape = RoundedCornerShape(25),
                border = BorderStroke(1.dp, Color.Transparent),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text)),
                modifier = Modifier
                    .width(160.dp)
                    .padding(10.dp, 0.dp)
            ) {
                Text(text = "Help", color = Color.White)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            TextButton(
                onClick = { iaIssue = true },
                shape = RoundedCornerShape(25),
                border = BorderStroke(1.dp, Color.Transparent),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text)),
                modifier = Modifier
                    .width(160.dp)
                    .padding(10.dp, 0.dp)
            ) {
                Text(text = "Report an Issue", color = Color.White)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            TextButton(
                onClick = { isTerm = true },
                shape = RoundedCornerShape(25),
                border = BorderStroke(1.dp, Color.Transparent),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text)),
                modifier = Modifier
                    .width(160.dp)
                    .padding(10.dp, 0.dp)
            ) {
                Text(text = "Term and Condition", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }
        if (isRate) {
            RatePop(onClose = {
                isRate = false
            })
        }
        if (isHelp) {
            HelpPop(onClose = {
                isHelp = false
            })
        }
        if (iaIssue) {
            Issue(onClose = {
                iaIssue = false
            })
        }
        if (isTerm) {
            TermsPop(onClose = {
                isTerm = false
            })
        }
        Spacer(modifier = Modifier.padding(12.dp))
        Column(
            modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 0.dp)
                .background(colorResource(id = R.color.Thistle), shape = RoundedCornerShape(15.dp)),

            ) {
            Text(
                text = "Guild line Video's ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp, 10.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp, 1.dp)
                    .border(2.dp, Color.Red)
            )
            Text(text = "Video's will be coming soon", modifier = Modifier.padding(10.dp, 10.dp))
        }

    } else {
        //Text(text = "Clicked")
        val currentUser = users.firstOrNull()
        currentUser?.let { user ->
            FullPersonalProfile(user = user, userID = user.id ?: "", onClose = {
                isPopProfile = false
            }, onSave = { updatedUser ->
                // Update the user in the list
                val index = users.indexOfFirst { it.id == updatedUser.id }
                if (index != -1) {
                    val updatedUsers = users.toMutableList()
                    updatedUsers[index] = updatedUser
                    isPopProfile = false
                }
            })
        }
    }
}

@Composable
fun FullPersonalProfile(
    user: UserData, userID: String, onClose: () -> Unit, onSave: (UserData) -> Unit
) {
    var firstname by remember { mutableStateOf(user.firstname ?: "") }
    var lastname by remember { mutableStateOf(user.lastname ?: "") }
    var username by remember { mutableStateOf(user.username ?: "") }
    var email by remember { mutableStateOf(user.email ?: "") }
    var password by remember { mutableStateOf(user.password ?: "") }

    var isEnable by remember { mutableStateOf(false) } //Will enable for changing details
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(400.dp)
            .background(colorResource(id = R.color.Thistle), shape = RoundedCornerShape(15.dp))
            .border(1.dp, color = Color.Blue, shape = RoundedCornerShape(15.dp))
            .padding(29.dp),

        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(5.dp)
                .clickable {
                    onClose()
                })//Close
        Icon(imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.End)
                .padding(5.dp, 0.dp, 2.dp, 0.dp)
                .clickable {
                    isEnable = true
                })// Live Edit
        LazyColumn(
            modifier = Modifier
                .height(400.dp)
                .width(400.dp)
                .padding(10.dp)
        ) {
            item {
                TextField(value = firstname,
                    onValueChange = { firstname = it },
                    enabled = isEnable,
                    label = { Text(text = "Enter Firstname: ") })
                TextField(value = lastname,
                    onValueChange = { lastname = it },
                    enabled = isEnable,
                    label = { Text(text = "Enter Lastname: ") })
                TextField(value = username,
                    onValueChange = { username = it },
                    enabled = isEnable,
                    label = { Text(text = "Enter Username: ") })
                TextField(value = email,
                    onValueChange = { email = it },
                    enabled = isEnable,
                    label = { Text(text = "Enter Email: ") })
                TextField(value = password,
                    onValueChange = { password = it },
                    enabled = isEnable,
                    label = { Text(text = "Enter Password: ") },
                    minLines = 2
                )

                Button(modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp, 10.dp, 0.dp, 0.dp),
                    onClick = {

                        val updatedProfile = updateUserProfile(
                            userID, firstname, lastname, username, email, password
                        )

                        onSave(updatedProfile)
                        Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                        isEnable = false
                    }) {

                    Text(text = "Save", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

fun updateUserProfile(
    userId: String,
    newFirstName: String? = null,
    newLastName: String? = null,
    newUsername: String? = null,
    newEmail: String? = null,
    newPassword: String? = null
): UserData {
    return UserData(
        id = userId,
        firstname = newFirstName,
        lastname = newLastName,
        username = newUsername,
        email = newEmail,
        password = newPassword
    )
}

@Composable
fun RatePop(onClose: () -> Unit) {
    var comment by remember {
        mutableStateOf("")
    }
    var select by remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = { onClose() }) {
        Surface {
            Column (modifier = Modifier.padding(20.dp) .border(1.dp, Color.Blue, shape = RoundedCornerShape(10.dp)),   verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {


                for (i in 1..5) {
                    RadioButton(
                        modifier = Modifier.padding(5.dp, 0.dp, 240.dp),
                        selected = select, onClick = {
                            select = true
                        }, colors = RadioButtonDefaults.colors(Color.Yellow)

                    )
                }
                Spacer(modifier = Modifier.padding(18.dp))
                TextField(modifier = Modifier.border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp)), value = comment,
                    onValueChange = { comment = it },
                    minLines = 5,
                    maxLines = 10,
                    label = { Text(text = "Your feedback would helps us a lot", fontSize = 14.sp) })

                Spacer(modifier = Modifier.padding(18.dp))
                Button(onClick = { onClose() })  {
                    Text(text = "Close")
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

    }
}

@Composable
fun HelpPop(onClose: () -> Unit) {
    var problem by remember {
        mutableStateOf("")
    }
    var problemsDescription by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onClose() }) {
        Surface {
            Column () {
                TextField(value = problem, onValueChange = { problem = it })
                Spacer(modifier = Modifier.padding(18.dp))
                TextField(value = problem, onValueChange = { problemsDescription = it })
                Spacer(modifier = Modifier.padding(18.dp))
                Button(onClick = { onClose() }) {
                    Text(text = "Send")
                }
            }
        }
    }
}

@Composable
fun Issue(onClose: () -> Unit) {
    var issue by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onClose() }) {
        Surface {
            Column {
                Text(text = "Have you been experiencing some problems with app ")
                TextField(value = issue, onValueChange = { issue = it })
                Spacer(modifier = Modifier.padding(18.dp))
                Button(onClick = { onClose() }) {
                    Text(text = "Send")
                }

            }
        }
    }
}

@Composable
fun TermsPop(onClose: () -> Unit) {

    Dialog(onDismissRequest = { onClose() }) {
        Surface {
            Column (modifier = Modifier.padding(20.dp) .border(1.dp, Color.Blue, shape = RoundedCornerShape(10.dp)),   verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "bbbb", fontSize = 14.sp,  modifier = Modifier.fillMaxWidth() .padding(20.dp, 5.dp))

                Spacer(modifier = Modifier.padding(18.dp))
                Row {
                    Button(onClick = { onClose() }) {
                        Text(text = "Agree")
                    }
                    Spacer(modifier = Modifier.padding(18.dp))
                    Button(onClick = { onClose() }) {
                        Text(text = "Disagree")
                    }

                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

    }
}