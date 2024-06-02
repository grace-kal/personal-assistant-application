using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration.UserSecrets;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class NoteController(INoteService service, DateTimeHelper dateHelper) : Controller
    {
        [HttpGet("GetAllNotesForDate")]
        public async Task<ActionResult> GetAllNotesForDate([FromQuery] string email, string date)
        {
            var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);
            return Ok(await service.GetAllNotesForDate(dateInDateFromJsonString, email));
        }
    }
}
