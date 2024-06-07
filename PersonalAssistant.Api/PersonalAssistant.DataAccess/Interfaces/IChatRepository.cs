using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface IChatRepository
    {
        Task<IEnumerable<Chat>> GetUserChats(string email);
    }
}
