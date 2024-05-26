package com.example.collectibles_den.Logic

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectibles_den.Data.UserData
import com.example.collectibles_den.collectiblesDenApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class DatabaseViewModel(private val context: Context) : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private var _userID = MutableLiveData<String?>()
    val userID: LiveData<String?>
        get() = _userID

    init {
        // Load the userID from SharedPreferences when the ViewModel is created
        _userID.value = sharedPreferences.getString("user_id", null)
    }

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
                    val userDataMap = loginSnapshot.value as? Map<*, *>
                    if (userDataMap != null && userDataMap.containsKey("email") && userDataMap.containsKey("password")) {
                        val email = userDataMap["email"] as String
                        val password = userDataMap["password"] as String
                        if (email == userEmail && password == userPassword) {
                            isUserFound = true
                            val id = userDataMap["id"] as String
                            _userID.value = id
                            saveUserID(id)
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

    private fun saveUserID(id: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("user_id", id).apply()
            collectiblesDenApp.setUserID(id)
        }
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
