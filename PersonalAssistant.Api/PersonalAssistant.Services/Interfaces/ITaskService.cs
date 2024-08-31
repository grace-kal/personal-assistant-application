using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.Services.Interfaces
{
    public interface ITaskService
    {
        System.Threading.Tasks.Task CreateTask(Task task, string email);
        Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email);
    }
}
