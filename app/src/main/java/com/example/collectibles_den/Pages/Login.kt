package com.example.collectibles_den.Pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun  Login() {
    Column {
        //
        UserLogin()
    }
}
@Preview
@Composable
fun UserLogin(){
    Text(text = "Login")
}