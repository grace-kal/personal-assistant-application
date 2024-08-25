﻿using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PersonalAssistant.DataAccess.Migrations
{
    /// <inheritdoc />
    public partial class clothcoloradd : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Color",
                table: "Clothes",
                type: "nvarchar(max)",
                nullable: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Color",
                table: "Clothes");
        }
    }
}
