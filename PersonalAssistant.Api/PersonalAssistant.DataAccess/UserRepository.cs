using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Microsoft.VisualStudio.Web.CodeGenerators.Mvc.Templates.BlazorIdentity.Pages.Manage;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess
{
    public class UserRepository(Context context) : IUserRepository
    {
        public async Task<bool> UserEmailExists(string email)
        {
            try
            {
                return await context.Users.FirstOrDefaultAsync(u => u.Email == email) != null;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        public async Task<bool> RegisterUser(User user)
        {
            try
            {
                var result = await context.Users.AddAsync(user);
                await context.SaveChangesAsync();
                return result.State == EntityState.Unchanged;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        public async Task<User?> Login(User user)
        {
            var result = await context.Users.FirstOrDefaultAsync(u => u.Email == user.Email);
            if (result == null) return null;

            return user.PasswordHash == result.PasswordHash ? result : null;
        }

        public async Task<IEnumerable<string?>?> AllUserEmails()
        {
            var users = context.Users.ToList();
            return users.Select(u => u.Email);

        }
    }
}
