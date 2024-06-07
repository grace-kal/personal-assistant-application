using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using OpenAI_API;
using OpenAI_API.Completions;
using PersonalAssistant.Api.ViewModels;
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

                var result = await openAiApi.Completions.CreateCompletionAsync(message.Content, max_tokens: 50);
                var responseText = result.ToString();


                // TODO: Save the response to the database or process it as needed

                var messageFromAi = new MessageVM()
                {
                    Content = responseText,
                    FromRobot = true
                };
                return Ok(messageFromAi);
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
