package com.example.personalassistantapp.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.lifecycle.lifecycleScope
import com.example.personalassistantapp.databinding.FragmentAddEventBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
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
import java.util.Date
import java.util.Locale

class AddEventFragment : Fragment() {
    private val client = OkHttpClient()

    private lateinit var searchUserBar: AutoCompleteTextView
    private lateinit var selectedUsersList: ListView
    private lateinit var selectedUsersAdapter: ArrayAdapter<String>
    private var allUsers: MutableList<String> = mutableListOf("test@gmail.com","myuser@gmail.com","testtest@gmail.com", "tester@abv.bg")


    private lateinit var eventStartDateET: EditText
    private lateinit var eventEndDateET: EditText
    private lateinit var eventStartTimeET: EditText
    private lateinit var eventEndTimeET: EditText
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

        // Date and time PICKERS
        eventStartDateET = binding.eventStartDateET
        eventEndDateET = binding.eventEndDateET
        eventStartTimeET = binding.eventStartTimeET
        eventEndTimeET = binding.eventEndTimeET

        eventStartDateET.setOnClickListener { showDatePicker(eventStartDateET) }
        eventEndDateET.setOnClickListener { showDatePicker(eventEndDateET) }
        eventStartTimeET.setOnClickListener { showTimePicker(eventStartTimeET) }
        eventEndTimeET.setOnClickListener { showTimePicker(eventEndTimeET) }


        // INVITE USERS
//        loadUsersToInvite()
        searchUserBar = binding.searchUserBar
        selectedUsersList = binding.selectedUsersList

        selectedUsersAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        selectedUsersList.adapter = selectedUsersAdapter

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allUsers)
        searchUserBar.setAdapter(adapter)

        searchUserBar.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = adapter.getItem(position).toString()
            addSelectedUser(selectedUser)
            searchUserBar.setText("")
        }

        val root: View = binding.root
        return root
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
                        withContext(Dispatchers.Main){
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