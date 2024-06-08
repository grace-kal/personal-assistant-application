package com.example.personalassistantapp.ui.wardrobe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.personalassistantapp.R
import com.example.personalassistantapp.databinding.FragmentAddClothBinding
import java.io.ByteArrayOutputStream

class AddClothFragment : Fragment() {
    private var _binding: FragmentAddClothBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClothBinding.inflate(inflater, container, false)

        //seasons dropdown
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.seasons_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.seasonSpinner.adapter = adapter
        }

        binding.uploadImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.addClothButton.setOnClickListener {
            addClothApiRequest()
        }

        return binding.root
    }

    private fun addClothApiRequest() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val season = binding.seasonSpinner.selectedItem.toString()

        val drawable = binding.clothImageView.drawable ?: return

        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        //api request
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // activity result launcher
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.clothImageView.setImageURI(it)
        }
    }
}
