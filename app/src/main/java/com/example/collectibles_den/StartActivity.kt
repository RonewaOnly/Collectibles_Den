package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        //hiding action bar
        supportActionBar?.hide()

    }

    //onclick function to navigate to register activity
    fun getStarted(@Suppress("UNUSED_PARAMETER") view:View) {

        //using intent class to switch activities
        val intent = Intent(this@StartActivity, Register::class.java)
        startActivity(intent)
    }

    //onclick function to navigate to login activity
    fun userLogin(@Suppress("UNUSED_PARAMETER") view: View) {

        //using intent class to switch activities
        val intent = Intent(this@StartActivity, Login::class.java)
        startActivity(intent)

    }
}