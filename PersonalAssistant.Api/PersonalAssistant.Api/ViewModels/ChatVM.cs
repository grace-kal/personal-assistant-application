namespace PersonalAssistant.Api.ViewModels
{
    public class ChatVM
    {
        public int Id { get; set; }
        public required string Title { get; set; }
        public DateTime CreatedAt { get; set; }
        //public required string SenderId { get; set; }
    }
}
