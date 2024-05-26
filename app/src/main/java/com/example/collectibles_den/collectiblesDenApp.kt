package com.example.collectibles_den

import android.app.Application
import android.content.Context

class collectiblesDenApp: Application() {
    companion object {
        private lateinit var instance: collectiblesDenApp
        private const val PREF_NAME = "user_prefs"
        private const val USER_ID_KEY = "user_id"

        fun getInstance(): collectiblesDenApp {
            return instance
        }

        fun setUserID(userID: String?) {
            val sharedPreferences = instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(USER_ID_KEY, userID).apply()
        }

        fun getUserID(): String? {
            val sharedPreferences = instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_ID_KEY, null)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}