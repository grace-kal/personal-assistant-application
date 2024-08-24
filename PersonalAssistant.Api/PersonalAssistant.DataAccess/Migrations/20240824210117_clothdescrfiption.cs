using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PersonalAssistant.DataAccess.Migrations
{
    /// <inheritdoc />
    public partial class clothdescrfiption : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Description",
                table: "Clothes",
                newName: "DescriptionUser");

            migrationBuilder.AddColumn<string>(
                name: "DescriptionAi",
                table: "Clothes",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "DescriptionAi",
                table: "Clothes");

            migrationBuilder.RenameColumn(
                name: "DescriptionUser",
                table: "Clothes",
                newName: "Description");
        }
    }
}
