package com.example.personalassistantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personalassistantapp.databinding.ItemClothBinding
import com.example.personalassistantapp.models.Cloth

class ClothesAdapter(private var clothes: List<Cloth>) :
    RecyclerView.Adapter<ClothesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemClothBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClothBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cloth = clothes[position]
        holder.binding.clothTitle.text = cloth.title
        holder.binding.clothDescription.text = cloth.description
        holder.binding.clothSeason.text = cloth.season

        // Load image
        Glide.with(holder.itemView.context)
            .load(cloth.imageUrl)
            .into(holder.binding.clothImageView)
    }

    override fun getItemCount(): Int = clothes.size

    fun updateData(newData: List<Cloth>) {
        clothes = newData
        notifyDataSetChanged()
    }
}
