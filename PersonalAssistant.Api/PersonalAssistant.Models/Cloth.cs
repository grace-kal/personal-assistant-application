using PersonalAssistant.Models.Enums;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PersonalAssistant.Models
{
    public class Cloth
    {

        [Key]
        public int Id { get; set; }

        [Required]
        public required string Title { get; set; }

        [Required]
        public required string Description { get; set; }

        //public Stream? Image { get; set; }

        [Required]
        public required Season Season { get; set; }

        [Required]
        public required WeatherKind WeatherKind { get; set; }

        [Required]
        public required ClothKind ClothKind { get; set; }

        [Required]
        public required ClothArea ClothArea { get; set; }

        [Required]
        public required ClothLenght ClothLenght { get; set; }

        [Required]
        public required ClothThickness ClothThickness { get; set; }

        [ForeignKey(nameof(User))]
        public required string UserId { get; set; }
        public required User User { get; set; }

        public IEnumerable<ClothOutfit>? Outfits { get; set; }
    }
}
