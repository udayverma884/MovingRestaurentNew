package com.movingrestaurent.model;

/**
 * Created by Android on 5/17/2016.
 */
public class UserMapModel {

    String lat;

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String longi;
    String Name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;
}
