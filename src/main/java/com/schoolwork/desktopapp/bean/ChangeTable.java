package com.schoolwork.desktopapp.bean;

import java.util.ArrayList;
import java.util.List;
//要该表的表（update和delete）
public class ChangeTable {
    private String tableName;  //表名
    private List<Integer> rows=new ArrayList<>(); //要修改的行名
    private int index;    //位于刚开始表的第几个位置
    private List<Integer> primaryKey=new ArrayList<>();//主键
    private List<UpdateItem> updateItems=new ArrayList<>();//这个表修改值
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
