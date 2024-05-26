package com.example.personalassistantapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalassistantapp.R
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.HashHelper
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.helpers.constantValues.StaticValues
import com.example.personalassistantapp.models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class LoginActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager

    //  View items
    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager(this)
        checkIfThereIsUserWithValidToken()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //      Getting the objects
        emailET = findViewById(R.id.loginEmailEditText)
        passwordET = findViewById(R.id.loginPasswordEditText)
    }

    //    Login btn onClick
    fun onClick(view: View) {
        if (view.id == R.id.loginOkBtn) {
            if (emailET?.text.toString().isEmpty()
                || passwordET?.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this, "Please enter all fields!",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            val user = User()
            user.email = emailET?.text.toString()
            user.password = passwordET?.text.toString()

            loginUserApiRequest(user)
        }
    }

    private fun loginUserApiRequest(user: User) {
        var urlString =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.USERCONTROLLER + ApiRequestHelper.LOGIN_ENDPOINT_USERCONTROLLER

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        // Example JSON data
        val jsonBody = """
            {
              "email": "${user.email}",
              "password": "${HashHelper.hashString(user.password)}"
            }
        """.trimIndent()
    }

    fun redirectToRegister(view: View) {
        val intent =
            Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun checkIfThereIsUserWithValidToken() {
        if (tokenManager.containsToken()) {
            val token = tokenManager.getToken()

        } else {
            val intent =
                Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
