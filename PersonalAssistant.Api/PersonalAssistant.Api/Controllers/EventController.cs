using System.ComponentModel.Design;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
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

        [HttpPost("CreateEvent")]
        public async Task<ActionResult> CreateEvent([FromQuery] string email, [FromBody] NewEventVM newEvent)
        {
            //Handle dates
            //Handle invited users
            //var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);

            var eventType = mapper.Map<Event>(newEvent);
            try
            {
                eventType.FromDateTime = dateHelper.GetDateTimeFromJsonString(newEvent.StartDate, newEvent.StartTime);
                eventType.ToDateTime = dateHelper.GetDateTimeFromJsonString(newEvent.EndDate, newEvent.EndTime);
            }
            catch (Exception e)
            {
                //ignore
            }
            
            await service.CreateEvent(eventType, email);
            //var invitedUsers = newEvent.InvitedUsers;
            //Create UserEvent for each of the users with the returned id from the creation of event
            return Ok();
        }
    }
}
