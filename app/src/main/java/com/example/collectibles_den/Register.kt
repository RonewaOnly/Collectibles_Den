package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.collectibles_den.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText

class Register : AppCompatActivity() {

    //register model
    //private val registerViewModel: AuthorizationViewModel by viewModels()

    //binding
    private lateinit var binding:ActivityRegisterBinding

    //global variables
    private lateinit var firstnameInput : String
    private lateinit var lastnameInput : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //assign binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //declaring text field inputs
        val firstname = findViewById<TextInputEditText>(R.id.signup_firstName)
        val lastname = findViewById<TextInputEditText>(R.id.signup_lastName)
        val email = findViewById<TextInputEditText>(R.id.signup_email)
        val password = findViewById<TextInputEditText>(R.id.signup_password)
        val confirmPassword = findViewById<TextInputEditText>(R.id.signup_confirm_password)
        val btnRegisterAccount = findViewById<Button>(R.id.signup_button)
        val btnLogin = findViewById<TextView>(R.id.btn_login)

        //onclick function to register account
        btnRegisterAccount.setOnClickListener {

            //assigning text input fields
            firstnameInput = firstname.text.toString()
            lastnameInput = lastname.text.toString()
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val confirmPasswordInput = confirmPassword.text.toString()

            //if statement to handle empty and if password fields do not match
            if(firstnameInput.isEmpty() || lastnameInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()){

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Invalid Input")
                    setMessage("Please fill the registration form")
                    setPositiveButton("OK",null)

                    //create and show alert dialog box
                    create()
                    show()
                }



            }else if (confirmPasswordInput != passwordInput){

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Invalid Input")
                    setMessage("Please ensure that both of your passwords match")
                    setPositiveButton("OK",null)

                    //create and show alert dialog box
                    create()
                    show()
                }


            }else{

                Toast.makeText(this, "hey $firstnameInput $lastnameInput", Toast.LENGTH_SHORT).show()
                //registerViewModel.registration(firstnameInput,lastnameInput,emailInput,passwordInput,confirmPasswordInput)
            }
        }

        //onclick function to login account activity
        btnLogin.setOnClickListener {

            //intent to switch activities
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)

        }

        // Observe the Registration result
//        registerViewModel.registerResult.observe(viewLifecycleOwner){ isSuccess ->
//
//            if(isSuccess) {
//
//                //alert dialog to notify user
//                AlertDialog.Builder(this).apply {
//
//                    setTitle("Registration Success")
//                    setMessage("Welcome!!!, $firstnameInput $lastnameInput To Collectables Den, login with your registered account and\nStart creating memories")
//                    setPositiveButton("OK"){_,_, ->
//
//                        //navigate to login class
//                        val intent = Intent(this@Register, Login::class.java)
//                        startActivity(intent)
//
//                    }
//
//                    //creates and show the dialog box
//                    create()
//                    show()
//
//                }
//
//            } else {
//
//                //alert dialog to notify user
//                AlertDialog.Builder(this).apply {
//
//                    setTitle("Registration Failed")
//                    setMessage("Please try again")
//                    setPositiveButton("OK",null)
//
//                    //creates and show the dialog box
//                    create()
//                    show()
//
//                }
//            }
//
//        }


    }
}