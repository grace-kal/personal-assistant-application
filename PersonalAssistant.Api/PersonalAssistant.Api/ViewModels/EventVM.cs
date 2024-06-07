namespace PersonalAssistant.Api.ViewModels
{
    public class EventVM
    {
        public string? Id { get; set; }
        public required string Title { get; set; }
        public required string Description { get; set; }
        public string? StartDate { get; set; }
        public string? EndDate { get; set; }
        public string? StartTime { get; set; }
        public string? EndTime { get; set; }
        public IList<string>? InvitedUsers { get; set; }
    }
}
