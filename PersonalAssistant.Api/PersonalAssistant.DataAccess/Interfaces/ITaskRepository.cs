using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface ITaskRepository
    {
        Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email);
    }
}
