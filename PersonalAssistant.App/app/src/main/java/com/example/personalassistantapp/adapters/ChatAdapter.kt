package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.ItemChatBinding
import com.example.personalassistantapp.models.Chat

class ChatAdapter(private var chats: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>()  {
    class ViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        val event = chats[position]
        holder.binding.titleTextView.text = event.title
    }

    override fun getItemCount(): Int = chats.size
    fun updateData(newData: List<Chat>?) {
        if (newData != null) {
            chats = newData
        }
    }
}