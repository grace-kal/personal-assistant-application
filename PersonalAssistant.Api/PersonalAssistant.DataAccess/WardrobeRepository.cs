using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using Azure.Storage.Blobs;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using PersonalAssistant.DataAccess.Interfaces;
using PersonalAssistant.Models;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.DataAccess
{
    public class WardrobeRepository(Context context, BlobServiceClient blobServiceClient) : IWardrobeRepository
    {
        private const string ContainerName = "clothimages";

        public async Task CreateClothItems(Cloth newCloth, string email)
        {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
            newCloth.UserId = user!.Id;
            var result = await context.Clothes.AddAsync(newCloth);
            await context.SaveChangesAsync();

            newCloth.Id = result.Entity.Id;
            if (newCloth.Id != 0)
                await SaveToBlobStorage(newCloth);
        }

        public async Task<List<Cloth>> GetClothes(string email)
        {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
            var result = context.Clothes.Where(c => c.UserId == user.Id).ToList();
            return result;
        }

        public async Task<Cloth> GetClothInfo(string email, int clothIdInt)
        {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
            var result = await context.Clothes.FirstOrDefaultAsync(c => c.Id == clothIdInt && c.UserId == user.Id);
            return result;
        }

        private async Task SaveToBlobStorage(Cloth newCloth)
        {
            var containerClient = blobServiceClient.GetBlobContainerClient(ContainerName);
            await containerClient.CreateIfNotExistsAsync();

            var timestamp = DateTime.UtcNow.ToString("yyyy-MM-ddTHH-mm-ss-fffZ");
            var blobFileName = $"{newCloth.Id}_{timestamp}";

            var blobClient = containerClient.GetBlobClient(blobFileName);

            await using (var stream = newCloth.Image?.OpenReadStream())
            {
                await blobClient.UploadAsync(stream, overwrite: true);
            }

            var clothEntity = await context.Clothes.FindAsync(newCloth.Id);
            if (clothEntity != null)
            {
                clothEntity.BlobUri = blobClient.Uri.ToString();
                await context.SaveChangesAsync();
            }
        }
    }
}
