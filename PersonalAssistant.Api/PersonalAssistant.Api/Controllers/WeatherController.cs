using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.Text.Json;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController(IWebHostEnvironment _env) : Controller
    {
        [HttpGet("Cities")]
        public async Task<ActionResult<bool>> GetCities([FromQuery] string countryCode)
        {
            var citiesFromCountry = new List<string?>();

            var filePath = Path.Combine(_env.ContentRootPath, "Resources", "LocationsInfo", "cities.json");

            using (var jsonFileReader = System.IO.File.OpenText(filePath))
            {
                var json = await jsonFileReader.ReadToEndAsync();
                using (JsonDocument doc = JsonDocument.Parse(json))
                {
                    JsonElement root = doc.RootElement;
                    if (root.ValueKind == JsonValueKind.Array)
                    {
                        foreach (JsonElement element in root.EnumerateArray())
                        {
                            if (element.TryGetProperty("country_code", out JsonElement countryCodeElement) &&
                                countryCodeElement.GetString().Equals(countryCode, StringComparison.OrdinalIgnoreCase))
                            {
                                element.TryGetProperty("name", out JsonElement cityName);
                                citiesFromCountry.Add(cityName.GetString());
                            }
                        }
                    }
                }
            }

            return Ok(citiesFromCountry.OrderBy(c => c));
        }
    }
}
