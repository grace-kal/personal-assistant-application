package com.example.personalassistantapp.ui.events

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalassistantapp.LoginActivity
import com.example.personalassistantapp.databinding.FragmentAddEventBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.HashHelper
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.helpers.constantValues.StaticValues
import com.example.personalassistantapp.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEventFragment : Fragment() {
    private val client = OkHttpClient()
    private lateinit var _tokenManager: TokenManager

    private lateinit var searchUserBar: AutoCompleteTextView
    private lateinit var selectedUsersList: ListView
    private lateinit var selectedUsersAdapter: ArrayAdapter<String>
    private var allUsers: MutableList<String> =
        mutableListOf("test@gmail.com", "myuser@gmail.com", "testtest@gmail.com", "tester@abv.bg")

    private lateinit var eventStartDateET: EditText
    private lateinit var eventEndDateET: EditText
    private lateinit var eventStartTimeET: EditText
    private lateinit var eventEndTimeET: EditText

    private var eventTitle: EditText? = null
    private var eventDescription: EditText? = null

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        eventTitle = binding.eventTitleET
        eventDescription = binding.eventDescriptionET

        // PICKERS Date and time
        setPickersForDateAndTime()

        // INVITE USERS
        inviteUsersSearchBarAndList()

        //ADD EVENT
        binding.createBtn.setOnClickListener() {
            if (eventTitle?.text.toString().isEmpty()
                || eventDescription?.text.toString().isEmpty()
            ) {
//                Toast.makeText(
//                    this, "Please enter all fields and make sure both passwords match",
//                    Toast.LENGTH_LONG
//                ).show()
            } else {
                if (_tokenManager.getEmailFromToken()?.isNotEmpty() == true) {
                    val event = Event(
                        null,
                        eventTitle!!.text.toString(),
                        eventDescription!!.text.toString(),
                        null,
                        eventStartDateET.text.toString(),
                        eventEndDateET.text.toString(),
                        eventStartTimeET.text.toString(),
                        eventEndTimeET.text.toString()
                    )
                    addEvent(event)
                }
            }
        }
        val root: View = binding.root
        return root
    }

    private fun addEvent(event: Event) {
        var baseUrl =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.EVENTSCONTROLLER + ApiRequestHelper.CREATE_EVENT_ENDPOINT_EVENTCONTROLLER
        val urlString = ApiRequestHelper.valuesBuilder(baseUrl,"email=${_tokenManager.getEmailFromToken()}")

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        // Example JSON data
        val jsonBody = """
            {
              "title": "${event.title}",
              "description": "${event.description}",
              "startDate": "${event.startDate}",
              "endDate": "${event.endDate}",
              "startTime": "${event.startTime}",
              "endTime": "${event.endTime}"
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
                withContext(Dispatchers.Main) {
                    findNavController().navigate(com.example.personalassistantapp.R.id.action_addEventsFragment_to_eventsFragment)
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    private fun inviteUsersSearchBarAndList() {
        //        loadUsersToInvite()
        searchUserBar = binding.searchUserBar
        selectedUsersList = binding.selectedUsersList

        selectedUsersAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1)
        selectedUsersList.adapter = selectedUsersAdapter

        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, allUsers)
        searchUserBar.setAdapter(adapter)

        searchUserBar.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = adapter.getItem(position).toString()
            addSelectedUser(selectedUser)
            searchUserBar.setText("")
        }
    }

    private fun setPickersForDateAndTime() {
        eventStartDateET = binding.eventStartDateET
        eventEndDateET = binding.eventEndDateET
        eventStartTimeET = binding.eventStartTimeET
        eventEndTimeET = binding.eventEndTimeET

        eventStartDateET.setOnClickListener { showDatePicker(eventStartDateET) }
        eventEndDateET.setOnClickListener { showDatePicker(eventEndDateET) }
        eventStartTimeET.setOnClickListener { showTimePicker(eventStartTimeET) }
        eventEndTimeET.setOnClickListener { showTimePicker(eventEndTimeET) }
    }

    private fun loadUsersToInvite() {
        val url = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.USERCONTROLLER,
            ApiRequestHelper.ALLUSEREMAILS_ENDPOINT_USERCONTROLLER
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { jsonString ->
                        withContext(Dispatchers.Main) {
                            parseJsonToList(jsonString)
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

    private fun parseJsonToList(jsonString: String) {
        val jsonArray = JSONArray(jsonString)
        allUsers.clear()
        for (i in 0 until jsonArray.length()) {
            val user = jsonArray.getString(i)
            allUsers.add(user)
        }
    }

    private fun addSelectedUser(user: String) {
        selectedUsersAdapter.add(user)
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(selectedYear - 1900, selectedMonth, selectedDay))
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

}