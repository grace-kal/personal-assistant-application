using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PersonalAssistant.DataAccess.Migrations
{
    /// <inheritdoc />
    public partial class bloburicloth : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "BlobUri",
                table: "Clothes",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "BlobUri",
                table: "Clothes");
        }
    }
}
