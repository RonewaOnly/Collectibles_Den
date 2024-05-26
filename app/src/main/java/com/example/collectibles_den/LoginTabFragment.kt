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
import androidx.fragment.app.viewModels
import com.example.collectibles_den.Logic.AuthorizationViewModel
import com.example.collectibles_den.Pages.MainActivity

class LoginTabFragment : Fragment() {
    private val loginViewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        val email = view.findViewById<EditText>(R.id.login_email)
        val password = view.findViewById<EditText>(R.id.login_password)

        // Logging to check if views are found
        Log.d("LoginTabFragment", "Email EditText: $email")
        Log.d("LoginTabFragment", "Password EditText: $password")
        //Log.d("LoginTabFragment", "Login Button: $loginButton")

        // Set the click listener for the login button
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            loginViewModel.login(emailInput, passwordInput)
        }

        // Observe the login result
        loginViewModel.loginResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
