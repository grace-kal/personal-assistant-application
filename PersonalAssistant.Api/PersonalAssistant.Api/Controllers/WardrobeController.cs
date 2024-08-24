using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision;
using Microsoft.Azure.CognitiveServices.Vision.ComputerVision.Models;
using OpenAI_API;
using PersonalAssistant.Api.ViewModels;
using PersonalAssistant.Services.Interfaces;

namespace PersonalAssistant.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WardrobeController(IWardrobeService service, ComputerVisionClient computerVisionClient) : Controller
    {
        [HttpPost("AddNewCloth")]
        public async Task<IActionResult> AddCloth([FromQuery] string email, [FromBody] ClothVM model)
        {
            try
            {
                var descriptionAI = "";

                //image recognition -get description
                if (model.Image != null || model.Image?.Length > 0)
                {
                    try
                    {
                        descriptionAI = await AnalyzeImageAsync(model.Image);
                    }
                    catch (Exception ex)
                    {
                    }
                }
                //save in sql with both descriptions


                //save in blob with id of the created cloth
            }
            catch (Exception ex)
            {
                return Ok();
            }

            return Ok();
        }

        private async Task<string> AnalyzeImageAsync(IFormFile image)
        {
            await using (var imageStream = image.OpenReadStream())
            {
                var features = new List<VisualFeatureTypes?> { VisualFeatureTypes.Description };
                var analysis = await computerVisionClient.AnalyzeImageInStreamAsync(imageStream, features);

                return analysis.Description.Captions.Count > 0 ? analysis.Description.Captions[0].Text : "No description available.";
            }
        }
    }
}
