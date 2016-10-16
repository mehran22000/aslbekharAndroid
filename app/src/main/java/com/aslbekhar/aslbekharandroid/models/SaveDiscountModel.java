package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 16/10/2016.
 * <p>
 * This class will be used for
 */

public class SaveDiscountModel {

    String bId;
    String sId;
    String startDate;
    String endDate;
    String startDateFa;
    String endDateFa;
    String precentage;
    String note;

    public SaveDiscountModel() {
    }

    public SaveDiscountModel(String bId, String sId, String startDate, String endDate, String startDateFa, String endDateFa, String precentage, String note) {
        this.bId = bId;
        this.sId = sId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startDateFa = startDateFa;
        this.endDateFa = endDateFa;
        this.precentage = precentage;
        this.note = note;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDateFa() {
        return startDateFa;
    }

    public void setStartDateFa(String startDateFa) {
        this.startDateFa = startDateFa;
    }

    public String getEndDateFa() {
        return endDateFa;
    }

    public void setEndDateFa(String endDateFa) {
        this.endDateFa = endDateFa;
    }

    public String getPrecentage() {
        return precentage;
    }

    public void setPrecentage(String precentage) {
        this.precentage = precentage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}