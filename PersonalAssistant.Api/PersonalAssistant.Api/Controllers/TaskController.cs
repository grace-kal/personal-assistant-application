using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TaskController(ITaskService service, DateTimeHelper dateHelper) : Controller
    {
        [HttpGet("GetAllTasksForDate")]
        public async Task<ActionResult> GetAllTasksForDate([FromQuery] string email, string date)
        {
            var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);
            return Ok(await service.GetAllTasksForDate(dateInDateFromJsonString, email));
        }
    }
}
