using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services.Interfaces
{
    public interface IEventService
    {
        Task CreateEvent(Event newEvent, string email);
        Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email);
        Task<Event> GetEvent(string eventId);
        Task UpdateEvent(Event @event, string email);
        Task DeleteEvent(string eventId);
    }
}
