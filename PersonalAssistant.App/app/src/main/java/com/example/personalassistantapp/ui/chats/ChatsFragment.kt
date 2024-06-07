package com.example.personalassistantapp.ui.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.personalassistantapp.R
import com.example.personalassistantapp.adapters.ChatAdapter
import com.example.personalassistantapp.adapters.EventsAdapter
import com.example.personalassistantapp.databinding.FragmentChatsBinding
import com.example.personalassistantapp.databinding.FragmentEventsBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.models.Chat
import com.example.personalassistantapp.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class ChatsFragment : Fragment() {
    private val client = OkHttpClient()
    private lateinit var _tokenManager: TokenManager
    private lateinit var _chatsAdapter: ChatAdapter
    private var _binding: FragmentChatsBinding? = null

    private val _chats = MutableLiveData<List<Chat>>()
    private val binding get() = _binding!!
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        if (_tokenManager.getEmailFromToken()?.isNotEmpty() == true) {

            val userEmail = _tokenManager.getEmailFromToken()
            fetchChats(userEmail!!)

            _chatsAdapter = ChatAdapter(emptyList())
            binding.chatsList.adapter = _chatsAdapter

            _chats.observe(viewLifecycleOwner) { chats ->
                _chatsAdapter.updateData(chats)
            }
        }

        binding.createBtn.setOnClickListener {
            // Navigate to AddEventFragment
            findNavController().navigate(R.id.action_chatsFragment_to_oneChatFragment)
        }

        val root: View = binding.root
        return root
    }

    private fun fetchChats(email: String) {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.CHATSCONTROLLER,
            ApiRequestHelper.GET_USER_CHATS_ENDPOINT_CHATCONTROLLER
        )
        var url = ApiRequestHelper.valuesBuilder(baseUrl, "email=${email}")

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (!responseData.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseData) // Import JSONArray
                    if (jsonArray.length() > 0) {
                        val chatsList = parseJsonChatsList(jsonArray) // Adjust parsing function
                        withContext(Dispatchers.Main) {
                            _chats.value = chatsList
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

    private fun parseJsonChatsList(jsonObj: JSONArray): List<Chat> {
        val chats = mutableListOf<Chat>()
        for (i in 0 until jsonObj.length()) {
            val eventObj = jsonObj.getJSONObject(i)
            val time = eventObj.getString("createdAt")
            val title = eventObj.getString("title")
            val id = eventObj.getString("id")
            chats.add(Chat(title, id, time))
        }
        return chats
    }
}