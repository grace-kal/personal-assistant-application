using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services.Interfaces
{
    public interface IWardrobeService
    {
        Task CreateClothItems(Cloth newCloth, string email);
    }
}
