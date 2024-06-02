using Microsoft.AspNetCore.Http;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Services.Interfaces;
using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.Services
{
    public class TaskService(ITaskRepository repository, IUserService userService) : ITaskService
    {
        public async Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email)
        {
            if (!await userService.UserEmailExists(email))
            {
                throw new BadHttpRequestException("No such user email registered");
            }

            return await repository.GetAllTasksForDate(date, email);
        }
    }
}
