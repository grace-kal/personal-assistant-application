using System.Text.Json;
using AutoMapper;
using Azure;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision.Models;
using Microsoft.DotNet.MSIdentity.Shared;
using OpenAI_API;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
using PersonalAssistant.Models.Enums;
using PersonalAssistant.Services.Interfaces;
using Task = System.Threading.Tasks.Task;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WardrobeController(IWardrobeService service, ComputerVisionClient computerVisionClient, IMapper mapper, OpenAIAPI openAiApi) : Controller
    {
        [HttpPost("AddNewCloth")]
        public async Task<IActionResult> AddCloth([FromQuery] string email, [FromForm] ClothVM? model)
        {
            try
            {
                var newCloth = mapper.Map<Cloth>(model);

                if (model?.Image != null || model?.Image?.Length > 0)
                {
                    try
                    {
                        newCloth.DescriptionAi = await AnalyzeImageAsync(model.Image);
                    }
                    catch (Exception ex)
                    {
                        // ignored
                    }
                }

                if (newCloth.DescriptionAi != null)
                    await SortVisualRecognitionInfoWithGpt(newCloth);

                await service.CreateClothItems(newCloth, email);
                return Ok();
            }
            catch (Exception ex)
            {
                return Ok();
            }
        }

        [HttpGet("GetClothes")]
        public async Task<List<ClothVM>> GetClothes([FromQuery] string email)
        {
            return MapClothes(await service.GetClothes(email));
        }

        [HttpGet("GetClothInfo")]
        public async Task<ClothVM> GetClothInfo([FromQuery] string email, string clothId)
        {
            Int32.TryParse(clothId, out var clothIdInt);
            if (clothIdInt > 0)
            {
                var result = await service.GetClothInfo(email, clothIdInt);
                return mapper.Map<ClothVM>(result);
            }
            return new ClothVM();
        }


        private List<ClothVM> MapClothes(List<Cloth> clothes)
        {
            var list = new List<ClothVM>();
            foreach (var item in clothes)
            {
                var newItem = new ClothVM()
                {
                    Id = item.Id.ToString(),
                    Title = item.Title,
                    Description = item.DescriptionUser,
                    WeatherKind = Enum.GetName(item.WeatherKind),
                    ClothArea = Enum.GetName(item.ClothArea),
                    ClothLenght = Enum.GetName(item.ClothLenght),
                    ClothKind = Enum.GetName(item.ClothKind),
                    ClothThickness = Enum.GetName(item.ClothThickness),
                    Season = Enum.GetName(item.Season),
                    Color = item.Color,
                    BlobUri = item.BlobUri
                };
                list.Add(newItem);
            }

            return list;
        }

        [HttpGet("GetOutfits")]
        public async Task<IActionResult> GetOutfits([FromQuery] string email)
        {
            return Ok();
        }
        private async Task SortVisualRecognitionInfoWithGpt(Cloth newCloth)
        {
            var promtContext =
                $"I have this description of an image. I want the info sorted as the sole object i want info on is the cloth. " +
                $"The only answers i want is Kind (TShirt,Shirt,Top,Sweatshirt,Hoodie,Sweater,Coat,WinterJacket,BlazerJacket,Cardigan,Suitjacket,Jacket,DenimJacket,Jeans,SuitPants,Shorts,LongPants,Leggins,Skirt,Overalls,Dress,WinterDress,ThreePcsSuit,Sneakers,OfficialShoes,HighHeels,Sandals,Shoes,,Swimsuit), " +
                $"cloth Area(,Tops,Bottoms,Over,Overall), weather kind(Hot,Warm,Neutral,Cold,Freezing), cloth length(Mini,Short,Midi,Long), cloth thickness(Thin,Medium,Thick)." +
                $"This is a description the user provided '{newCloth.DescriptionUser}' and this is an AI visual recognition description:'{newCloth.DescriptionAi}'. Return me as result a json format answer containing all the properties i asked answered with these exact names of the prop in the json. Color, Length, Area, Kind, Thickness, WeatherKind." +
                $"The properties names should begin with capital letters. Make sure the json is correctly formatted so i can convert it!";

            var result = await openAiApi.Completions.CreateCompletionAsync(promtContext, max_tokens: 50);
            var responseText = result.ToString();

            AddInfoToCloth(newCloth, responseText);
        }

        private void AddInfoToCloth(Cloth newCloth, string responseText)
        {
            try
            {
                var response = JsonSerializer.Deserialize<ClothFeatures>(responseText);

                newCloth.Color = response?.Color ?? "";
                newCloth.ClothLenght = Enum.TryParse(response?.Length.Replace("/", "_"), out ClothLenght parsedLength) ? parsedLength : ClothLenght.Unknown;
                newCloth.ClothArea = Enum.TryParse(response?.Area.Replace("/", "_"), out ClothArea parsedArea) ? parsedArea : ClothArea.Unknown;
                newCloth.ClothKind = Enum.TryParse(response?.Kind, out ClothKind parsedKind) ? parsedKind : ClothKind.Unknown;
                newCloth.ClothThickness = Enum.TryParse(response?.Thickness.Replace("/", "_"), out ClothThickness parsedThickness) ? parsedThickness : ClothThickness.Unknown;
                newCloth.WeatherKind = Enum.TryParse(response?.WeatherKind.Replace("/", "_"), out WeatherKind parsedWeatherKind) ? parsedWeatherKind : WeatherKind.Unknown;
            }
            catch (Exception ex)
            {
                // ignored
            }
        }

        private async Task<string> AnalyzeImageAsync(IFormFile image)
        {
            await using (var imageStream = image.OpenReadStream())
            {
                var features = new List<VisualFeatureTypes?>
                {
                    VisualFeatureTypes.Description,
                    VisualFeatureTypes.Tags,
                    VisualFeatureTypes.Objects,
                    VisualFeatureTypes.Color,
                    VisualFeatureTypes.Brands,
                    VisualFeatureTypes.Categories
                };
                var analysis = await computerVisionClient.AnalyzeImageInStreamAsync(imageStream, features);

                //Tags for the image
                var tagInfo = "List of tags found in the image. ";
                if (analysis.Tags.Count > 0)
                    foreach (var tag in analysis.Tags)
                    {
                        tagInfo += $"Tag name: {tag.Name}, tag confidence in accuracy: {tag.Confidence}; ";
                    }

                //Captions  for the image
                var captionInfo = "List of desciption captions found in the image. ";
                if (analysis.Description.Captions.Count > 0)
                    foreach (var descriptionCaption in analysis.Description.Captions)
                    {
                        captionInfo += $"Caption: {descriptionCaption.Text}; ";
                    }

                //Objects on the image
                var objectsInfo = "List of objects detected on the image. ";
                if (analysis.Objects.Count > 0)
                    foreach (var objects in analysis.Objects)
                    {
                        objectsInfo += $"Object property: {objects.ObjectProperty}, object(h,w,x,y): {objects.Rectangle.H}, {objects.Rectangle.W}, {objects.Rectangle.X}, {objects.Rectangle.Y}; ";
                    }

                var brandsInfo = "List if brands detected on the image. ";
                if (analysis.Brands.Count > 0)
                    foreach (var brand in analysis.Brands)
                    {
                        brandsInfo += $"Brand: {brand.Name}; ";
                    }

                var categoriesInfo = "List of categories in the image. ";
                if (analysis.Categories.Count > 0)
                    foreach (var category in analysis.Categories)
                    {
                        categoriesInfo += $"Category name: {category.Name}; ";
                    }

                var colorsInfo = $"Colors information. Background color: {analysis.Color.DominantColorBackground}, foreground color: {analysis.Color.DominantColorForeground}, accent color: {analysis.Color.AccentColor}; ";

                var detailedResult = $"Description: {captionInfo}, Tags: {tagInfo}, Objects: {objectsInfo}, Brands: {brandsInfo}, Categories: {categoriesInfo}, Colors: {colorsInfo}";

                return detailedResult;
            }
        }
    }
}
