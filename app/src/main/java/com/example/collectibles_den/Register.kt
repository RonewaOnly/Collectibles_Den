package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collectibles_den.databinding.ActivityRegisterBinding
import com.example.collectibles_den.logic.AuthorizationViewModel
import com.example.collectibles_den.logic.AuthorizationViewModelFactory
import com.example.collectibles_den.logic.DatabaseViewModel
import com.example.collectibles_den.logic.DatabaseViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class Register : AppCompatActivity() {

    // Register model
    private lateinit var registerViewModel: AuthorizationViewModel

    // Binding
    private lateinit var binding: ActivityRegisterBinding

    // Global variables
    private lateinit var firstnameInput: String
    private lateinit var lastnameInput: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Assign binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DatabaseViewModel with factory
        val databaseFactory = DatabaseViewModelFactory(this)
        val databaseViewModel = ViewModelProvider(this, databaseFactory)[DatabaseViewModel::class.java]

        // Initialize AuthorizationViewModel with factory
        val authFactory = AuthorizationViewModelFactory(this, databaseViewModel)
        registerViewModel = ViewModelProvider(this, authFactory)[AuthorizationViewModel::class.java]

        // Function to validate input fields
        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()

        // Declaring text field inputs
        val firstname = findViewById<TextInputEditText>(R.id.signup_firstName)
        val lastname = findViewById<TextInputEditText>(R.id.signup_lastName)
        val email = findViewById<TextInputEditText>(R.id.signup_email)
        val password = findViewById<TextInputEditText>(R.id.signup_password)
        val confirmPassword = findViewById<TextInputEditText>(R.id.signup_confirm_password)
        val btnRegisterAccount = findViewById<Button>(R.id.signup_button)
        val btnLogin = findViewById<TextView>(R.id.btn_login)

        // OnClick function to register account
        btnRegisterAccount.setOnClickListener {
            // Assigning text input fields
            firstnameInput = firstname.text.toString()
            lastnameInput = lastname.text.toString()
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val confirmPasswordInput = confirmPassword.text.toString()

            // If statement to handle empty fields and mismatched passwords
            if (firstnameInput.isEmpty() || lastnameInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                // Alert dialog to notify user
                AlertDialog.Builder(this).apply {
                    setTitle("Invalid Input")
                    setMessage("Please fill in all the fields")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            } else if (confirmPasswordInput != passwordInput) {
                // Alert dialog to notify user
                AlertDialog.Builder(this).apply {
                    setTitle("Invalid Input")
                    setMessage("Passwords do not match")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            } else {
                // Clearing form after successful validation
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

                Toast.makeText(this, "Hello $firstnameInput $lastnameInput", Toast.LENGTH_SHORT).show()
                registerViewModel.registration(firstnameInput, lastnameInput, emailInput, passwordInput, confirmPasswordInput)
            }
        }

        // OnClick function to switch to login activity
        btnLogin.setOnClickListener {
            // Intent to switch activities
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
    }

    // Function to validate last name
    private fun lastNameFocusListener() {
        binding.signupLastName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.signupLastNameContainer.helperText = validLastName()
            }
        }
    }

    private fun validLastName(): String? {
        val lastName = binding.signupLastName.text.toString()
        return when {
            lastName.length < 3 -> "Minimum 3 characters"
            lastName.matches(".*[0-9].*".toRegex()) -> "Cannot contain numbers"
            lastName.matches(".*[@#\$%^&+=].*".toRegex()) -> "Cannot contain special characters"
            else -> null
        }
    }

    // Function to validate first name
    private fun firstNameFocusListener() {
        binding.signupFirstName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.signupFirstNameContainer.helperText = validFirstName()
            }
        }
    }

    private fun validFirstName(): String? {
        val firstName = binding.signupFirstName.text.toString()
        return when {
            firstName.length < 3 -> "Minimum 3 characters"
            firstName.matches(".*[0-9].*".toRegex()) -> "Cannot contain numbers"
            firstName.matches(".*[@#\$%^&+=].*".toRegex()) -> "Cannot contain special characters"
            else -> null
        }
    }

    // Function to validate password
    private fun passwordFocusListener() {
        binding.signupPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.signupPasswordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val password = binding.signupPassword.text.toString()
        return when {
            password.length < 8 -> "Minimum 8 characters"
            !password.matches(".*[A-Z].*".toRegex()) -> "Must contain an uppercase letter"
            !password.matches(".*[a-z].*".toRegex()) -> "Must contain a lowercase letter"
            !password.matches(".*[@#\$%^&+=].*".toRegex()) -> "Must contain a special character"
            !password.matches(".*[0-9].*".toRegex()) -> "Must contain a number"
            else -> null
        }
    }

    // Function to listen to email input
    private fun emailFocusListener() {
        binding.signupEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.signupEmailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val email = binding.signupEmail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email address"
        } else null
    }
}

