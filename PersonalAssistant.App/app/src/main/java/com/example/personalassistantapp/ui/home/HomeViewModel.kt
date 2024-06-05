package com.example.personalassistantapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.models.Event
import com.example.personalassistantapp.models.Note
import com.example.personalassistantapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
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

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

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
        Log.w("Events response:", "jj")
        return listOf(
            Event("00:00", "IMPLEMENT", "Description 1",null, null, null, null, null),
            Event("00:00", "Task 2", "Description 2",null, null, null, null, null)
        )

//        val events = mutableListOf<Event>()
//        for (i in 0 until jsonArray.length()) {
//            val eventObj = jsonArray.getJSONObject(i)
//            val time = eventObj.getString("time")
//            val title = eventObj.getString("title")
//            val description = eventObj.getString("description")
//            events.add(Event(time, title, description))
//        }
//        return events
    }

    private fun fetchTasks() {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.TASKSCONTROLLER,
            ApiRequestHelper.GET_ALL_TASKS_FOR_DATE_ENDPOINT_TASKCONTROLLER
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
                        val tasksList = parseJsonTasksList(jsonArray) // Adjust parsing function
                        withContext(Dispatchers.Main) {
                            _tasks.value = tasksList
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

    private fun parseJsonTasksList(jsonArray: JSONArray): List<Task> {
        return listOf(
            Task("11:00", "Task 1", "Description 1"),
            Task("12:00", "Task 2", "Description 2")
        )
    }

    private fun fetchNotes() {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.NOTESCONTROLLER,
            ApiRequestHelper.GET_ALL_NOTES_FOR_DATE_ENDPOINT_NOTECONTROLLER
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
                        val notesList = parseJsonNotesList(jsonArray) // Adjust parsing function
                        withContext(Dispatchers.Main) {
                            _notes.value = notesList
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

    private fun parseJsonNotesList(jsonArray: JSONArray): List<Note> {
        return listOf(
            Note("Note 1", "Content 1"),
            Note("Note 2", "Content 2")
        )
    }
}