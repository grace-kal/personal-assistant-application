using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface ITaskRepository
    {
        System.Threading.Tasks.Task CreateTask(Task task, string email);
        Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email);
    }
}
