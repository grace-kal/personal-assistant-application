package com.example.personalassistantapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalassistantapp.models.Event
import com.example.personalassistantapp.models.Note
import com.example.personalassistantapp.models.Task

class HomeViewModel : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun init() {
        fetchEvents()
        fetchTasks()
        fetchNotes()
    }

    private fun fetchEvents() {
        // Simulated API request to fetch events
        _events.value = listOf(
            Event("09:00", "Event 1", "Description 1"),
            Event("10:00", "Event 2", "Description 2")
        )
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
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
}