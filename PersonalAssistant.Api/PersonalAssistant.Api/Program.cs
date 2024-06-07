using Microsoft.EntityFrameworkCore;
using PersonalAssistant.DataAccess;
using PersonalAssistant.Api;
using Microsoft.AspNetCore.Identity;
using PersonalAssistant.Api.Helpers;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services;
using PersonalAssistant.Services.Interfaces;
using OpenAI_API;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddSingleton(new OpenAIAPI("sk-proj-gxACBvL40yJPXhjTAj8QT3BlbkFJLxBT2tVEezMcEUbYtb4J"));
builder.Services.AddControllers();

builder.Services.AddDbContext<Context>(options =>
                options
                .UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddAutoMapper(typeof(Mapping));

builder.Services.AddScoped<DateTimeHelper>();

builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IEventService, EventService>();
builder.Services.AddScoped<ITaskService, TaskService>();
builder.Services.AddScoped<INoteService, NoteService>();
builder.Services.AddScoped<IChatService, ChatService>();

builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IEventRepository, EventRepository>();
builder.Services.AddScoped<ITaskRepository, TaskRepository>();
builder.Services.AddScoped<INoteRepository, NoteRepository>();
builder.Services.AddScoped<IChatRepository, ChatRepository>();

builder.Services.AddIdentity<User, IdentityRole>(options => options.SignIn.RequireConfirmedAccount = false)
    .AddRoles<IdentityRole>()
    .AddEntityFrameworkStores<Context>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseDefaultFiles();
app.UseStaticFiles();

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
