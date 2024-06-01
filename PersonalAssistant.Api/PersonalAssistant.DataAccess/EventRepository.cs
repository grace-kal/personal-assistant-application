using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess
{
    public class EventRepository(Context context) : IEventRepository
    {
        public async Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email)
        {
            var allUserEvents = context.UserEvents.Where(eu => eu.User.Email == email).ToList();
            return GetEventsRangeContainingDate(allUserEvents, date);
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
