using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.DataAccess
{
    public class NoteRepository(Context context) : INoteRepository
    {
        public async Task<IEnumerable<Note>> GetAllNotesForDate(DateTime date, string email)
        {
            var allUserNotes = context.Notes.Where(eu => eu.User.Email == email && eu.CreatedAt.Date == date).ToList();
            return allUserNotes;
        }
    }
}
