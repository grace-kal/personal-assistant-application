package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.ItemEventBinding
import com.example.personalassistantapp.models.Event

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