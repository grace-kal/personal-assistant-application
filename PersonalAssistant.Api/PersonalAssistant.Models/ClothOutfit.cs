using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PersonalAssistant.Models
{
    public class ClothOutfit
    {
        [Key]
        public int Id { get; set; }

        [ForeignKey(nameof(Outfit))]
        public required int OutfitId { get; set; }
        public required Outfit Outfit { get; set; }

        [ForeignKey(nameof(Cloth))]
        public required int ClothId { get; set; }
        public required Cloth Cloth { get; set; }
    }
}
