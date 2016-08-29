package com.movingrestaurent.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.movingrestaurent.interfaces.OnLogin;


/**
 * Created by satender on 26/12/15.
 */

public class LocationCapture extends BroadcastReceiver {
    private final static String TAG = LocationCapture.class.getSimpleName();
    private Context context;
    private CurrentLocation mApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");
        Location currentLocation = null;
        this.context = context;
        mApplication = CurrentLocation.getInstance();
        try {
            if (mApplication.getPreviousLocation() != null) {

                currentLocation = mApplication.getConsumerLocation()
                        .getLocation();
                Log.d(TAG, "if " + currentLocation.getLatitude() + "");
                if (currentLocation != null
                        && mApplication.getPreviousLocation().distanceTo(
                        currentLocation) >= OnLogin.LOCATION_SYNC_DISTANCE) {

                    syncDriverLocation(currentLocation);
                    mApplication.setPreviousLocation(currentLocation);
                }
            } else {

                if(mApplication==null){
                    mApplication = CurrentLocation.getInstance();
                }
                Log.d(TAG, "else" + mApplication
                        .getConsumerLocation().getLocation());
                mApplication.setPreviousLocation(mApplication
                        .getConsumerLocation().getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncDriverLocation(Location currentLocation) {

        PreferenceConnector.writeString(context, "latitude", String.valueOf(currentLocation.getLatitude()));
        PreferenceConnector.writeString(context, "longitude", String.valueOf(currentLocation.getLongitude()));

      //  UpdateLocationApi.onUpdateLocation(Urls.UPDATE_CITY_NAME,context,currentLocation.getLatitude()+"",currentLocation.getLongitude()+"");
        //UpdateDeviceAPI.onDeviceUpdate(Constants.UPDATE_DEVICE_INFO, context, String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), regId, "driver");

    }

}
