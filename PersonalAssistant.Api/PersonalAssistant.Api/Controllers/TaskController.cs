using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TaskController(ITaskService service) : Controller
    {
        [HttpGet("GetAllTasksForDate")]
        public async Task<ActionResult> GetAllEventsForDate([FromQuery] string email, string date)
        {
            //var dateNOTImplConv = DateTime.Now;
            //return Ok(await service.GetAllEventsForDate(dateNOTImplConv, email));
            return Ok();
        }
    }
}
