using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.DataAccess
{
    public class EventRepository(Context context) : IEventRepository
    {
        public async Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email)
        {
            var allUserEvents = context.UserEvents
                .Where(eu => eu.User.Email == email)
                .Include(eu=>eu.Event)
                .ToList();

            return GetEventsRangeContainingDate(allUserEvents, date);
        }

        public async Task CreateEvent(Event newEvent, string email)
        {
            try
            {
                var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);

                var eventAdded = await context.Events.AddAsync(newEvent);
                await context.SaveChangesAsync();
                
                if(eventAdded.Entity!=null)
                {
                    var userInvite = new UserEventInvite
                    {
                        EventId = eventAdded.Entity.Id,
                        UserId = user.Id,
                        User = user,
                        Event = eventAdded.Entity
                    };
                    await context.UserEvents.AddAsync(userInvite);
                    await context.SaveChangesAsync();
                }
            }
            catch (Exception ex)
            {
                //ignore
            }

        }

        private IEnumerable<Event> GetEventsRangeContainingDate(List<UserEventInvite> allUserEvents, DateTime date)
        {
            var eventsFromDay = new List<Event>();
            foreach (var userEvent in allUserEvents)
            {
                if (date.Date >= userEvent.Event.FromDateTime.Date && date.Date <= userEvent.Event.ToDateTime.Date)
                    eventsFromDay.Add(userEvent.Event);
            }

            return eventsFromDay;
        }
    }
}
