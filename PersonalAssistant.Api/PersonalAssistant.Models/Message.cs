﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PersonalAssistant.Models
{
    public class Message
    {
        [Key]
        public int Id { get; set; }
        public bool FromRobot{ get; set; }
        public DateTime CreatedAt{ get; set; }

        [Required]
        public required string Content { get; set; }

        [ForeignKey(nameof(Chat))]
        public required int ChatId { get; set; }
        public Chat? Chat { get; set; }
    }
}
