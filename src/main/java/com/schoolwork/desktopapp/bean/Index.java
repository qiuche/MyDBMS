package com.schoolwork.desktopapp.bean;

import java.util.ArrayList;
import java.util.List;

public class Index {
    private int key;
    private List<Integer>  valueList=new ArrayList<>();

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<Integer> getValueList() {
        return valueList;
    }

    public void setValueList(List<Integer> valueList) {
        this.valueList = valueList;
    }

    public Index() {
    }

    public Index(int key) {
        this.key = key;
    }

    public Index(int key, List<Integer> valueList) {
        this.key = key;
        this.valueList=valueList;
    }
    public Index(int key, int value) {
        this.key = key;
        this.valueList.add(value);
    }
    public void setValueList(int value) {
        this.valueList .add(value);
    }
    @Override
    public String toString() {
        return "Index{" +
                "key=" + key +
                ", valueList=" + valueList +
                '}';
    }
}
