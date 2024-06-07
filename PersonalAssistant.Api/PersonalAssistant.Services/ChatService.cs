using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.VisualStudio.Web.CodeGenerators.Mvc.Templates.BlazorIdentity.Pages.Manage;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Models.Enums;
using PersonalAssistant.Services.Interfaces;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services
{
    public class ChatService(IChatRepository repository, IUserService userService) : IChatService

    {
        public async Task<IEnumerable<Chat>> GetUserChats(string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            return await repository.GetUserChats(email);
        }

        public async Task<int> CreateNewChat(string messageContent, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            var newChat = new Chat()
            {
                ChatType = ChatType.None,
                CreatedAt = DateTime.Now,
                ImportanceLevel = ImportanceLevel.Urgent,
                Title = messageContent.Substring(0,messageContent.Length/2),
                SenderId = ""
            };
            return await repository.CreateNewChat(newChat, email);
        }

        public async Task<List<Message>> GetChatHistory(string email, int chatId)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            return await repository.GetChatHistory(email, chatId);
        }

        public async Task CreateNewMessage(Message newUserMessage)
        {
            newUserMessage.CreatedAt =DateTime.Now;
            await repository.CreateNewMessage(newUserMessage);
        }
    }
}
