package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.collectibles_den.Pages.MainActivity

class LoginTabFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        //onclick function
        view.findViewById<Button>(R.id.login_button).setOnClickListener{
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(activity,"Logged in successful", Toast.LENGTH_SHORT).show();

        }

        return view
    }

}