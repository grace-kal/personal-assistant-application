using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface IEventRepository
    {
        Task<IEnumerable<Event>> GetAllEventsForDate(DateTime date, string email);
    }
}
