package com.schoolwork.desktopapp.bean;

public class OpValue {
    private String key;
    private String value;
    private String operator;
    private String uid;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "OpValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", operator='" + operator + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
