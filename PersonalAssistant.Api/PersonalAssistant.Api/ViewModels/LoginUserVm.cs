using Azure.Core.Serialization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace PersonalAssistant.Api.ViewModels
{
    public class LoginUserVm
    {
        [JsonPropertyName("username")]
        public required string Username { get; set; }

        [JsonPropertyName("password")]
        public required string PasswordHash{ get; set; }
    }
}
