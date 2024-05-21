using Azure.Core.Serialization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace PersonalAssistant.Api.ViewModels
{
    public class LoginUserVm
    {
        [JsonPropertyName("email")]
        public required string Email { get; set; }

        [JsonPropertyName("password")]
        public required string PasswordHash{ get; set; }
    }
}
