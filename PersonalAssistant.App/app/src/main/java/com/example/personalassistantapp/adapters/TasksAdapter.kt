package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.ItemTaskBinding
import com.example.personalassistantapp.models.Task

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
