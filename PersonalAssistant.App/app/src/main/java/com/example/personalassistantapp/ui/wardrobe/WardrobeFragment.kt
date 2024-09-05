package com.example.personalassistantapp.ui.wardrobe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalassistantapp.R
import com.example.personalassistantapp.adapters.ClothesAdapter
import com.example.personalassistantapp.databinding.FragmentChatsBinding
import com.example.personalassistantapp.databinding.FragmentHomeBinding
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
import org.json.JSONArray
import java.io.IOException

class WardrobeFragment : Fragment() {
    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!
    private lateinit var _tokenManager: TokenManager
    private val _client = OkHttpClient()

    private lateinit var adapter: ClothesAdapter
    private lateinit var clothesList: MutableList<Cloth>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        clothesList = mutableListOf()

        adapter = ClothesAdapter(clothesList) { clothId ->
            val action = WardrobeFragmentDirections.actionWardrobeFragmentToClothInfoFragment(clothId)
            findNavController().navigate(action)
        }

        binding.clothesList.layoutManager = LinearLayoutManager(context)
        binding.clothesList.adapter = adapter

        binding.createBtn .setOnClickListener {
            findNavController().navigate(R.id.action_wardrobeFragment_to_addClothFragment)
        }

        //update the data set

        fetchClothes()

        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_wardrobeFragment_to_addClothFragment)
        }

        val root: View = binding.root
        return root
    }

    private fun fetchClothes() {

        var baseUrl =
            ApiRequestHelper.HOSTADDRESS + ApiRequestHelper.WARDROBECONTROLLER + ApiRequestHelper.GET_CLOTHES_ENDPOINT_WARDROBECONTROLLER
        val urlString = ApiRequestHelper.valuesBuilder(baseUrl,"email=${_tokenManager.getEmailFromToken()}")

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request
                    .Builder()
                    .url(urlString)
                    .build()

                val response: Response = _client.newCall(request).execute()
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseData)
                    if (jsonArray.length() > 0) {
                        val list = parseJsonClothesList(jsonArray)
                        withContext(Dispatchers.Main) {
                            clothesList.clear()
                            clothesList.addAll(list)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.e("FetchApiData", "Error response code: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    private fun parseJsonClothesList(jsonArray: JSONArray): Collection<Cloth> {
        val clothesList = mutableListOf<Cloth>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val cloth = Cloth(
                id = jsonObject.getInt("id"),
                title = jsonObject.getString("title"),
                description = jsonObject.optString("description"),
                imageUrl = jsonObject.optString("blobUri"),
                color = jsonObject.optString("color"),
                season = jsonObject.getString("season"),
                weatherKind = jsonObject.getString("weatherKind"),
                kind =jsonObject.getString("clothKind"),
                area = jsonObject.getString("clothArea"),
                length = jsonObject.getString("clothLenght"),
                thickness = jsonObject.getString("clothThickness"),
            )
            clothesList.add(cloth)
        }
        return clothesList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}