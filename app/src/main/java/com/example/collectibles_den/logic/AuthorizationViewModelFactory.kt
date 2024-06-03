package com.example.collectibles_den.logic

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthorizationViewModelFactory(
    private val context: Context,
    private val databaseViewModel: DatabaseViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthorizationViewModel(context, databaseViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

