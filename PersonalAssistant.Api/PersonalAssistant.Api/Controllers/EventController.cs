using System.ComponentModel.Design;
using System.Globalization;
using System.Text.Json.Serialization;
using System.Text.Json;
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
            var list = MapEvents(await service.GetAllEventsForDate(dateInDateFromJsonString, email));

            //var options = new JsonSerializerOptions
            //{
            //    ReferenceHandler = ReferenceHandler.Preserve
            //};
            //var json = JsonSerializer.Serialize(list, options);
            return Ok(list);
        }

        [HttpGet("GetEvent")]
        public async Task<EventVM> GetEvent([FromQuery] string eventId)
        {
            return mapper.Map<EventVM>(await service.GetEvent(eventId));
        }

        [HttpDelete("DeleteEvent")]
        public async Task<ActionResult> DeleteEvent([FromQuery] string email, string eventId)
        {
            await service.DeleteEvent(eventId);

            return Ok();
        }

        [HttpPut("UpdateEvent")]
        public async Task<ActionResult> UpdateEvent([FromQuery] string email, [FromBody] EventVM @event)
        {
            var eventType = mapper.Map<Event>(@event);
            try
            {
                eventType.FromDateTime = dateHelper.GetDateTimeFromJsonString(@event.StartDate, @event.StartTime);
                eventType.ToDateTime = dateHelper.GetDateTimeFromJsonString(@event.EndDate, @event.EndTime);
            }
            catch (Exception e)
            {
                //ignore
            }
            
            await service.UpdateEvent(eventType, email);

            return Ok();
        }

        [HttpPost("CreateEvent")]
        public async Task<ActionResult> CreateEvent([FromQuery] string email, [FromBody] EventVM @event)
        {
            //Handle dates
            //Handle invited users
            //var dateInDateFromJsonString = dateHelper.GetDateFromJsonString(date);

            var eventType = mapper.Map<Event>(@event);
            try
            {
                eventType.FromDateTime = dateHelper.GetDateTimeFromJsonString(@event.StartDate, @event.StartTime);
                eventType.ToDateTime = dateHelper.GetDateTimeFromJsonString(@event.EndDate, @event.EndTime);
            }
            catch (Exception e)
            {
                //ignore
            }
            
            await service.CreateEvent(eventType, email);
            //var invitedUsers = @event.InvitedUsers;
            //Create UserEvent for each of the users with the returned id from the creation of event
            return Ok();
        }

        private IEnumerable<EventVM> MapEvents(IEnumerable<Event> allEventsFromDate)
        {
            var mapped = new List<EventVM>();
            foreach (var e in allEventsFromDate)
            {
                var eVM = new EventVM()
                {
                    Id = e.Id.ToString(),
                    Title = e.Title,
                    Description = e.Description,
                    StartDate = e.FromDateTime.Date.ToString("yyyy-MM-dd"),
                    StartTime = e.FromDateTime.ToString("HH:mm"),
                    EndDate = e.ToDateTime.Date.ToString("yyyy-MM-dd"),
                    EndTime = e.ToDateTime.ToString("HH:mm")
                };
                mapped.Add(eVM);
            }
            return mapped;
        }

    }
}
