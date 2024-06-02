package com.example.personalassistantapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.adapters.EventsAdapter
import com.example.personalassistantapp.adapters.NotesAdapter
import com.example.personalassistantapp.adapters.TasksAdapter
import com.example.personalassistantapp.databinding.FragmentHomeBinding
import com.example.personalassistantapp.databinding.ItemEventBinding
import com.example.personalassistantapp.databinding.ItemNoteBinding
import com.example.personalassistantapp.databinding.ItemTaskBinding
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.models.Event
import com.example.personalassistantapp.models.Note
import com.example.personalassistantapp.models.Task

class HomeFragment : Fragment() {
    private lateinit var _tokenManager: TokenManager
    private lateinit var _eventsAdapter: EventsAdapter
    private lateinit var _tasksAdapter: TasksAdapter
    private lateinit var _notesAdapter: NotesAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        if (_tokenManager.getEmailFromToken()?.isNotEmpty() == true) {
            val homeViewModel =
                ViewModelProvider(this)[HomeViewModel::class.java]

            homeViewModel.init(_tokenManager.getEmailFromToken())

//            if(homeViewModel.events.value? !=null) do this check if i want text when no items
            _eventsAdapter = EventsAdapter(homeViewModel.events.value ?: emptyList())
            _tasksAdapter = TasksAdapter(homeViewModel.tasks.value ?: emptyList())
            _notesAdapter = NotesAdapter(homeViewModel.notes.value ?: emptyList())

            homeViewModel.events.observe(viewLifecycleOwner) { events ->
                _eventsAdapter.updateData(events)
            }

            homeViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
                _tasksAdapter.updateData(tasks)
            }

            homeViewModel.notes.observe(viewLifecycleOwner) { notes ->
                _notesAdapter.updateData(notes)
            }


            binding.eventsList.adapter = _eventsAdapter
            binding.tasksList.adapter = _tasksAdapter
            binding.notesList.adapter = _notesAdapter
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}