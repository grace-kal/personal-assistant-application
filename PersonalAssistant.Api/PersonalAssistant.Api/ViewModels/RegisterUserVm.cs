using System.Text.Json.Serialization;

namespace PersonalAssistant.Api.ViewModels
{
    public class RegisterUserVm
    {
        [JsonPropertyName("username")]
        public required string Username { get; set; }

        [JsonPropertyName("email")]
        public required string Email { get; set; }

        [JsonPropertyName("password")]
        public required string PasswordHash { get; set; }

        [JsonPropertyName("firstName")]
        public required string FirstName { get; set; }

        [JsonPropertyName("lastName")]
        public required string LastName { get; set; }

        [JsonPropertyName("country")]
        public required string Country { get; set; }

        [JsonPropertyName("city")]
        public required string City { get; set; }
    }
}
