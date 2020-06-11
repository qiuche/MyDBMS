package com.schoolwork.desktopapp.entity;

public class tableSet {
    public int limitNum;
    public String arrLimit;
    public String key;
    public String limitStyle;

    public tableSet(int limitNum, String arrLimit, String key, String limitStyle) {
        this.limitNum = limitNum;
        this.arrLimit = arrLimit;
        this.key = key;
        this.limitStyle = limitStyle;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getArrLimit() {
        return arrLimit;
    }

    public void setArrLimit(String arrLimit) {
        this.arrLimit = arrLimit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLimitStyle() {
        return limitStyle;
    }

    public void setLimitStyle(String limitStyle) {
        this.limitStyle = limitStyle;
    }
}
