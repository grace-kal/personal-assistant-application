using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using OpenAI_API;
using OpenAI_API.Completions;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ChatController(IMapper mapper, IChatService service, OpenAIAPI openAiApi) : Controller
    {
        [HttpGet("GetUserChats")]
        public async Task<IActionResult> GetUserChats([FromQuery] string email)
        {
            var chats = await service.GetUserChats(email);
            var list = chats.ToList().ToList();
            var chatsVM = list.Select(mapper.Map<ChatVM>);
            return Ok(chatsVM);
        }

        [HttpPost("NewMessage")]
        public async Task<IActionResult> NewMessage([FromQuery] string email, bool isInitialMessage, [FromBody] MessageVM message)
        {
            if (string.IsNullOrEmpty(message.Content))
            {
                return BadRequest("Message content cannot be empty.");
            }

            try
            {
                var conversationHistory = new List<Message>();
                var chatId = 0;
                if (string.IsNullOrEmpty(message.ChatId) || isInitialMessage)
                {
                    chatId = await service.CreateNewChat(message.Content, email);
                    message.ChatId = chatId.ToString();
                }
                else
                {
                    // Retrieve chat history
                    conversationHistory = await service.GetChatHistory(email, int.Parse(message.ChatId!));
                    chatId = int.Parse(message.ChatId); // CHANGE: Assign chatId
                }

                var newUserMessage = new Message
                {
                    Content = message.Content,
                    FromRobot = false,
                    ChatId = chatId
                };

                //Add the new message to the DB
                await service.CreateNewMessage(newUserMessage);

                // Add the new message to the history
                conversationHistory.Add(newUserMessage);

                var prompt = string.Join("\n", conversationHistory.Select(m => m.FromRobot ? $"AI: {m.Content}" : $"User: {m.Content}"));

                #region Calling with exact model
                //calling exact model
                ////"text-davinci-003" aka GPT-3.5)
                //var completionRequest = new CompletionRequest
                //{
                //    Model = "text-davinci-003",
                //    Prompt = message.Content,
                //    MaxTokens = 50 // Specify the maximum number of tokens
                //};

                //var result = await openAiApi.Completions.CreateCompletionAsync(completionRequest);
                //var responseText = result.Completions[0].Text.Trim();
                #endregion

                var result = await openAiApi.Completions.CreateCompletionAsync(prompt, max_tokens: 50);
                var responseText = result.ToString();

                var newAiMessage = new Message
                {
                    Content = responseText,
                    FromRobot = true,
                    ChatId = chatId
                };

                await service.CreateNewMessage(newAiMessage);
                var messageVm = mapper.Map<MessageVM>(newAiMessage);
                return Ok(messageVm);
            }
            catch (HttpRequestException ex) when (ex.Message.Contains("TooManyRequests"))
            {
                return StatusCode(429, "You have exceeded your current quota. Please check your plan and billing details.");
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Internal server error: {ex.Message}");
            }
        }
    }
}
