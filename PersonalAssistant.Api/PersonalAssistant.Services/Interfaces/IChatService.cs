using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services.Interfaces
{
    public interface IChatService
    {
        Task<IEnumerable<Chat>> GetUserChats(string email);
        Task<int> CreateNewChat(string messageContent, string email);
        Task<List<Message>> GetChatHistory(string email, int chatId);
        Task CreateNewMessage(Message newUserMessage);
    }
}
