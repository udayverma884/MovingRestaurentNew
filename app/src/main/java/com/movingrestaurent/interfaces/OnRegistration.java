package com.movingrestaurent.interfaces;


import com.movingrestaurent.model.RegistrationBean;

/**
 * Created by Android on 5/2/2016.
 */
public interface OnRegistration {

    long LOCATION_SYNC_TIME = 5 * 1000;
    int LOCATION_SYNC_DISTANCE = 0;

    void onSuccess(RegistrationBean registrationBean);

    void onFailure(String msg);
}
