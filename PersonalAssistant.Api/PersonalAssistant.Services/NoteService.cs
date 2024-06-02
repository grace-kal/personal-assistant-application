using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.Identity.Client;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Services
{
    public class NoteService(INoteRepository repository, IUserService userService) : INoteService
    {
        public async Task<IEnumerable<Note>> GetAllNotesForDate(DateTime date, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }

            return await repository.GetAllNotesForDate(date, email);
        }
    }
}
