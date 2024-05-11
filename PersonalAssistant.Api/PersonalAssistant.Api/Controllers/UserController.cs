using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using PersonalAssistant.Api.ViewModels;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        [HttpPost("Register")]
        public async Task<ActionResult<bool>> Register(LoginUser user)
        {
            if (!user.Username.IsNullOrEmpty())
                return true;
            else return false;
        }

        [HttpGet("GetRegister")]
        public async Task<ActionResult<bool>> GetRegister([FromQuery] string username)
        {
            if (!username.IsNullOrEmpty())
                return true;
            else return false;
        }
    }
}
