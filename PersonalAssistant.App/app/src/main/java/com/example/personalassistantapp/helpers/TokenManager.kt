package com.example.personalassistantapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.example.personalassistantapp.helpers.constantValues.StaticValues.PREFS_NAME
import com.example.personalassistantapp.helpers.constantValues.StaticValues.TOKEN_EMAIL
import com.example.personalassistantapp.helpers.constantValues.StaticValues.TOKEN_KEY
import java.util.Date

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun containsToken(): Boolean {
        return sharedPreferences.contains(TOKEN_KEY)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun getEmailFromToken(): String? {
        val token = getToken() ?: return null
        return try {
            val jwt = JWT(token)
            jwt.getClaim(TOKEN_EMAIL).asString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun removeToken() {
        with(sharedPreferences.edit()) {
            remove(TOKEN_KEY)
            apply()
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val jwt = JWT(token)
            val expiresAt = jwt.expiresAt
            val issuedAt = jwt.issuedAt

            if (expiresAt != null && issuedAt != null) {
                val currentTime = Date()

                if (expiresAt.before(currentTime)) {
                    return true
                }
                val validityDuration = expiresAt.time - issuedAt.time // Duration in milliseconds
                val validityDurationMinutes =
                    validityDuration / (1000 * 60) // Convert milliseconds to minutes

                println("Token validity duration (minutes): $validityDurationMinutes")

                val expectedDurationMinutes = 420
                if (validityDurationMinutes.toInt() == expectedDurationMinutes) {
                    return false
                } else {
                    return true
                }
            } else {
                // If there is no expiration or issued at claim, consider the token invalid or expired
                true
            }
        } catch (e: Exception) {
            // Handle the exception as per your requirements
            e.printStackTrace()
            true
        }
    }
}