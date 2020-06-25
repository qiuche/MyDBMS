package com.schoolwork.desktopapp.bean;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tablename;    //表名
    private String alias;        //表的别名
    private List<Index> index=new ArrayList<>();   //表数据索引
    private List<TableIndex> tableIndex;           //表内分表的索引
    private List<Column> columnList;      //属性及其类别约束
    private List<List<String>> value;     //每一行的值

    public List<List<String>> getValue() {
        return value;
    }

    public void setValue(List<List<String>> value) {
        this.value = value;
    }

    public List<TableIndex> getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(List<TableIndex> tableIndex) {
        this.tableIndex = tableIndex;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }


    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<Index> getIndex() {
        return index;
    }

    public void setIndex(List<Index> index) {
        this.index = index;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public Table(String tablename, String alias) {
        this.tablename = tablename;
        this.alias = alias;
    }

    public Table(String tablename, String alias, List<Index> index) {
        this.tablename = tablename;
        this.alias = alias;
        this.index = index;
    }

    public Table(List<Index> index, List<TableIndex> tableIndex, List<Column> columnList, List<List<String>> value) {
        this.index = index;
        this.tableIndex = tableIndex;
        this.columnList = columnList;
        this.value = value;
    }

    public Table(List<Index> index, List<Column> columnList, List<List<String>> value) {
        this.index = index;
        this.columnList = columnList;
        this.value = value;
    }

    public Table() {
    }

    @Override
    public String toString() {
        return "Table{" +
                ", index=" + index +
                "column="+columnList+
                ", tableIndex=" + tableIndex +
                ", value=" + value +
                '}';
    }

}
