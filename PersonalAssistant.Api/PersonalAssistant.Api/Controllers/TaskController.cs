using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;
using System.Threading.Tasks;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TaskController(ITaskService service, DateTimeHelper dateHelper, IMapper mapper) : Controller
    {
        [HttpGet("GetAllTasksForDate")]
        public async Task<ActionResult> GetAllEventsForDate([FromQuery] string email, string date)
        {
            var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);
            var list = MapTasks(await service.GetAllTasksForDate(dateInDateFromJsonString, email));

            return Ok(list);
        }

        [HttpPost("CreateTask")]
        public async Task<ActionResult> CreateTask([FromQuery] string email, [FromBody] TaskVM @event)
        {
            var task = mapper.Map<Models.Task>(@event);
            try
            {
                task.FromDateTime = dateHelper.GetDateTimeFromJsonString(@event.StartDate, @event.StartTime);
                task.ToDateTime = dateHelper.GetDateTimeFromJsonString(@event.EndDate, @event.EndTime);
            }
            catch (Exception e)
            {
                //ignore
            }
            await service.CreateTask(task, email);
            return Ok();
        }

        private object MapTasks(IEnumerable<Models.Task> enumerable)
        {
            var mapped = new List<TaskVM>();
            foreach (var e in enumerable)
            {
                var eVM = new TaskVM()
                {
                    Id = e.Id.ToString(),
                    Title = e.Title,
                    Description = e.Description,
                    StartDate = e.FromDateTime.Date.ToString("yyyy-MM-dd"),
                    StartTime = e.FromDateTime.ToString("HH:mm"),
                    EndDate = e.ToDateTime.Date.ToString("yyyy-MM-dd"),
                    EndTime = e.ToDateTime.ToString("HH:mm"),
                    IsDone = e.IsDone
                };
                mapped.Add(eVM);
            }

            return mapped;

        }
    }
}
