using AutoMapper;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;

namespace PersonalAssistant.Api
{
    public class Mapping : Profile
    {
        public Mapping()
        {
            CreateMap<RegisterUserVm, User>();
        }

    }
}
