using Microsoft.AspNetCore.Http;

namespace PersonalAssistant.Api.Helpers
{
    public class DateTimeHelper
    {
        const string DateFormat = "yyyy-MM-dd";
        const string TimeFormat = "hh-mm";
        const string DateTimeFormat = "yyyy-MM-ddThh-mm";


        public DateTime GetDateFromJsonString(string date)
        {

            return DateTime.ParseExact(date, DateFormat, System.Globalization.CultureInfo.InvariantCulture);
        }

        public DateTime GetDateTimeFromJsonString(string? date, string? time)
        {
            var dateTimeString = $"{date}T{time}";
            return DateTime.ParseExact(dateTimeString, DateTimeFormat, System.Globalization.CultureInfo.InvariantCulture);
        }
    }
}
