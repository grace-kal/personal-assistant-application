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
        private const string PromptContext =
            "I'm contacting you from a application that has overview page, where the user can see their events, tasks, notes. The app has a events page where teh user can see their events, notes page where user can find notes, tasks page where user can see their tasks. Wardrobe page where user can upload and see their clothes. News page to see teh latest news. Contact and support page where the user can contact the ai assistant. If they want to contact customer support or need any other help regarding the application they can email: kalinina.grace@gmail.com or call 02345678. This is the context from which the user contacts you. This is the user message you need to simply answer:";
        //[HttpGet("GetChatHistory")]
        //public async Task<IActionResult> GetChatHistory([FromQuery] string email, string chatId)
        //{
        //    var messages = await service.GetChatHistory(email,int.Parse(chatId));
        //    var list = messages.ToList().ToList();
        //    var messagesVm = list.Select(mapper.Map<MessageVM>);
        //    return Ok(messagesVm);
        //}

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

                //var promptFromMessages = string.Join("\n", conversationHistory.Select(m => m.FromRobot ? $"AI: {m.Content}" : $"User: {m.Content}"));
                var promptFull =string.Join("\n",new List<string>{ PromptContext ,message.Content});
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

                var result = await openAiApi.Completions.CreateCompletionAsync(promptFull, max_tokens: 50);
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
