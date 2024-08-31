using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services
{
    public class WardrobeService(IWardrobeRepository repository) : IWardrobeService
    {
        public async Task CreateClothItems(Cloth newCloth, string email)
        {
            await repository.CreateClothItems(newCloth, email);
        }

        public async Task<List<Cloth>> GetClothes(string email)
        {
            return await repository.GetClothes(email);
        }

        public async Task<Cloth> GetClothInfo(string email, int clothIdInt)
        {
            return await repository.GetClothInfo(email,clothIdInt);
        }
    }
}
