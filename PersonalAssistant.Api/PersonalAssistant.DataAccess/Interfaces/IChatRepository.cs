using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface IChatRepository
    {
        Task<IEnumerable<Chat>> GetUserChats(string email);
        Task<int> CreateNewChat(Chat newChat, string email);
        Task<List<Message>> GetChatHistory(string email, int chatId);
        Task CreateNewMessage(Message newUserMessage);
    }
}
