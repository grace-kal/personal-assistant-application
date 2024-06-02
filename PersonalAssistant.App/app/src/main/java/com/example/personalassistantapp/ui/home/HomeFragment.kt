package com.example.personalassistantapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.FragmentHomeBinding
import com.example.personalassistantapp.databinding.ItemEventBinding
import com.example.personalassistantapp.databinding.ItemNoteBinding
import com.example.personalassistantapp.databinding.ItemTaskBinding
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.models.Event
import com.example.personalassistantapp.models.Note
import com.example.personalassistantapp.models.Task

class HomeFragment : Fragment() {
    private lateinit var tokenManager: TokenManager
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
        tokenManager = TokenManager(requireContext())

        if (tokenManager.getEmailFromToken()?.isNotEmpty() == true) {
            val homeViewModel =
                ViewModelProvider(this)[HomeViewModel::class.java]

            homeViewModel.init(tokenManager.getEmailFromToken())

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

//    Inflating and setting the recycle views items
class EventsAdapter(private var events: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.binding.timeTextView.text = event.time
        holder.binding.titleTextView.text = event.title
        holder.binding.descriptionTextView.text = event.description
    }

    override fun getItemCount(): Int = events.size
    fun updateData(newData: List<Event>?) {
        if (newData != null) {
            events = newData
        }
    }
}

class TasksAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.timeTextView.text = task.time
        holder.binding.titleTextView.text = task.title
        holder.binding.descriptionTextView.text = task.description
    }

    override fun getItemCount(): Int = tasks.size
    fun updateData(newData: List<Task>?) {
        if (newData != null) {
            tasks = newData
        }
    }
}

class NotesAdapter(private var notes: List<Note>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.titleTextView.text = note.title
        holder.binding.descriptionTextView.text = note.content
    }

    override fun getItemCount(): Int = notes.size
    fun updateData(newData: List<Note>?) {
        if (newData != null) {
            notes = newData
        }
    }
}
