package com.example.personalassistantapp.helpers;

public class ApiRequestHelper {
    public static final String HOSTADDRESS = "http://10.0.2.2:5239/api";
    //    public static final String HOSTADDRESS = "my azure url";
    // var urlString = "http://10.0.2.2:5239/api/User/GetRegister?username=lala"
    public static final String USERCONTROLLER = "/User";
    public static final String EVENTSCONTROLLER = "";
    public static final String TASKSCONTROLLER = "";
    public static final String WARDROBECONTROLLER = "";
    public static final String WEATHERCONTROLLER = "/Weather";


    //    User controller methods
    public static final String REGISTER_ENDPOINT_USERCONTROLLER = "/Register";

//    Weather controller methods
    public static final String CITIES_FOR_COUNTRY_ENDPOINT_WEATHERCONTROLLER = "/Cities";

    public static String urlBuilder(String controller, String method) {
        return HOSTADDRESS + controller + method;
    }

    public static String valuesBuilder(String url, String values) {
        return url + "?" + values;
    }
}
