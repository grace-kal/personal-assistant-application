using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision.Models;
using OpenAI_API;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Models;
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

                if (model.Image != null || model.Image?.Length > 0)
                {
                    try
                    {
                        newCloth.DescriptionAi = await AnalyzeImageAsync(model.Image);
                    }
                    catch (Exception ex)
                    {
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

        private async Task SortVisualRecognitionInfoWithGpt(Cloth newCloth)
        {
            var promtContext =
                $"I have this description of an image. I want the info sorted as the sole object i want info on is the cloth. The only answers i want is Kind(tshirt, skirt and all possible other kinds), cloth Area(torso, head, bottom half, face, hands, overall and etc). This is a description the user provided '{newCloth.DescriptionUser}' and this is an AI visual recognition description:'{newCloth.DescriptionAi}'. Return me as result a json format answer containing all the properties i asked answeard. Color, Lenght, Area, Kind, Thickness.";

            var result = await openAiApi.Completions.CreateCompletionAsync(promtContext, max_tokens: 50);
            var responseText = result.ToString();
            
            throw new NotImplementedException();
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

                var detailedResult = $"Description: {analysis.Description?.Captions[0].Text}, Tags: {analysis.Tags}, Objects: {analysis.Objects}, Color: {analysis.Color}, Brands: {analysis.Brands}, Categories: {analysis.Categories}";

                return detailedResult;
            }
        }
    }
}
