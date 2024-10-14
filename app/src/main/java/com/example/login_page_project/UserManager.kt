package com.example.login_page_project

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREFS_NAME = "user_session"
    private const val KEY_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ID = "user_id"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean, userId: String? = null) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putBoolean(KEY_LOGGED_IN, isLoggedIn)
            userId?.let { putString(KEY_USER_ID, it) }
            apply()
        }
    }

    fun getUserId(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_USER_ID, null)
    }

    fun logout(context: Context) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}
