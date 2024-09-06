using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using PersonalAssistant.Api.ViewModels;
using System.Net.Http;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class NewsController(HttpClient client) : Controller
    {
        [HttpGet("LatestNews")]
        public async Task<List<NewsVM>> LatestNews()
        {
            //Env var TODO add
            //string is in https://newsdata.io/api-key
            var newsApiKey = Environment.GetEnvironmentVariable("NEWS_API_KEY");
            var newsApiUrl = $"https://newsdata.io/api/1/news?apikey={newsApiKey}&country=us&category=technology";

            HttpResponseMessage response = await client.GetAsync(newsApiUrl);

            if (response.IsSuccessStatusCode)
            {
                string jsonResponse = await response.Content.ReadAsStringAsync();

                var newsData = JsonConvert.DeserializeObject<NewsApiResponse>(jsonResponse);

                if (newsData?.Results != null)
                {
                   
                    return newsData.Results;
                }
            }

            return null;
        }
    }
}
