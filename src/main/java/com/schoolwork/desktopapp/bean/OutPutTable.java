package com.schoolwork.desktopapp.bean;

import java.util.List;

public class OutPutTable {
    private List<String> columns;
    private List<List<String>> values;

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
