package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personalassistantapp.databinding.ItemNewsBinding
import com.example.personalassistantapp.models.News

class NewsAdapter(private var news: List<News>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>()  {
    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsAdapter.ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val news = news[position]
        holder.binding.title.text = news.title
        holder.binding.date.text = news.description
        holder.binding.content.text = news.pubDate

        Glide.with(holder.itemView.context)
            .load(news.imageUrl)
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = news.size
    fun updateData(newData: List<News>?) {
        if (newData != null) {
            news = newData
        }
    }
}