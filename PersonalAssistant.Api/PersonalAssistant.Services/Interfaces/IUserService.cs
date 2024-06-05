using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Models;

namespace PersonalAssistant.Services.Interfaces
{
    public interface IUserService
    {
        Task<bool> RegisterUser(User user);
        Task<bool> UserEmailExists(string email);
        Task<User?> Login(User user);
        Task<IEnumerable<string>> AllUserEmails();
    }
}
