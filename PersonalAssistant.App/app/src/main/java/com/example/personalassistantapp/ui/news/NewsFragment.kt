package com.example.personalassistantapp.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.personalassistantapp.adapters.ClothesAdapter
import com.example.personalassistantapp.adapters.NewsAdapter
import com.example.personalassistantapp.databinding.FragmentNewsBinding
import com.example.personalassistantapp.databinding.FragmentWardrobeBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.models.Chat
import com.example.personalassistantapp.models.Cloth
import com.example.personalassistantapp.models.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val _client = OkHttpClient()

    private lateinit var adapter: NewsAdapter
    private val news = MutableLiveData<List<News>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        adapter = NewsAdapter(emptyList())
        binding.newsList.adapter = adapter

        news.observe(viewLifecycleOwner) { newss ->
            adapter.updateData(newss)
        }
        fetchNews()

        val root: View = binding.root
        return root
    }

    private fun fetchNews() {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.NEWSCONTROLLER,
            ApiRequestHelper.LATEST_NEWS_NEWSCONTROLLER
        )
//        var url = ApiRequestHelper.valuesBuilder(baseUrl, "email=${email}")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(baseUrl)
                    .build()
                val response: Response = _client.newCall(request).execute()
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseData) // Import JSONArray
                    if (jsonArray.length() > 0) {
                        val newsList = parseJsonNewsList(jsonArray) // Adjust parsing function
                        withContext(Dispatchers.Main) {
                            news.value = newsList
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

    private fun parseJsonNewsList(jsonArray: JSONArray):List<News> {
        var list = mutableListOf<News>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val new = News(
                title = jsonObject.optString("title"),
                description = jsonObject.optString("description"),
                pubDate = jsonObject.optString("pubDate"),
                imageUrl = jsonObject.optString("image_url")
            )
            list.add(new)
        }
        return list
    }
}