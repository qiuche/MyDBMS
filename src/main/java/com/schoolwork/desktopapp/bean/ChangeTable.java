package com.schoolwork.desktopapp.bean;

import java.util.ArrayList;
import java.util.List;

public class ChangeTable {
    private String tableName;
    private List<Integer> rows=new ArrayList<>();
    private int index;
    private List<Integer> primaryKey=new ArrayList<>();
    private List<UpdateItem> updateItems=new ArrayList<>();
    public List<Integer> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<Integer> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<UpdateItem> getUpdateItems() {
        return updateItems;
    }

    public void setUpdateItems(List<UpdateItem> updateItems) {
        this.updateItems = updateItems;
    }

    public ChangeTable(String tableName, List<Integer> rows) {
        this.tableName = tableName;
        this.rows = rows;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ChangeTable() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Integer> getRows() {
        return rows;
    }

    public void setRows(List<Integer> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ChangeTable{" +
                "tableName='" + tableName + '\'' +
                ", rows=" + rows +
                ", index=" + index +
                ", primaryKey=" + primaryKey +
                ", updateItems=" + updateItems +
                '}';
    }
}
