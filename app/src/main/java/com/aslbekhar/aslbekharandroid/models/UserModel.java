package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 05/06/2016.
 * <p>
 * This class will be used for
 */
public class UserModel {

    String buId;
    String buEmail;
    String buPassword;
    String buCityName;
    String buCityNameFa;
    String buBrandId;
    String buBrandName;
    String buBrandCategory;
    String buStoreId;
    String buStoreName;
    String buStoreAddress;
    String buStoreHours;
    String buDistributor;
    String buStoreLat;
    String buStoreLon;
    String buAreaCode;
    String buTel;
    String buBrandLogoName;
    String dStartDate;
    String dEndDate;
    String dStartDateFa;
    String dEndDateFa;
    int dPrecentage;
    String dNote;
    String err = "";

    public String getBuId() {
        return buId;
    }

    public void setBuId(String buId) {
        this.buId = buId;
    }

    public String getBuEmail() {
        return buEmail;
    }

    public void setBuEmail(String buEmail) {
        this.buEmail = buEmail;
    }

    public String getBuPassword() {
        return buPassword;
    }

    public void setBuPassword(String buPassword) {
        this.buPassword = buPassword;
    }

    public String getBuCityName() {
        return buCityName;
    }

    public void setBuCityName(String buCityName) {
        this.buCityName = buCityName;
    }

    public String getBuCityNameFa() {
        return buCityNameFa;
    }

    public void setBuCityNameFa(String buCityNameFa) {
        this.buCityNameFa = buCityNameFa;
    }

    public String getBuBrandId() {
        return buBrandId;
    }

    public void setBuBrandId(String buBrandId) {
        this.buBrandId = buBrandId;
    }

    public String getBuBrandName() {
        return buBrandName;
    }

    public void setBuBrandName(String buBrandName) {
        this.buBrandName = buBrandName;
    }

    public String getBuBrandCategory() {
        return buBrandCategory;
    }

    public void setBuBrandCategory(String buBrandCategory) {
        this.buBrandCategory = buBrandCategory;
    }

    public String getBuStoreId() {
        return buStoreId;
    }

    public void setBuStoreId(String buStoreId) {
        this.buStoreId = buStoreId;
    }

    public String getBuStoreName() {
        return buStoreName;
    }

    public void setBuStoreName(String buStoreName) {
        this.buStoreName = buStoreName;
    }

    public String getBuStoreAddress() {
        return buStoreAddress;
    }

    public void setBuStoreAddress(String buStoreAddress) {
        this.buStoreAddress = buStoreAddress;
    }

    public String getBuStoreHours() {
        return buStoreHours;
    }

    public void setBuStoreHours(String buStoreHours) {
        this.buStoreHours = buStoreHours;
    }

    public String getBuDistributor() {
        return buDistributor;
    }

    public void setBuDistributor(String buDistributor) {
        this.buDistributor = buDistributor;
    }

    public String getBuStoreLat() {
        return buStoreLat;
    }

    public void setBuStoreLat(String buStoreLat) {
        this.buStoreLat = buStoreLat;
    }

    public String getBuStoreLon() {
        return buStoreLon;
    }

    public void setBuStoreLon(String buStoreLon) {
        this.buStoreLon = buStoreLon;
    }

    public String getBuAreaCode() {
        return buAreaCode;
    }

    public void setBuAreaCode(String buAreaCode) {
        this.buAreaCode = buAreaCode;
    }

    public String getBuTel() {
        return buTel;
    }

    public void setBuTel(String buTel) {
        this.buTel = buTel;
    }

    public String getBuBrandLogoName() {
        return buBrandLogoName;
    }

    public void setBuBrandLogoName(String buBrandLogoName) {
        this.buBrandLogoName = buBrandLogoName;
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

    public int getdPrecentage() {
        return dPrecentage;
    }

    public void setdPrecentage(int dPrecentage) {
        this.dPrecentage = dPrecentage;
    }

    public String getdNote() {
        return dNote;
    }

    public void setdNote(String dNote) {
        this.dNote = dNote;
    }

    public String getErr() {
        if (err == null){
            return "";
        }
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
