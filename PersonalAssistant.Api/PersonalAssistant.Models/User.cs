using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace PersonalAssistant.Models
{
    public class User : IdentityUser
    {
        [Required]
        public required string FirstName { get; set; }

        [Required]
        public required string LastName { get; set; }

        public int Age { get; set; }

        public DateTime Dob { get; set; }

        [Required]
        public required string Country { get; set; }

        [Required]
        public required string City { get; set; }

        public string? Address { get; set; }

        public DateTime CreatedAt { get; set; }

        public DateTime UpdatedAt { get; set; }

        public IEnumerable<Schedule>? Schedules { get; set; }
        public IEnumerable<UserEventInvite>? UserEvents { get; set; }
        public IEnumerable<Note>? Notes { get; set; }
        public IEnumerable<Task>? Tasks { get; set; }
        public IEnumerable<Chat>? Chats { get; set; }
        public IEnumerable<Cloth>? Clothes{ get; set; }
    }
}
