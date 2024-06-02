using PersonalAssistant.DataAccess.Interfaces;
using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.DataAccess
{
    public class TaskRepository(Context context) : ITaskRepository
    {
        public async Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email)
        {
            var allUserTasks = context.Tasks.Where(eu => eu.User.Email == email).ToList();
            return GetTasksRangeContainingDate(allUserTasks, date);
        }

        private IEnumerable<Task> GetTasksRangeContainingDate(List<Task> allUserTasks, DateTime date)
        {
            var tasksFromDay = new List<Task>();
            foreach (var task in allUserTasks)
            {
                if (date.Date >= task.FromDateTime.Date && date.Date <= task.ToDateTime.Date)
                    tasksFromDay.Add(task);
            }

            return tasksFromDay;
        }
    }
}
