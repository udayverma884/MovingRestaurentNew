package com.movingrestaurent.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 5/3/2016.
 */
public class RegistrationBean {

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("first_name")
    public String first_name;

    @SerializedName("last_name")
    public String last_name;

    @SerializedName("email")
    public String email;




    @SerializedName("profile_pic")
    public String userImage;

    @SerializedName("restaurant_name")
    public String restaurant_name;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("restaurant_description")
    public String restaurant_description;

    @SerializedName("user_type")
    public String user_type;

    @SerializedName("latitude")
    public String latitude;


    @SerializedName("longitude")
    public String longitude;

    @SerializedName("address")
    public String address;


}
