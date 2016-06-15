package com.aslbekhar.aslbekharandroid.models;

import com.alibaba.fastjson.JSON;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Amin on 13/06/2016.
 *
 */
public class AnalyticsDataModel {

    String device;
    String day;
    String month;
    String value;
    String key;
    String year;
    String lat;
    String lon;

    public AnalyticsDataModel(String value, String key) {
        this.value = value;
        this.key = key;
        device = Snippets.getSP(Constants.DEVICE_ID);
        lat = Snippets.getSP(Constants.LAST_LAT);
        lon = Snippets.getSP(Constants.LAST_LONG);
        Calendar calendar = Calendar.getInstance();
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        year = String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static void saveAnalytic(String value, String key){
        List<AnalyticsDataModel> analyticsDataModels = new ArrayList<>();
        String jsonData = Snippets.getSP(Constants.SAVED_ANALYTICS);
        if (!jsonData.equals(Constants.FALSE)){
            try {
                analyticsDataModels = JSON.parseArray(jsonData, AnalyticsDataModel.class);
            } catch (Exception ignored){}
        }
        analyticsDataModels.add(new AnalyticsDataModel(value, key));
        Snippets.setSP(Constants.SAVED_ANALYTICS, JSON.toJSONString(analyticsDataModels));
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
