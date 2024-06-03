package com.example.collectibles_den

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.collectibles_den.databinding.ActivityResetBinding
import com.google.android.material.textfield.TextInputEditText

class Reset : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //assigning binding
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //function to valid input fields
        emailFocusListener()
        passwordFocusListener()

        //declaring variable text field and button
        val email = findViewById<TextInputEditText>(R.id.reset_email)
        val password = findViewById<TextInputEditText>(R.id.reset_new_password)
        val confirmPassword = findViewById<TextInputEditText>(R.id.reset_confirm_password)
        val resetBtn = findViewById<Button>(R.id.reset_button)

        //onclick function
        resetBtn.setOnClickListener {

            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val confirmPasswordInput = confirmPassword.text.toString()

            //if to handle user input
            if(emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()){

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Invalid Input")
                    setMessage("Please fill the reset form")
                    setPositiveButton("OK", null)

                    //create and show dialog
                    create()
                    show()
                }
            }
            else if(confirmPasswordInput != passwordInput){

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Invalid Input")
                    setMessage("Please Ensure that password's match")
                    setPositiveButton("OK", null)

                    //create and show dialog
                    create()
                    show()
                }

                //changing helper text
                binding.resetConfirmPasswordContainer.helperText = "Password do not match"
            }
            else {

                //alert dialog to notify user
                AlertDialog.Builder(this).apply {

                    setTitle("Success")
                    setMessage("reset success")
                    setPositiveButton("OK", null)

                    //create and show dialog
                    create()
                    show()
                }

                //clearing the form
                binding.resetEmail.text = null
                binding.resetNewPassword.text = null
                binding.resetConfirmPassword.text = null
                binding.resetEmailContainer.helperText = null
                binding.resetNewPasswordContainer.helperText = null
                binding.resetConfirmPasswordContainer.helperText = null
            }

        }

    }

    //function to validate password
    private fun passwordFocusListener() {

        //focus listener
        binding.resetNewPassword.setOnFocusChangeListener { _, focused ->

            if(!focused){

                binding.resetNewPasswordContainer.helperText = validPassword()
            }

        }

    }

    //to validate password
    private fun validPassword(): String? {

        //declaring a variable for password
        val password = binding.resetNewPassword.text.toString()

        //if statement to check password
        if(password.length < 8){

            return "Minimum 8 characters"
        }
        if(!password.matches(".*[A-Z].*".toRegex())){

            return "Must Contain Uppercase letter"
        }
        if(!password.matches(".*[a-z].*".toRegex())){

            return "Must Contain lowercase letter"
        }
        if(!password.matches(".*[@#\$%^&+=].*".toRegex())){

            return "Must Contain special characters"
        }
        if(!password.matches(".*[0-9].*".toRegex())){

            return "Must Contain at a number"
        }

        //return statement
        return null
    }

    //to validate email
    private fun validEmail(): String? {

        //declaring a variable for email
        val email = binding.resetEmail.text.toString()

        //if email address domain is invalid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            //returning a string message
            return "Invalid Email address"
        }

        //return statement
        return null
    }

    //function to valid email
    private fun emailFocusListener() {

        //focus listener
        binding.resetEmail.setOnFocusChangeListener { _, focused ->

            if(!focused){

                binding.resetEmailContainer.helperText = validEmail()
            }
        }
    }
}