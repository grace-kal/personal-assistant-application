using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.DataAccess
{
    public class ChatRepository(Context context) : IChatRepository
    {
        public async Task<IEnumerable<Chat>> GetUserChats(string email)
        {
            return context.Chats.Where(c => c.Sender.Email == email);
        }

        public async Task<int> CreateNewChat(Chat newChat, string email)
        {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
            newChat.SenderId = user!.Id;
            var chat = await context.Chats.AddAsync(newChat);
            await context.SaveChangesAsync();
            return chat.Entity.Id;
        }

        public async Task<List<Message>> GetChatHistory(string email, int chatId)
        {
            return context.Messages.Where(m => m.ChatId == chatId).ToList();
        }

        public async Task CreateNewMessage(Message newUserMessage)
        {
            await context.Messages.AddAsync(newUserMessage);
            await context.SaveChangesAsync();
        }
    }
}
