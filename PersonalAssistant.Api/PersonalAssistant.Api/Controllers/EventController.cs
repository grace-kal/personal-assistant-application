using System.ComponentModel.Design;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class EventController(IMapper mapper, IEventService service, DateTimeHelper dateHelper) : Controller
    {
        [HttpGet("GetAllEventsForDate")]
        public async Task<ActionResult> GetAllEventsForDate([FromQuery] string email, string date)
        {
            var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);
            return Ok(await service.GetAllEventsForDate(dateInDateFromJsonString, email));
        }
    }
}
