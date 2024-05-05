using PersonalAssistant.Models.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PersonalAssistant.Models
{
    public class Task
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

        public bool IsDone { get; set; }

        public string? TaskLink { get; set; }

        [ForeignKey(nameof(Models.Schedule))]
        public int? ScheduleId { get; set; }
        public Schedule? Schedule { get; set; }

        [ForeignKey(nameof(User))]
        public required string UserId { get; set; }
        public required User User { get; set; }
    }
}
