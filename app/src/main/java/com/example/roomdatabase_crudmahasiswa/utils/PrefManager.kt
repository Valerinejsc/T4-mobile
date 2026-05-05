package com.example.roomdatabase_crudmahasiswa.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USERNAME = "username"
        private const val REMEMBER_ME = "rememberMe"
    }

    fun setLogin(isLoggedIn: Boolean, user: String, rememberMe: Boolean) {
        prefs.edit().apply {
            putBoolean(IS_LOGGED_IN, isLoggedIn)
            putString(USERNAME, user)
            putBoolean(REMEMBER_ME, rememberMe)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(IS_LOGGED_IN, false)
    fun getUsername(): String? = prefs.getString(USERNAME, "")
    fun isRememberMe(): Boolean = prefs.getBoolean(REMEMBER_ME, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}