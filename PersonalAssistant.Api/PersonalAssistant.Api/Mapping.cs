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
            CreateMap<LoginUserVm, User>();

            CreateMap<EventVM, Event>();
            CreateMap<Event, EventVM>();
            CreateMap<TaskVM, Models.Task>();

            CreateMap<Chat, ChatVM>();
            CreateMap<Message, MessageVM>();

            CreateMap<Cloth, ClothVM>();
            CreateMap<ClothVM, Cloth>()
                .ForMember(dest => dest.DescriptionUser, opt => opt.MapFrom(src => src.Description));
        }

    }
}
