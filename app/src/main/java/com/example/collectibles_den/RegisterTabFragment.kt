package com.example.collectibles_den

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.collectibles_den.Pages.MainActivity


class RegisterTabFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register_tab, container, false)

        // onclick function
        view.findViewById<Button>(R.id.signup_button).setOnClickListener {

            val intent = Intent(this@RegisterTabFragment.requireContext(), Login::class.java)
            startActivity(intent)
        }

        return view
    }

}