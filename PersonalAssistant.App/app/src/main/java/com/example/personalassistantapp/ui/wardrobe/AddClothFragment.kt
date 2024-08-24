package com.example.personalassistantapp.ui.wardrobe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalassistantapp.R
import com.example.personalassistantapp.databinding.FragmentAddClothBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddClothFragment : Fragment() {
    private var _binding: FragmentAddClothBinding? = null
    private val _client = OkHttpClient()
    private lateinit var _tokenManager: TokenManager
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClothBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

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

        //!!!!!!!!!! if no image handle
        val drawable = binding.clothImageView.drawable ?: return

        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        //api request
        var baseUrl =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.WARDROBECONTROLLER + ApiRequestHelper.ADD_NEW_CLOTH_ENDPOINT_WARDROBECONTROLLER
        val urlString = ApiRequestHelper.valuesBuilder(baseUrl,"email=${_tokenManager.getEmailFromToken()}")

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("title", title)
            .addFormDataPart("description", description)
            .addFormDataPart("season", season)
            .addFormDataPart("image", "cloth.jpg",
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray))
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request
                    .Builder()
                    .url(urlString)
                    .post(requestBody)
                    .build()
                _client.newCall(request).execute()
                withContext(Dispatchers.Main) {
//                    findNavController().navigate(R.id.action_addEventsFragment_to_eventsFragment)
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }

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
