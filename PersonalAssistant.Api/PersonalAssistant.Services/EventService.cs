﻿using Microsoft.AspNetCore.Http;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services
{
    public class EventService(IEventRepository repository, IUserService userService) : IEventService
    {
        public async Task CreateEvent(Event newEvent, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            await repository.CreateEvent(newEvent, email);
        }

        public async Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }

            return await repository.GetAllEventsForDate(date, email);
        }

        public async Task<Event> GetEvent(string eventId)
        {
            return await repository.GetEvent(eventId);
        }

        public async Task UpdateEvent(Event @event, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }
            await repository.UpdateEvent(@event, email);
        }

        public async Task DeleteEvent(string eventId)
        {
            await repository.DeleteEvent(eventId);
        }
    }
}
