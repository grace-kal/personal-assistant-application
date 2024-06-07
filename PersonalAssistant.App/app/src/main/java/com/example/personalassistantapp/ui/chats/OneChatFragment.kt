package com.example.personalassistantapp.ui.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalassistantapp.adapters.MessageAdapter
import com.example.personalassistantapp.databinding.FragmentOneChatBinding
import com.example.personalassistantapp.helpers.ApiRequestHelper
import com.example.personalassistantapp.helpers.TokenManager
import com.example.personalassistantapp.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OneChatFragment : Fragment() {
    private val client = OkHttpClient()
    private lateinit var _tokenManager: TokenManager
    private lateinit var _messageAdapter: MessageAdapter
    private var _binding: FragmentOneChatBinding? = null
    private var _chatId: String? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOneChatBinding.inflate(inflater, container, false)
        _tokenManager = TokenManager(requireContext())

        if (_tokenManager.getEmailFromToken()?.isNotEmpty() == true) {

            _messageAdapter = MessageAdapter(mutableListOf())
            binding.messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.messagesRecyclerView.adapter = _messageAdapter

            binding.sendButton.setOnClickListener {
                val userMessage = binding.messageEditText.text.toString()
                if (userMessage.isNotEmpty()) {
                    addMessageToChat(userMessage, true, _chatId)
                    sendMessageToApi(userMessage, _tokenManager.getEmailFromToken()!!)
                    binding.messageEditText.text.clear()
                }
            }
        }

        val root: View = binding.root
        return root
    }

    private fun sendMessageToApi(userMessage: String, emailFromToken: String) {
        val baseUrl = ApiRequestHelper.urlBuilder(
            ApiRequestHelper.CHATSCONTROLLER, ApiRequestHelper.NEW_MESSAGE_ENDPOINT_CHATCONTROLLER
        )

        var isInitialMessage: Boolean = false
        if (_messageAdapter.itemCount <= 1) isInitialMessage = true

        val url = ApiRequestHelper.valuesBuilder(
            baseUrl, "email=${emailFromToken}&isInitialMessage=${isInitialMessage}"
        )

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        val jsonBody = """
            {
              "chatId" : "${_chatId ?: ""}",
              "content": "$userMessage"
            }
        """.trimIndent()


        val requestBody: RequestBody = jsonBody.toRequestBody(jsonMediaType)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).post(requestBody).build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (!responseData.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        handleResponseData(responseData)
                    }
                } else {
                    Log.e("FetchApiData", "Error response code: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e("FetchApiData", "Exception: ${e.message}")
            }
        }
    }

    private fun handleResponseData(responseData: String) {
        try {
            val jsonObject = JSONObject(responseData)
            val content = jsonObject.getString("content")
            val fromRobot = jsonObject.getBoolean("fromRobot")
            val chatId = jsonObject.getString("chatId")
            _chatId = chatId
            addMessageToChat(content, !fromRobot, chatId)

        } catch (e: Exception) {
            Log.e("HandleResponseData", "Exception: ${e.message}")
        }
    }

    private fun addMessageToChat(message: String, isUser: Boolean, chatId: String?) {
        val newMessage = Message(message, isUser, chatId)
        _messageAdapter.addMessage(newMessage)
        binding.messagesRecyclerView.scrollToPosition(_messageAdapter.itemCount - 1)
    }
}