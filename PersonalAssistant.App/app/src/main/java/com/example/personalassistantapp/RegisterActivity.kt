package com.example.personalassistantapp

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.models.User
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale


class RegisterActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener {

    //  View items
    private var usernameET: EditText? = null
    private var firstNameET: EditText? = null
    private var lastNameET: EditText? = null
    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var repeatPasswordET: EditText? = null
    private lateinit var countriesSpinner: Spinner
    private lateinit var citiesSpinner: Spinner

    //    Class variables
    var countriesList: MutableList<String> = mutableListOf()
    var citiesList: MutableList<String> = mutableListOf()
    var selectedCountry: String? = null
    var selectedCity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//      Getting the objects
        usernameET = findViewById(R.id.registerUsernameEditText)
        firstNameET = findViewById(R.id.registerFirstNameEditText)
        lastNameET = findViewById(R.id.registerLastNameEditText)
        emailET = findViewById(R.id.registerEmailEditText)
        passwordET = findViewById(R.id.registerPasswordEditText)
        repeatPasswordET = findViewById(R.id.registerRepeatPasswordEditText)
        countriesSpinner = findViewById(R.id.country_spinner)
        citiesSpinner = findViewById(R.id.city_spinner)

//      Settings the countries for the spinner
        val countryCodes = Locale.getISOCountries()

        for (countryCode: String? in countryCodes) {
            val obj = Locale("", countryCode)
            countriesList.add(obj.displayCountry)
            println(
                "Country Code = " + obj.country
                        + ", Country Name = " + obj.displayCountry
            )
        }

        countriesSpinner.onItemSelectedListener = this
        //Creating the ArrayAdapter instance
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                countriesList as List<Any?>
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Setting the ArrayAdapter data on the Spinner
        countriesSpinner.setAdapter(aa)
    }

    fun onClick(view: View) {
        if (view.getId() == R.id.registerOkBtn) {
            if (usernameET?.text.toString().isEmpty()
                || emailET?.text.toString().isEmpty()
                || passwordET?.text.toString().isEmpty()
                || passwordET?.text.toString() != repeatPasswordET?.text.toString()
            ) {

                Toast.makeText(
                    this, "Please enter all fields and make sure both passwords match",
                    Toast.LENGTH_SHORT
                ).show();
                return;
            }

            val user = User()
            user.username = usernameET?.text.toString()
            user.email = emailET?.text.toString()
            user.firstName=firstNameET?.text.toString()
            user.lastName=lastNameET?.text.toString()
            user.country=selectedCountry
            user.city = "implement"
            user.password = passwordET?.text.toString()

            val registerAsyncTask = RegisterAsyncTask(user)
            registerAsyncTask.execute()
        }
    }

    //    Spinner methods
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(applicationContext, countriesList[position], Toast.LENGTH_LONG).show();
        selectedCountry = countriesList[position]

////        Cities to display
//        citiesList.add("Implement")
//        citiesSpinner.onItemSelectedListener = this
//        val aaa: ArrayAdapter<*> =
//            ArrayAdapter<Any?>(
//                this, android.R.layout.simple_spinner_item,
//                citiesList as List<Any?>
//            )
//        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private inner class RegisterAsyncTask(private val user: User) : AsyncTask<Void, Void, Void>() {
        private var isSuccessful: Boolean = false
        private val dialog = ProgressDialog(this@RegisterActivity)
        private var error: String? = null

        override fun onPreExecute() {
            dialog.setTitle("Registering process is going...")
            dialog.show()
        }

        override fun doInBackground(vararg voids: Void?): Void? {
            val urlString = String.format(
                "%s/User/GetRegister?username=%s",
                ApiRequestHelper.ADDRESS, user.username
            )

            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urlString)
                urlConnection = url.openConnection() as HttpURLConnection

                val stream = BufferedInputStream(urlConnection.inputStream)
                val reader = BufferedReader(InputStreamReader(stream))

                val result = reader.readLine()

//                if (result != null) {
//                    val jsonOb = JSONObject(result)
//
//                    if (jsonOb.getBoolean("Success")) {
//                        isSuccessful = true
//                    } else {
//                        error = jsonOb.getString("Message")
//                    }
//                } else {
//                    error = "There was no response!"
//                }
            } catch (e: IOException) {
                error = e.message
                throw RuntimeException(e)
            } catch (e: JSONException) {
                error = e.message
                throw RuntimeException(e)
            } finally {
                urlConnection?.disconnect()
            }

            return null
        }

        override fun onPostExecute(unused: Void?) {
           dialog.hide()
//
//            if (isSuccessful) {
//                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                intent.putExtra("newUser", true)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            } else {
//                Toast.makeText(
//                    this@RegisterActivity,
//                    "Register was not successful $error", Toast.LENGTH_SHORT
//                ).show()
//            }
        }
    }

}