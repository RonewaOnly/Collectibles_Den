package com.example.collectibles_den.Logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthorizationViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult
    private val _loginState = MutableStateFlow<LoginState>(LoginState.IDLE)
    val loginState: StateFlow<LoginState> = _loginState
    private val _registerState = MutableStateFlow<LoginState>(LoginState.IDLE)
    val registerState: StateFlow<LoginState> = _registerState

    // Calling the class for the database
    private val dataClass = DatabaseViewModel()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.LOADING

            delay(2000)
            dataClass.loginValidation(email, password, object : DatabaseViewModel.LoginValidationCallback {
                override fun onUserFound() {
                    _loginState.value = LoginState.SUCCESS
                    _loginResult.value = true
                }

                override fun onUserNotFound() {
                    _loginState.value = LoginState.ERROR("User doesn't exist")
                    _loginResult.value = false
                }

                override fun onError(error: DatabaseError) {
                    _loginState.value = LoginState.ERROR("Database error: $error")
                    _loginResult.value = false
                }
            })
        }
    }

    fun registration(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerState.value = LoginState.LOADING

            delay(2000)

            if (password == confirmPassword) {
                // Save to database
                dataClass.registrationTaker(firstname, lastname, email, password)
                _registerState.value = LoginState.SUCCESS
                _registerResult.value = true
            } else {
                _registerState.value = LoginState.ERROR("Passwords do not match")
                _registerResult.value = false
            }
        }
    }
}
sealed class LoginState {
    object IDLE : LoginState()
    object LOADING : LoginState()
    object SUCCESS : LoginState()
    data class ERROR(val message: String) : LoginState()
}