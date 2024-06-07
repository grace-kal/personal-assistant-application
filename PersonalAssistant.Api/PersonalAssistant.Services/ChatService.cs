using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Services
{
    public class ChatService(IChatRepository repository, IUserService userService) : IChatService

    {
        public async Task<IEnumerable<Chat>> GetUserChats(string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            return await repository.GetUserChats(email);
        }
    }
}
