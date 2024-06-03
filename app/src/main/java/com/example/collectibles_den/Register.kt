package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.collectibles_den.databinding.ActivityRegisterBinding
import com.example.collectibles_den.logic.AuthorizationViewModel
import com.google.android.material.textfield.TextInputEditText
import android.util.Patterns as Patterns

class Register : AppCompatActivity() {

    //register model
    private val registerViewModel: AuthorizationViewModel by viewModels()

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

        //function to valid input fields
        emailFocusListener()
        passwordFocusListener()
        namesFocusListener()
        lastNameFocusListener()

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

                //clearing form after successful validation
                binding.signupFirstName.text = null
                binding.signupLastName.text = null
                binding.signupEmail.text = null
                binding.signupPassword.text = null
                binding.signupConfirmPassword.text = null
                binding.signupFirstNameContainer.helperText = null
                binding.signupLastNameContainer.helperText = null
                binding.signupEmailContainer.helperText = null
                binding.signupPasswordContainer.helperText = null
                binding.signupConfirmPasswordContainer.helperText = null


                Toast.makeText(this, "hey $firstnameInput $lastnameInput", Toast.LENGTH_SHORT).show()
                registerViewModel.registration(firstnameInput,lastnameInput,emailInput,passwordInput,confirmPasswordInput)
            }
        }

        //onclick function to login account activity
        btnLogin.setOnClickListener {

            //intent to switch activities
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)

        }

//        // Observe the Registration result
//        registerViewModel.registerResult.observe(this){ isSuccess ->
//
//            if(isSuccess) {
//
//                //alert dialog to notify user
//                AlertDialog.Builder(this).apply {
//
//                    setTitle("Registration Success")
//                    setMessage("Welcome!!!, $firstnameInput $lastnameInput To Collectables Den, login with your registered account and\nStart creating memories")
//                    setPositiveButton("OK") { _, _ ->
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

    //function to valid last name
    private fun lastNameFocusListener() {

        binding.signupLastName.setOnFocusChangeListener { _, focused ->

            if(!focused){

                binding.signupLastNameContainer.helperText = validLastName()
            }
        }
    }

    private fun validLastName(): String? {

        //declaring variable for user names
        val lastName = binding.signupLastName.text.toString()

        //if statement to check names
        if(lastName.length < 3){

            return "Minimum 3 Characters"
        }
        if(lastName.matches(".*[0-9].*".toRegex())){

            return "Can not contain a number"
        }
        if(lastName.matches(".*[@#\$%^&+=].*".toRegex())){

            return "Can not contain special characters"
        }

        //return statement
        return null
    }

    //function to valid first name
    private fun namesFocusListener() {

        binding.signupFirstName.setOnFocusChangeListener { _, focused ->

            //if not focused
            if(!focused){

                binding.signupFirstNameContainer.helperText = validName()
            }

        }
    }

    private fun validName(): String? {

        //declaring variable for user names
        val firstName = binding.signupFirstName.text.toString()


        //if statement to check names
        if(firstName.length < 3){

            return "Minimum 3 Characters"
        }
        if(firstName.matches(".*[0-9].*".toRegex())){

            return "Can not contain a number"
        }
        if(firstName.matches(".*[@#\$%^&+=].*".toRegex())){

            return "Can not contain special characters"
        }

        //return statement
        return null
    }

    //function to valid password
    private fun passwordFocusListener() {

        binding.signupPassword.setOnFocusChangeListener { _, focused ->

            //if not focused
            if(!focused){

                binding.signupPasswordContainer.helperText = validPassword()
            }
        }
    }

    //function to valid password
    private fun validPassword(): String? {

        //declare a variable for password input field
        val password = binding.signupPassword.text.toString()

        //if statement to check for password characters
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


    //function to listen email input
    private fun emailFocusListener() {

        binding.signupEmail.setOnFocusChangeListener { _, focused ->

            //if not focused
            if(!focused){

                binding.signupEmailContainer.helperText = validEmail()
            }
        }
    }

    //function to valid email
    private fun validEmail(): String? {

        //declaring a variable
        val email = binding.signupEmail.text.toString()

        //if email address domain is invalid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            //returning a string message
            return "Invalid Email address"
        }

        //return statement
        return null
    }
}