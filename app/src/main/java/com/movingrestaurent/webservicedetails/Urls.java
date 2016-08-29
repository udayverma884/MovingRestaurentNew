package com.movingrestaurent.webservicedetails;

/**
 * Created by Android on 5/25/2016.
 */
public class Urls {
    public static final String MAIN_DOMAIN = "http://appxtechnology.com/moving_restaurant/Webservice/index.php?action=";
    public static final  String USER_ACTION = "userprofile&actionMethod=";
    public static final  String USER_ACTION_NEW = "pushcrone&actionMethod=";
    public static final String LOGIN =  MAIN_DOMAIN + USER_ACTION + "doLogin";
    public static final String REGISTRATION =  MAIN_DOMAIN + USER_ACTION + "doRegistration";
    public static final String FETCH_RESTAURANT =  MAIN_DOMAIN + USER_ACTION_NEW + "sendRequests";
    public static final String FETCH_MENU =  MAIN_DOMAIN + USER_ACTION + "get_restaurant";
    public static final String UPDATE_RESTAURANT_LOCATION =  MAIN_DOMAIN + USER_ACTION + "update_lat_long";
    public static final String UPDATE_PROFILE =  MAIN_DOMAIN + USER_ACTION + "updateUserProfile";
    public static final String ADD_MENU =  MAIN_DOMAIN + USER_ACTION + "menu_item_restaurant";




}
