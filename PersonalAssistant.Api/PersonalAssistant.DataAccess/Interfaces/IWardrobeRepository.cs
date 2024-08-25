using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.DataAccess.Interfaces
{
    public interface IWardrobeRepository
    {
        Task CreateClothItems(Cloth newCloth, string email);
        Task<List<Cloth>> GetClothes(string email);
    }
}
