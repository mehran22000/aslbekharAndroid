package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 27/05/2016.
 */
public class StoreDiscountModel extends StoreModel {

    String dStartDate;
    String dEndDate;
    String dStartDateFa;
    String dEndDateFa;
    String dPrecentage;
    String dNote;
    String distance;

    public StoreDiscountModel() {
    }

    public StoreDiscountModel(String sName, String bName, String sAddress, String sHour, String sTel1, String sLat, String sLong, int sDiscount, String sVerified) {
        super(sName, bName, sAddress, sHour, sTel1, sLat, sLong, sDiscount, sVerified);
    }

    public String getdStartDate() {
        return dStartDate;
    }

    public void setdStartDate(String dStartDate) {
        this.dStartDate = dStartDate;
    }

    public String getdEndDate() {
        return dEndDate;
    }

    public void setdEndDate(String dEndDate) {
        this.dEndDate = dEndDate;
    }

    public String getdStartDateFa() {
        return dStartDateFa;
    }

    public void setdStartDateFa(String dStartDateFa) {
        this.dStartDateFa = dStartDateFa;
    }

    public String getdEndDateFa() {
        return dEndDateFa;
    }

    public void setdEndDateFa(String dEndDateFa) {
        this.dEndDateFa = dEndDateFa;
    }

    public String getdPercentage() {
        return dPrecentage;
    }

    public void setdPercentage(String dPercentage) {
        this.dPrecentage = dPercentage;
    }

    public String getdNote() {
        return dNote;
    }

    public void setdNote(String dNote) {
        this.dNote = dNote;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
