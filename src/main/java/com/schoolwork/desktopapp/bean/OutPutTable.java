package com.schoolwork.desktopapp.bean;

import java.util.List;

public class OutPutTable {
    private List<String> columns;         //输出的列的名称
    private List<List<String>> values;    //输出的每一行的值

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }
}
