using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Services
{
    public class UserService(UserManager<User> _userManager) : IUserService
    {
        public async Task<bool> RegisterUser(User user)
        {
            try
            {
                var userEmailCheck = await _userManager.FindByEmailAsync(user.Email!);
                if (userEmailCheck != null)
                    return false;
                var result = await _userManager.CreateAsync(new User
                {
                    UserName = user.UserName,
                    Email = user.Email,
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    City = user.City,
                    Country = user.Country
                }, user.PasswordHash!);

                if (!result.Succeeded)
                {
                    throw new Exception(string.Join(Environment.NewLine, result.Errors.Select(e => e.Description)));
                }

                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(string.Join(Environment.NewLine, ex.Message));
            }

        }
    }
}
