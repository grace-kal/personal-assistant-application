using Newtonsoft.Json;

namespace PersonalAssistant.Api.ViewModels
{
    public class NewsVM
    {
        [JsonProperty("title")]
        public string? Title { get; set; }

        [JsonProperty("description")]
        public string? Description { get; set; }

        [JsonProperty("image_url")]
        public string? ImageUrl { get; set; }

        [JsonProperty("pubDate")]
        public string? PubDate { get; set; }
        
        [JsonProperty("creator")]
        public List<string>? Creators { get; set; }
        
        [JsonProperty("source_url")]
        public string? SourceUrl { get; set; }
        
        [JsonProperty("source_icon")]
        public string? SourceIcon { get; set; }

        [JsonProperty("author")]
        public string? Author { get; set; }
    }
}
