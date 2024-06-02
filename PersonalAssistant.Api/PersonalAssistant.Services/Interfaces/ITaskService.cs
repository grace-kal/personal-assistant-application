﻿using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.Services.Interfaces
{
    public interface ITaskService
    {
        Task<IEnumerable<Task>> GetAllTasksForDate(DateTime date, string email);
    }
}
