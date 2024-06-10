package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        //set a handler
        Handler().postDelayed({

            //intent to navigate to start activity
            val intent = Intent(this@Splash, StartActivity::class.java)
            startActivity(intent)

            //finish the method
            finish()

        }, 3000)

    }
}