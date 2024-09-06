using Newtonsoft.Json;

namespace PersonalAssistant.Api.ViewModels
{
    public class NewsApiResponse
    {
        [JsonProperty("results")]
        public List<NewsVM> Results { get; set; }
    }
}
