using PersonalAssistant.Models;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface INoteRepository
    {
        Task<IEnumerable<Note>> GetAllNotesForDate(DateTime date, string email);
    }
}
