using AutoMapper;
using Microsoft.VisualStudio.Web.CodeGenerators.Mvc.Templates.Blazor;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;

namespace PersonalAssistant.Api
{
    public class Mapping : Profile
    {
        public Mapping()
        {
            CreateMap<RegisterUserVm, User>();
            CreateMap<LoginUserVm, User>();

            CreateMap<NewEventVM, Event>();
        }

    }
}
