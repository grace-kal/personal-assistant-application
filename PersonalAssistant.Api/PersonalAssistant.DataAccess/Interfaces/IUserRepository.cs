using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface IUserRepository
    {
        Task<bool> UserEmailExists(string email);
        Task<bool> RegisterUser(User user);
        Task<User?> Login(User user);
    }
}
