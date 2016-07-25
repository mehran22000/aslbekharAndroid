package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 19/05/2016.
 */
public class CityModel {

    String id;
    String persianName;
    String englishName;
    String lat;
    String lon;

    public CityModel() {
    }


    public CityModel(String id, String persianName, String englishName, String lat, String lon) {
        this.id = id;
        this.persianName = persianName;
        this.englishName = englishName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersianName() {
        return persianName;
    }

    public void setPersianName(String persianName) {
        this.persianName = persianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
