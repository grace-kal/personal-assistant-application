using System.Net;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController(IMapper _mapper, IUserService _userService) : ControllerBase
    {
        [HttpPost("Register")]
        public async Task<ActionResult> Register([FromBody] RegisterUserVm request)
        {
            var user = _mapper.Map<User>(request);
            var result = await _userService.RegisterUser(user);
            if (result) return Ok(true);
            return BadRequest("This email is already registered!");
        }

        //[HttpGet("GetRegister")]
        //public async Task<ActionResult<bool>> GetRegister([FromQuery] string username)
        //{
        //    if (!username.IsNullOrEmpty())
        //        return true;
        //    else return false;
        //}
    }
}
