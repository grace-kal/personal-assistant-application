using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess
{
    public class ChatRepository(Context context) : IChatRepository
    {
        public async Task<IEnumerable<Chat>> GetUserChats(string email)
        {
            return context.Chats.Where(c => c.Sender.Email == email);
        }
    }
}
