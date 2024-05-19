package com.example.personalassistantapp

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.HashHelper
import com.example.personalassistantapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.util.Locale


class RegisterActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener {

    private val client = OkHttpClient()

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
    var countriesWithCodes: MutableMap<String, String> = mutableMapOf()
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

        loadCountries();
    }

    fun onClick(view: View) {
        if (view.id == R.id.registerOkBtn) {
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
            user.firstName = firstNameET?.text.toString()
            user.lastName = lastNameET?.text.toString()
            user.country = selectedCountry
            user.city = selectedCity
            user.password = passwordET?.text.toString()

            registerUserApiRequest(user)
        }
    }

    private fun registerUserApiRequest(user: User) {
        var urlString =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.USERCONTROLLER + ApiRequestHelper.REGISTER_ENDPOINT_USERCONTROLLER
//        val urlString = ApiRequestHelper.urlBuilder(
//            ApiRequestHelper.USERCONTROLLER,
//            ApiRequestHelper.REGISTER_ENDPOINT_USERCONTROLLER
//        )

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        // Example JSON data
        val jsonBody = """
            {
              "username": "${user.username}",
              "email": "${user.email}",
              "password": "${HashHelper.hashString(user.password)}",
              "firstName": "${user.firstName}",
              "lastName": "${user.lastName}",
              "country": "${user.country}",
              "city": "${user.city}"
            }
        """.trimIndent()

        val requestBody: RequestBody = jsonBody.toRequestBody(jsonMediaType)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request
                    .Builder()
                    .url(urlString)
                    .post(requestBody)
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                // Switch to Main dispatcher to update UI
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseData != null) {
                        // Handle the API response here (e.g., update UI)
                        Log.d("FetchApiData", "Response from regsiter: $responseData")
                    } else {
                        Log.e("FetchApiData", "Error response code from regidter: ${response.code}")
                    }
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    //    Spinner methods
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(applicationContext, countriesList[position], Toast.LENGTH_LONG).show();
        if (parent?.id == R.id.country_spinner) {
            selectedCountry = countriesList[position]
            loadCitiesForCountry(countriesList[position])
        } else if (parent?.id == R.id.city_spinner) {
            selectedCity = citiesList[position]
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun loadCountries() {
        //      Settings the countries for the spinner
        val countryCodes = Locale.getISOCountries()

        for (countryCode: String? in countryCodes) {
            val obj = Locale("", countryCode)
            countriesList.add(obj.displayCountry)
            countriesWithCodes[obj.displayCountry] = obj.country
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

    private fun loadCitiesForCountry(country: String) {
        val countryCode = countriesWithCodes[country]
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.WEATHERCONTROLLER,
            ApiRequestHelper.CITIES_FOR_COUNTRY_ENDPOINT_WEATHERCONTROLLER
        )
        val url = ApiRequestHelper.valuesBuilder(baseUrl, "countryCode=${countryCode}")

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { jsonString ->
                        parseJsonToMutableList(jsonString)
                    }
                    withContext(Dispatchers.Main) {
                        setAdapterForCitiesSpinner()
                    }
                } else {
                    Log.e("FetchApiData", "Error response code: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    private fun setAdapterForCitiesSpinner() {
        citiesSpinner.onItemSelectedListener = this
        //Creating the ArrayAdapter instance
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_item,
                citiesList as List<Any?>
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Setting the ArrayAdapter data on the Spinner
        citiesSpinner.setAdapter(aa)
    }

    private fun parseJsonToMutableList(jsonString: String) {
        val jsonArray = JSONArray(jsonString)
        citiesList.clear()
        for (i in 0 until jsonArray.length()) {
            val city = jsonArray.getString(i)
            citiesList.add(city)
        }
    }
}