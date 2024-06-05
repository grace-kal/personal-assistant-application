package com.example.personalassistantapp.ui.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventsViewModel : ViewModel() {
    private val client = OkHttpClient()
    private var email: String = ""
    private var date: String = ""
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events
    fun init(email: String?, selectedDate: String) {
        this.email = email!!
        date = selectedDate
        fetchEvents()
    }

    private fun fetchEvents() {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.EVENTSCONTROLLER,
            ApiRequestHelper.GET_ALL_EVENTS_FOR_DATE_ENDPOINT_EVENTCONTROLLER
        )
        var url = ApiRequestHelper.valuesBuilder(baseUrl, "email=${email}&date=${date}")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseData) // Import JSONArray
                    if (jsonArray.length() > 0) {
                        val eventsList = parseJsonEventsList(jsonArray) // Adjust parsing function
                        withContext(Dispatchers.Main) {
                            _events.value = eventsList
                        }
                    }
                } else {
                    Log.e("FetchApiData", "Error response code: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    private fun parseJsonEventsList(jsonObj: JSONArray): List<Event> {
        val events = mutableListOf<Event>()
        for (i in 0 until jsonObj.length()) {
            val eventObj = jsonObj.getJSONObject(i)
            val startDate = eventObj.getString("startDate")
            val startTime = eventObj.getString("startTime")
            val endDate = eventObj.getString("endDate")
            val endTime = eventObj.getString("endTime")
            val title = eventObj.getString("title")
            val description = eventObj.getString("description")
            events.add(Event(null, title, description,null,startDate,endDate,startTime,endTime))
        }
        return events
    }
}