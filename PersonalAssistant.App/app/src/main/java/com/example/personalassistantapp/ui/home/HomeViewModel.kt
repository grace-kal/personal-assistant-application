package com.example.personalassistantapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.models.Event
import com.example.personalassistantapp.models.Note
import com.example.personalassistantapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomeViewModel : ViewModel() {
    private val client = OkHttpClient()
    private var email: String = "";
    private var date: String = "";

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun init(email: String?) {
        this.email = email!!

        //current date to format ISO 8601
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date = dateFormat.format(calendar.time)

        fetchEvents()
        fetchTasks()
        fetchNotes()
    }

    private fun fetchEvents() {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.EVENTSCONTROLLER,
            ApiRequestHelper.GET_ALL_EVENTS_FOR_DATE_ENDPOINT_EVENTCONTROLLER
        )
        var url = ApiRequestHelper.valuesBuilder(baseUrl, "email=${email}&date=${date}")

        try {
            val request = Request.Builder()
                .url(url)
                .build()
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { jsonString ->
                    parseJsonEventsList(jsonString)
                }
            } else {
                Log.e("FetchApiData", "Error response code: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("FetchApiData", "Exception: ${e.message}")
        }
    }

    private fun parseJsonEventsList(jsonString: String) {

    }

    private fun fetchTasks() {
        // Simulated API request to fetch tasks
        _tasks.value = listOf(
            Task("11:00", "Task 1", "Description 1"),
            Task("12:00", "Task 2", "Description 2")
        )
    }

    private fun fetchNotes() {
        // Simulated API request to fetch notes
        _notes.value = listOf(
            Note("Note 1", "Content 1"),
            Note("Note 2", "Content 2")
        )
    }
}