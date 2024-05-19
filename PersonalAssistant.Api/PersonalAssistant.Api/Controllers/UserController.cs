using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController(IMapper _mapper) : ControllerBase
    {
        [HttpPost("Register")]
        public async Task<ActionResult<bool>> Register([FromBody] RegisterUserVm request)
        {
            var user = _mapper.Map<User>(request);

            if (!user.UserName.IsNullOrEmpty())
                return true;
            else return false;
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
