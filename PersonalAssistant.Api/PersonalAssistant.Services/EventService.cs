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
    public class EventService(IEventRepository repository,IUserService userService) : IEventService
    {
        public async Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }

            return await repository.GetAllEventsForDate(date, email);
        }
    }
}
