using System.ComponentModel.Design;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class EventController(IMapper mapper, IEventService service) : Controller
    {
        [HttpGet("GetAllEventsForDate")]
        public async Task<ActionResult> GetAllEventsForDate([FromQuery] string email, DateTime date)
        {
            return Ok(await service.GetAllEventsForDate(date, email));
        }
    }
}
