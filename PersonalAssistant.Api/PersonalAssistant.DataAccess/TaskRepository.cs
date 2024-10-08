﻿using Microsoft.EntityFrameworkCore;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using Task = PersonalAssistant.Models.Task;

namespace PersonalAssistant.DataAccess
{
    public class TaskRepository(Context context) : ITaskRepository
    {
        public async System.Threading.Tasks.Task CreateTask(Task task, string email)
        {
            try
            {
                var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
                await context.Tasks.AddAsync(task);
                await context.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                //ignore
            }
        }

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
