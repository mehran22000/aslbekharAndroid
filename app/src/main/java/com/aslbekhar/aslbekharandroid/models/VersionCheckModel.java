package com.aslbekhar.aslbekharandroid.models;

/**
 * Created by Amin on 11/06/2016.
 * <p>
 * This class will be used for
 */
public class VersionCheckModel {

    String _id;
    String device;
    int current;
    int minSupport;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getMinSupport() {
        return minSupport;
    }

    public void setMinSupport(int minSupport) {
        this.minSupport = minSupport;
    }
}