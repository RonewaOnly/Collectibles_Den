package com.example.collectibles_den.Logic

import android.util.Log
import com.example.collectibles_den.Data.UserData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class DatabaseViewModel {

    private val database: DatabaseReference = Firebase.database.reference

    interface LoginValidationCallback {
        fun onUserFound()
        fun onUserNotFound()
        fun onError(error: DatabaseError)
    }

    fun loginValidation(userEmail: String, userPassword: String, callback: LoginValidationCallback) {
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserFound = false
                for (loginSnapshot in snapshot.children) {
                    // Retrieve user data as a Map
                    val userDataMap = loginSnapshot.value as? Map<*, *>

                    // Check if userDataMap is not null and contains required fields
                    if (userDataMap != null && userDataMap.containsKey("email") && userDataMap.containsKey("password")) {
                        val email = userDataMap["email"] as String
                        val password = userDataMap["password"] as String

                        // Check if email and password match
                        if (email == userEmail && password == userPassword) {
                            isUserFound = true
                            break
                        }
                    }
                }
                if (isUserFound) {
                    callback.onUserFound()
                } else {
                    callback.onUserNotFound()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }


    fun registrationTaker(firstname: String, lastname: String, email: String, password: String) {
        val key = database.child("Users").push().key
        val registerDetails = UserData(key, firstname, lastname, email, email, password)

        key?.let {
            database.child("Users").child(it).setValue(registerDetails)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "Registration Successful")
                }.addOnFailureListener { error ->
                    Log.e("RegistrationActivity", "Registration Failed: $error")
                }
        }
    }
}