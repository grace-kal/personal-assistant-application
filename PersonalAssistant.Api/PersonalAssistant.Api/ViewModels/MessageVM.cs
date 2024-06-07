namespace PersonalAssistant.Api.ViewModels
{
    public class MessageVM
    {
        public bool FromRobot { get; set; }
        public required string Content { get; set; }
        public string? ChatId { get; set; }
        //public required int ChatId { get; set; }
    }
}
