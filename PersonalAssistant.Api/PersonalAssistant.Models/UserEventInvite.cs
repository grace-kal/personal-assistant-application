using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PersonalAssistant.Models
{
    public class UserEventInvite
    {
        [Key]
        public int Id { get; set; }
        public bool IsAccepted { get; set; }
        public bool IsCreator { get; set; }

        [ForeignKey(nameof(User))]
        public required string UserId { get; set; }
        public required User User { get; set; }

        [ForeignKey(nameof(Event))]
        public required int EventId { get; set; }
        public required Event Event { get; set; }
    }
}
