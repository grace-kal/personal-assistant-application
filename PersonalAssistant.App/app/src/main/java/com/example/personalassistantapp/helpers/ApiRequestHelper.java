package com.example.personalassistantapp.helpers;

public class ApiRequestHelper {
    public static final String HOSTADDRESS = "http://10.0.2.2:5239/api";
    //    public static final String HOSTADDRESS = "my azure url";
    // var urlString = "http://10.0.2.2:5239/api/User/GetRegister?username=lala"
    public static final String USERCONTROLLER = "/User";
    public static final String EVENTSCONTROLLER = "/Event";
    public static final String TASKSCONTROLLER = "/Task";
    public static final String NOTESCONTROLLER = "/Note";
    public static final String CHATSCONTROLLER = "/Chat";
    public static final String WARDROBECONTROLLER = "/Wardrobe";
    public static final String WEATHERCONTROLLER = "/Weather";


    //    User controller methods
    public static final String REGISTER_ENDPOINT_USERCONTROLLER = "/Register";
    public static final String LOGIN_ENDPOINT_USERCONTROLLER = "/Login";
    public static final String ALLUSEREMAILS_ENDPOINT_USERCONTROLLER = "/AllUserEmails";

    //    Weather controller methods
    public static final String CITIES_FOR_COUNTRY_ENDPOINT_WEATHERCONTROLLER = "/Cities";

    //    Event controller methods
    public static final String GET_ALL_EVENTS_FOR_DATE_ENDPOINT_EVENTCONTROLLER = "/GetAllEventsForDate";
    public static final String CREATE_EVENT_ENDPOINT_EVENTCONTROLLER = "/CreateEvent";

    //    Tasks controller methods
    public static final String GET_ALL_TASKS_FOR_DATE_ENDPOINT_TASKCONTROLLER = "/GetAllTasksForDate";

    //    Notes controller methods
    public static final String GET_ALL_NOTES_FOR_DATE_ENDPOINT_NOTECONTROLLER = "/GetAllNotesForDate";

    //    Chats controller methods
    public static final String GET_USER_CHATS_ENDPOINT_CHATCONTROLLER = "/GetUserChats";
    public static final String NEW_MESSAGE_ENDPOINT_CHATCONTROLLER = "/NewMessage";

    //    Wardrobe controller methods
    public static final String ADD_NEW_CLOTH_ENDPOINT_WARDROBECONTROLLER = "/AddNewCloth";
    public static final String GET_CLOTHES_ENDPOINT_WARDROBECONTROLLER = "/GetClothes";


    public static String urlBuilder(String controller, String method) {
        return HOSTADDRESS + controller + method;
    }

    public static String valuesBuilder(String url, String values) {
        return url + "?" + values;
    }
}
