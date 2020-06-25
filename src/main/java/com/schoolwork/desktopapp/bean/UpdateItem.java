package com.schoolwork.desktopapp.bean;

public class UpdateItem {
    private String key;    //键
    private int index;     //在原来的中的第几个
    private String value;   //值
    private String type;    //类型
    private String constraint;  //约束
    private String tableName;    //来自的表
    public String getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "UpdateItem{" +
                "key='" + key + '\'' +
                ", index=" + index +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", constraint='" + constraint + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
