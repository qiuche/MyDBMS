package com.schoolwork.desktopapp.bean;

import java.util.ArrayList;
import java.util.List;

public class TableIndex {
    private String tableName;
    private String alias;
    private int start;
    private int end;
    private List<List<String>> tablevalue;
    private List<Integer> primarykey=new ArrayList<>();

    @Override
    public String toString() {
        return "TableIndex{" +
                "tableName='" + tableName + '\'' +
                ", alias='" + alias + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", tablevalue=" + tablevalue +
                ", primarykey=" + primarykey +
                '}';
    }

    public TableIndex(String tableName, String alias, int start, int end, List<Integer> primarykey) {
        this.tableName = tableName;
        this.alias = alias;
        this.start = start;
        this.end = end;
        this.primarykey = primarykey;
    }

    public List<Integer> getPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(List<Integer> primarykey) {
        this.primarykey = primarykey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public TableIndex(String tableName, String alias,int start, int end) {
        this.tableName = tableName;
        this.start = start;
        this.end = end;
        this.alias=alias;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }




    public String getTableName() {
        return tableName;
    }

    public List<List<String>> getTablevalue() {
        return tablevalue;
    }

    public void setTablevalue(List<List<String>> tablevalue) {
        this.tablevalue = tablevalue;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public TableIndex() {
    }


}
