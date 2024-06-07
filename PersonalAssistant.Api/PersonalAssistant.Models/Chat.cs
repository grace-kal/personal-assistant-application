using PersonalAssistant.Models.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PersonalAssistant.Models
{
    public class Chat
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public required string Title { get; set; }

        [Required]
        public required ChatType ChatType { get; set; }

        [Required]
        public required ImportanceLevel ImportanceLevel { get; set; }

        public DateTime CreatedAt { get; set; }

        public DateTime ClosedAt { get; set; }

        [ForeignKey(nameof(User))]
        public required string SenderId { get; set; }
        public User? Sender { get; set; }

        public IEnumerable<Message>? Messages { get; set; }
    }
}
