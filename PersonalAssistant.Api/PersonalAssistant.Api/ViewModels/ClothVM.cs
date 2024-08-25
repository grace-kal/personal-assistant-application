using PersonalAssistant.Models.Enums;

namespace PersonalAssistant.Api.ViewModels
{
    public class ClothVM
    {
        public string? Id { get; set; }
        public string? Title { get; set; }
        public string? Season { get; set; }
        public string? Description { get; set; }
        public string? WeatherKind { get; set; }

        public string? ClothKind { get; set; }

        public string? ClothArea { get; set; }

        public string? ClothLenght { get; set; }

        public string? ClothThickness { get; set; }

        public string? BlobUri { get; set; }
        public string? Color { get; set; }
        public IFormFile? Image { get; set; }
    }
}
