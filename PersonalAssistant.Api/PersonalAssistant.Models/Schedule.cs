using PersonalAssistant.Models.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PersonalAssistant.Models
{
    public class Schedule
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

        [ForeignKey(nameof(User))]
        public required string UserId { get; set; }
        public required User User { get; set; }

        public IEnumerable<Event>? Events { get; set; }
        public IEnumerable<Task>? Tasks { get; set; }
    }
}
