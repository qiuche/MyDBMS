package com.schoolwork.desktopapp.entity;

import java.util.ArrayList;
import java.util.List;

public class TableValue {
    private String column;
    private List<String> value=new ArrayList<>();

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public TableValue(String column) {
        this.column = column;
    }

    public TableValue() {
    }

    @Override
    public String toString() {
        return "TableValue{" +
                "column='" + column + '\'' +
                ", value=" + value +
                '}';
    }
    public void setValue(String value) {
        this.value.add(value);
    }
}

