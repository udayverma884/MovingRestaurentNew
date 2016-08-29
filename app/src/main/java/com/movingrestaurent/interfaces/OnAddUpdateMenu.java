package com.movingrestaurent.interfaces;

/**
 * Created by Android on 6/7/2016.
 */
public interface OnAddUpdateMenu {

    long LOCATION_SYNC_TIME = 5 * 1000;
    int LOCATION_SYNC_DISTANCE = 0;

    void onSuccess(String msg);

    void onFailure(String msg);
}
