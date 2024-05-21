using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PersonalAssistant.Models;

namespace PersonalAssistant.Services.Interfaces
{
   public interface IUserService
   {
       Task<bool> RegisterUser(User user);
   }
}
