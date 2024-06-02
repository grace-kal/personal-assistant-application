package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.ItemNoteBinding
import com.example.personalassistantapp.models.Note

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