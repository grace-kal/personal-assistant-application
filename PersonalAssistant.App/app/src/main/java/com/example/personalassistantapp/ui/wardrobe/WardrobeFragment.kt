package com.example.personalassistantapp.ui.wardrobe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalassistantapp.R
import com.example.personalassistantapp.adapters.ClothesAdapter
import com.example.personalassistantapp.databinding.FragmentChatsBinding
import com.example.personalassistantapp.databinding.FragmentHomeBinding
import com.example.personalassistantapp.databinding.FragmentWardrobeBinding
import com.example.personalassistantapp.models.Cloth

class WardrobeFragment : Fragment() {
    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ClothesAdapter
    private lateinit var clothesList: MutableList<Cloth>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)

        clothesList = mutableListOf()
        adapter = ClothesAdapter(clothesList)

        binding.clothesList.layoutManager = LinearLayoutManager(context)
        binding.clothesList.adapter = adapter

        binding.createBtn .setOnClickListener {
            findNavController().navigate(R.id.action_wardrobeFragment_to_addClothFragment)
        }

        //update teh data set

        fetchClothes()

        binding.createBtn.setOnClickListener {
            // Navigate to AddEventFragment
            findNavController().navigate(R.id.action_wardrobeFragment_to_addClothFragment)
        }

        val root: View = binding.root
        return root
    }

    private fun fetchClothes() {
        // API call
//
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}