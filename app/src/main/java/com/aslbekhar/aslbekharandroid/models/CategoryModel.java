package com.aslbekhar.aslbekharandroid.models;

import java.util.List;

/**
 * Created by Amin on 19/05/2016.
 */
public class CategoryModel {

    String title;
    String cId;
    List<String> images;
    int latestBrandLogoUsed = 0;

    public CategoryModel() {
    }

    public CategoryModel(String title) {
        this.title = title;
    }

    public CategoryModel(String title, String cId, List<String> images) {
        this.title = title;
        this.images = images;
        this.cId = cId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getLatestBrandLogoUsed() {
        return latestBrandLogoUsed;
    }

    public void setLatestBrandLogoUsed(int latestBrandLogoUsed) {
        this.latestBrandLogoUsed = latestBrandLogoUsed;
    }
}
