package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistantapp.databinding.ItemMessageBinding
import com.example.personalassistantapp.models.Message

class MessageAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size
    class MessageViewHolder(val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageTextView.text = message.content
            if (message.isUser) {
                // Customize user message appearance
                binding.messageTextView.setBackgroundColor(1)
            } else {
                // Customize AI response appearance
                binding.messageTextView.setBackgroundColor(2)
            }
        }
    }
}