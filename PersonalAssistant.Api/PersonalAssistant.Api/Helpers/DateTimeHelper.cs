using Microsoft.AspNetCore.Http;

namespace PersonalAssistant.Api.Helpers
{
    public class DateTimeHelper
    {
        public DateTime GetDateFromJsonString(string date)
        {
            const string format = "yyyy-MM-dd";

            return DateTime.ParseExact(date, format, System.Globalization.CultureInfo.InvariantCulture);
        }
    }
}
