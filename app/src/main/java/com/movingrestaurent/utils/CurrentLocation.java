package com.movingrestaurent.utils;

import android.location.Location;

public class CurrentLocation {

    private Location previousLocation;
    private LocationService driverLocation;
    public static CurrentLocation dLInstance;

    public static CurrentLocation getInstance() {
        if (dLInstance == null) {
            dLInstance = new CurrentLocation();
        }
        return dLInstance;
    }
    public LocationService getConsumerLocation() {
        return driverLocation;
    }

    public void setConsumerLocation(LocationService consumerLocation) {
        this.driverLocation = consumerLocation;
    }

    /**
     * @return the previousLocation
     */
    public Location getPreviousLocation() {
        return previousLocation;
    }

    /**
     * @param previousLocation
     *            the previousLocation to set
     */
    public void setPreviousLocation(Location previousLocation) {
        this.previousLocation = previousLocation;
    }
}
