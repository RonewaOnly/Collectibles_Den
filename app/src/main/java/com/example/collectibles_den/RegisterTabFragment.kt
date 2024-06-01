package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
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


class RegisterTabFragment : Fragment() {
    private lateinit var  registerViewModel: AuthorizationViewModel
    private lateinit var databaseViewModel: DatabaseViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register_tab, container, false)
        val firstname = view.findViewById<EditText>(R.id.signup_firstName)
        val lastname = view.findViewById<EditText>(R.id.signup_lastName)
        val email = view.findViewById<EditText>(R.id.signup_email)
        val password = view.findViewById<EditText>(R.id.signup_password)
        val confirmPassword = view.findViewById<EditText>(R.id.signup_confirm)

        // Initialize the ViewModels
        val factory = DatabaseViewModelFactory(requireContext())
        databaseViewModel = ViewModelProvider(this, factory).get(DatabaseViewModel::class.java)
        registerViewModel = ViewModelProvider(
            this,
            AuthorizationViewModelFactory(requireContext(), databaseViewModel)
        )[AuthorizationViewModel::class.java]
        // onclick function
        view.findViewById<Button>(R.id.signup_button).setOnClickListener {
            val firstnameInput = firstname.text.toString()
            val lastnameInput = lastname.text.toString()
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val ConfirmpasswordInput = confirmPassword.text.toString()
            registerViewModel.registration(firstnameInput,lastnameInput,emailInput,passwordInput,ConfirmpasswordInput)
        }

        // Observe the Registration result
        registerViewModel.registerResult.observe(viewLifecycleOwner){ isSuccess ->

            if(isSuccess) {
                val intent = Intent(this@RegisterTabFragment.requireContext(), Login::class.java)
                startActivity(intent)
                Toast.makeText(activity, "Registered in successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

}