package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 19/05/2016.
 */
public class CityModel {

    String id;
    String persianName;
    String englishName;

    public CityModel(String id, String persianName, String englishName) {
        this.id = id;
        this.persianName = persianName;
        this.englishName = englishName;
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
}
