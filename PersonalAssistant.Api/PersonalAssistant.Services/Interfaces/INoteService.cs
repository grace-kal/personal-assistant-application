using PersonalAssistant.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PersonalAssistant.Services.Interfaces
{
    public interface INoteService
    {
        Task<IEnumerable<Note>> GetAllNotesForDate(DateTime date, string email);

    }
}
