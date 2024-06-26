﻿using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController(IMapper mapper, IUserService userService) : ControllerBase
    {
        [HttpPost("AllUserEmails")]
        public async Task<IEnumerable<string>> Register()
        {
            return await userService.AllUserEmails();
        }

        [HttpPost("Register")]
        public async Task<ActionResult> Register([FromBody] RegisterUserVm request)
        {
            var user = mapper.Map<User>(request);

            if (await userService.UserEmailExists(user.Email!))
                return Ok(new { Error = "This email is already registered!" });

            var result = await userService.RegisterUser(user);
            if (result) return Ok();

            return BadRequest();
        }

        [HttpPost("Login")]
        public async Task<ActionResult> Login([FromBody] LoginUserVm request)
        {
            var user = mapper.Map<User>(request);
            var result = await userService.Login(user);

            if (result == null) return Unauthorized(new { Error = "Wrong credentials" });

            var token = GenerateJwtToken(result);
            return Ok(new { Token = token, User = user });
        }

        private string GenerateJwtToken(User user)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = System.Text.Encoding.UTF8.GetBytes("your-very-secure-key-12345-secure");

            // Verify key length
            if (key.Length < 16)
            {
                throw new ArgumentException("The key must be at least 16 bytes long.");
            }

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new Claim[]
                {
                    new(ClaimTypes.Email, user.Email!),
                }),
                Expires = DateTime.UtcNow.AddHours(7),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }
    }
}
