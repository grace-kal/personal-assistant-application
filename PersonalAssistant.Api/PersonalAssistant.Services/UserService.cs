using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using PersonalAssistant.Services.Interfaces;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Services
{
    public class UserService(IUserRepository repository) : IUserService
    {
        public async Task<bool> RegisterUser(User user)
        {
            try
            {
                return await repository.RegisterUser(user);
                #region UserManagerExample
                //    var userEmailCheck = await _userManager.FindByEmailAsync(user.Email!);
                //    if (userEmailCheck != null)
                //        return false;
                //    var result = await _userManager.CreateAsync(new User
                //    {
                //        UserName = user.UserName,
                //        Email = user.Email,
                //        FirstName = user.FirstName,
                //        LastName = user.LastName,
                //        City = user.City,
                //        Country = user.Country
                //    }, user.PasswordHash!);

                //    if (!result.Succeeded)
                //    {
                //        throw new Exception(string.Join(Environment.NewLine, result.Errors.Select(e => e.Description)));
                //    }

                //    return true;
                #endregion
            }
            catch (Exception ex)
            {
                throw new Exception(string.Join(Environment.NewLine, ex.Message));
            }
        }

        public async Task<bool> UserEmailExists(string email)
        {
            return await repository.UserEmailExists(email);
        }

        public async Task<User?> Login(User user)
        {
            return await repository.Login(user);
        }
    }
}
