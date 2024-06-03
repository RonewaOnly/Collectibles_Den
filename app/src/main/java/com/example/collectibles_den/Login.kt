package com.example.collectibles_den

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collectibles_den.R.id.login_password
import com.example.collectibles_den.logic.AuthorizationViewModel
import com.example.collectibles_den.logic.AuthorizationViewModelFactory
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.example.collectibles_den.pages.MainActivity
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {

    //declaring variables
    private lateinit var loginViewModel: AuthorizationViewModel
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var id: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //hide toolbar
        supportActionBar?.hide()

        //user id
        val userID = CollectiblesDenApp.getUserID()

        //declaring input fields
        val email = findViewById<TextInputEditText>(R.id.login_email)
        val password = findViewById<TextInputEditText>(login_password)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val registerBtn = findViewById<TextView>(R.id.btn_register)
        val resetBtn = findViewById<TextView>(R.id.btn_reset)

        // Logging to check if views are found
        Log.d("Login", "Email TextInputEditText: $email")
        Log.d("Login","Password TextInputEditText: $password")

        // Initialize the DatabaseViewModel
        val databaseFactory = DatabaseViewModelFactory(this)
        databaseViewModel = ViewModelProvider(this, databaseFactory)[DatabaseViewModel::class.java]

        // Initialize the AuthorizationViewModel with factory
        val authFactory = AuthorizationViewModelFactory(this, databaseViewModel)
        loginViewModel = ViewModelProvider(this, authFactory)[AuthorizationViewModel::class.java]

        //set onclick function for login button
        loginBtn.setOnClickListener {

            //declaring temp variables
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            //if to check empty text input fields
            if (emailInput.isEmpty() || passwordInput.isEmpty()){

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Invalid Input")
                    setMessage("Please fill the login form")
                    setPositiveButton("OK", null)

                    //creates and show the dialog box
                    create()
                    show()

                }
            } else {
                //model to login
                loginViewModel.login(emailInput, passwordInput)
            }
        }

        //set onclick function for register button
        registerBtn.setOnClickListener {

            //using intent to switch activities
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        //set onclick function for reset button
        resetBtn.setOnClickListener {

            //using intent to switch activities
            val intent = Intent(this@Login, Reset::class.java)
            startActivity(intent)
        }

        // Uncomment and adjust the following section if needed to observe the login result

        // // Observe the login result
         loginViewModel.loginResult.observe(this) { isSuccess ->
             if (isSuccess) {
                 // Redirect to MainActivity upon successful login
                 val intent = Intent(this@Login, MainActivity::class.java)
                 startActivity(intent)

                 // Observe userID directly
                 loginViewModel.databaseViewModel.userID.observe(this) { userID ->
                     if (userID != null) {
                         id = userID
                     }

                     //alert dialog to notify user
                     AlertDialog.Builder(this).apply {
                         setTitle("Success")
                         setMessage("Hi welcome to Collectable Den\n" +
                                 "Logged in successfully: ID's: $userID\n" +
                                 "I took : $userID")
                         setPositiveButton("OK") { _, _ ->
                             //intent to switch activities
                             val intent = Intent(this@Login, MainActivity::class.java)
                             startActivity(intent)
                         }
                         //creates and show the dialog box
                         create()
                         show()
                     }
                 }
             } else {
                 //alert dialog to notify user
                 AlertDialog.Builder(this).apply {
                     setTitle("Login Failed")
                     setMessage("Email or Password is incorrect")
                     setPositiveButton("OK", null)
                     //creates and show the dialog box
                     create()
                     show()
                 }
             }
         }
    }
}
