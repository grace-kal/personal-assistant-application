using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using PersonalAssistant.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PersonalAssistant.DataAccess
{
    public class Context(DbContextOptions<Context> options) : IdentityDbContext(options)
    {
        public DbSet<User> Users { get; set; }
        public DbSet<Event> Events { get; set; }
        public DbSet<UserEventInvite> UserEvents { get; set; }
        public DbSet<Models.Task> Tasks { get; set; }
        public DbSet<Schedule> Schedules { get; set; }
        public DbSet<Note> Notes { get; set; }
        public DbSet<Chat> Chats { get; set; }
        public DbSet<Message> Messages { get; set; }
        public DbSet<Cloth> Clothes { get; set; }
        public DbSet<Outfit> Outfits { get; set; }
        public DbSet<ClothOutfit> ClothOutfit { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            builder.Entity<ClothOutfit>()
                .HasOne(u => u.Cloth)
                .WithMany(p => p.Outfits)
                .HasForeignKey(p => p.ClothId).OnDelete(DeleteBehavior.NoAction);
            builder.Entity<ClothOutfit>()
                .HasOne(p => p.Outfit)
                .WithMany(w => w.Clothes)
                .HasForeignKey(p => p.OutfitId).OnDelete(DeleteBehavior.NoAction);

            base.OnModelCreating(builder);
        }
    }
}
