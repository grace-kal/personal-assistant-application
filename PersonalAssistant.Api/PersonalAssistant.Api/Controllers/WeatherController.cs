using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController : Controller
    {
        [HttpGet("Cities")]
        public async Task<ActionResult<bool>> GetCities([FromQuery] string countryCode)
        {
            if (!countryCode.IsNullOrEmpty())
                return true;
            else return false;
        }
    }
}
