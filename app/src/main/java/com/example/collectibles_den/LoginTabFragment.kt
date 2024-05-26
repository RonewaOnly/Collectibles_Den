package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collectibles_den.Logic.AuthorizationViewModel
import com.example.collectibles_den.Logic.AuthorizationViewModelFactory
import com.example.collectibles_den.Logic.DatabaseViewModel
import com.example.collectibles_den.Logic.DatabaseViewModelFactory
import com.example.collectibles_den.Pages.MainActivity

class LoginTabFragment : Fragment() {
    private lateinit var loginViewModel: AuthorizationViewModel
    private lateinit var databaseViewModel: DatabaseViewModel
    lateinit var id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //User Id
        val userID = collectiblesDenApp.getUserID()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)
        val email = view.findViewById<EditText>(R.id.login_email)
        val password = view.findViewById<EditText>(R.id.login_password)

        // Logging to check if views are found
        Log.d("LoginTabFragment", "Email EditText: $email")
        Log.d("LoginTabFragment", "Password EditText: $password")

        // Initialize the ViewModels
        val factory = DatabaseViewModelFactory(requireContext())
        databaseViewModel = ViewModelProvider(this, factory).get(DatabaseViewModel::class.java)
        loginViewModel = ViewModelProvider(this, AuthorizationViewModelFactory(requireContext(), databaseViewModel)).get(AuthorizationViewModel::class.java)
        // Set the click listener for the login button
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            loginViewModel.login(emailInput, passwordInput)
        }

        // Observe the login result
        loginViewModel.loginResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                // Redirect to MainActivity upon successful login
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                // Observe userID directly
                loginViewModel.databaseViewModel.userID.observe(viewLifecycleOwner) { userID ->
                    if (userID != null) {
                        id = userID
                    }
                    Toast.makeText(activity, "Logged in successfully: ID's: $userID", Toast.LENGTH_SHORT).show()

                    Toast.makeText(activity," I took : $userID",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}

