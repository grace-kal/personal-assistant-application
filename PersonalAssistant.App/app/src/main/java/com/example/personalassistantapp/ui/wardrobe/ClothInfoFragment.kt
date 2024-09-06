package com.example.personalassistantapp.ui.wardrobe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.personalassistantapp.R
import com.example.personalassistantapp.adapters.ClothesAdapter
import com.example.personalassistantapp.databinding.FragmentClothInfoBinding
import com.example.personalassistantapp.databinding.FragmentWardrobeBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.models.Cloth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.json.JSONObject
import java.io.IOException

class ClothInfoFragment : Fragment() {
    private var _binding: FragmentClothInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var _tokenManager: TokenManager
    private val _client = OkHttpClient()
    private var clothId: Int = 0

    private lateinit var cloth: Cloth

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClothInfoBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        clothId = ClothInfoFragmentArgs.fromBundle(requireArguments()).clothId

//        binding.editBtn.setOnClickListener {
//            // Navigate to the update fragment with clothId
//            val action = ClothInfoFragmentDirections.actionClothInfoFragmentToAddClothFragment(clothId)
//            findNavController().navigate(action)
//        }

//        binding.deleteBtn.setOnClickListener {
//            // Handle delete functionality
//            deleteCloth(clothId)
//        }

        fetchCloth()

        return binding.root
    }


    private fun fetchCloth() {
        val baseUrl =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.WARDROBECONTROLLER + ApiRequestHelper.GET_CLOTH_INFO_ENDPOINT_WARDROBECONTROLLER
        val urlString = ApiRequestHelper.valuesBuilder(
            baseUrl,
            "email=${_tokenManager.getEmailFromToken()}&clothId=$clothId"
        )

        // Make API request
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(urlString).build()
                val response: Response = _client.newCall(request).execute()
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val jsonObject = JSONObject(responseData)
                    val cloth = parseJsonCloth(jsonObject)
                    withContext(Dispatchers.Main) {
                        Log.d("ClothInfoFragment", "Fragment is added: $isAdded")
                        if (isAdded) {
                            populateUI(cloth)
                        } else {
                            Log.e("ClothInfoFragment", "Fragment no longer added, cannot update UI")
                        }
                    }
                } else {
                    // Handle empty response or error
                }
            } catch (e: IOException) {
                // Handle exception
            }
        }
    }

    private fun parseJsonCloth(jsonObject: JSONObject): Cloth {
        return Cloth(
            id = jsonObject.optInt("id", 0),
            title = jsonObject.optString("title", "N/A"),
            description = jsonObject.optString("description", "N/A"),
            imageUrl = jsonObject.optString("blobUri", "N/A"),
            color = jsonObject.optString("color", "N/A"),
            season = jsonObject.optString("season", "N/A"),
            weatherKind = jsonObject.optString("weatherKind", "N/A"),
            kind = jsonObject.optString("clothKind", "N/A"),
            area = jsonObject.optString("clothArea", "N/A"),
            length = jsonObject.optString("clothLength", "N/A"),
            thickness = jsonObject.optString("clothThickness", "N/A")
        )
    }

    private fun populateUI(cloth: Cloth) {
        binding.clothTitle.text = cloth.title
        binding.clothDescription.text = cloth.description
        binding.clothColor.text = cloth.color
        binding.clothSeason.text = cloth.season
        binding.clothThickness.text = cloth.thickness
        binding.clothLenght.text = cloth.length
        binding.clothArea.text = cloth.area
        binding.clothKind.text = cloth.kind
        binding.weatherKind.text = cloth.weatherKind

        Glide.with(this)
            .load(cloth.imageUrl)
            .into(binding.clothImage)
    }

    private fun deleteCloth(id: Int) {
        // Implement the delete functionality
    }

    override fun onDestroy() {
        super.onDestroy()
        super.onDestroyView()
        Log.d("ClothInfoFragment", "onDestroyView")

    }
}