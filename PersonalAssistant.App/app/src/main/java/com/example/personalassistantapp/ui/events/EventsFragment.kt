package com.example.personalassistantapp.ui.events

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.personalassistantapp.adapters.EventsAdapter
import com.example.personalassistantapp.databinding.FragmentEventsBinding
import com.example.personalassistantapp.helpers.TokenManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EventsFragment : Fragment() {
    private lateinit var _tokenManager: TokenManager
    private lateinit var _eventsAdapter: EventsAdapter
    private var _binding: FragmentEventsBinding? = null
    private var _selectedDate: String = ""
    private var _userEmail: String? = ""
    private val binding get() = _binding!!
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        val viewModel =
            ViewModelProvider(this)[EventsViewModel::class.java]

        if (_tokenManager.getEmailFromToken()?.isNotEmpty() == true) {

            //current date to format ISO 8601 on initial load
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            _selectedDate = dateFormat.format(calendar.time)
            _userEmail = _tokenManager.getEmailFromToken()
            viewModel.init(_userEmail, _selectedDate)

//            if(homeViewModel.events.value? !=null) do this check if i want text when no items
            _eventsAdapter = EventsAdapter(viewModel.events.value ?: emptyList())

            viewModel.events.observe(viewLifecycleOwner) { events ->
                _eventsAdapter.updateData(events)
            }

            binding.eventsList.adapter = _eventsAdapter
        }


        // Set date change listener
        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = dateFormat.format(calendar.time)

            // Fetch events for the selected date
            viewModel.init(_userEmail, selectedDate)

        })

        binding.createBtn.setOnClickListener {
            // Navigate to AddEventFragment
            findNavController().navigate(com.example.personalassistantapp.R.id.action_eventsFragment_to_addEventsFragment)
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}