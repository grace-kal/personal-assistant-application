using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;

namespace PersonalAssistant.Services.Interfaces
{
    public interface IChatService
    {
        Task<IEnumerable<Chat>> GetUserChats(string email);
    }
}
