namespace PersonalAssistant.Api.ViewModels
{
    public class ClothVM
    {
        public string? Title { get; set; }
        public string? Season { get; set; }
        public string? Description { get; set; }
        public IFormFile? Image { get; set; }
    }
}
