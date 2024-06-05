package com.example.personalassistantapp.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.personalassistantapp.R
import com.example.personalassistantapp.R.id
import com.example.personalassistantapp.databinding.FragmentAddEventBinding
import com.example.personalassistantapp.databinding.FragmentEventsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEventFragment : Fragment() {
    private lateinit var eventStartDateET: EditText
    private lateinit var eventEndDateET: EditText
    private lateinit var eventStartTimeET: EditText
    private lateinit var eventEndTimeET: EditText
    private var _binding: FragmentAddEventBinding? = null
    private var allUsers: List<String> = listOf("User1", "User2", "User3", "User4", "User5")
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_event, container, false)
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        eventStartDateET = binding.eventStartDateET
        eventEndDateET = binding.eventEndDateET
        eventStartTimeET = binding.eventStartTimeET
        eventEndTimeET = binding.eventEndTimeET

        // Set click listeners for date and time EditText fields
        eventStartDateET.setOnClickListener { showDatePicker(eventStartDateET) }
        eventEndDateET.setOnClickListener { showDatePicker(eventEndDateET) }
        eventStartTimeET.setOnClickListener { showTimePicker(eventStartTimeET) }
        eventEndTimeET.setOnClickListener { showTimePicker(eventEndTimeET) }


        val root: View = binding.root
        return root
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