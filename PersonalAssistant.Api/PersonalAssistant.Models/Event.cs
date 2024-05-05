using PersonalAssistant.Models.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PersonalAssistant.Models
{
    public class Event
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public required string Title { get; set; }

        [Required]
        public required string Description { get; set; }

        public ImportanceLevel ImportanceLevel { get; set; }

        [Required]
        public DateTime FromDateTime { get; set; }

        [Required]
        public DateTime ToDateTime { get; set; }

        [Required]
        public required string TimeZone { get; set; }

        public bool IsWholeDay { get; set; }

        public string? Location { get; set; }

        public string? MeetLink { get; set; }

        public string? EventLink { get; set; }

        public DateTime CreatedAt { get; set; }

        public DateTime UpdatedAt { get; set; }

        [ForeignKey(nameof(Models.Schedule))]
        public int? ScheduleId { get; set; }
        public Schedule? Schedule { get; set; }

        public IEnumerable<UserEventInvite>? EventUsers { get; set; }

    }
}
