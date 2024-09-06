using Newtonsoft.Json;

namespace PersonalAssistant.Api.ViewModels
{
    public class NewsVM
    {
        [JsonProperty("title")]
        public string Title { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("image_url")]
        public string ImageUrl { get; set; }

        [JsonProperty("pubDate")]
        public string PubDate { get; set; }
    }
}
