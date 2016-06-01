package com.aslbekhar.aslbekharandroid.models;

import java.util.List;

/**
 * Created by Amin on 19/05/2016.
 */
public class CategoryModel {

    String title;
    List<String> images;

    public CategoryModel() {
    }

    public CategoryModel(String title) {
        this.title = title;
    }

    public CategoryModel(String title, List<String> images) {
        this.title = title;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
