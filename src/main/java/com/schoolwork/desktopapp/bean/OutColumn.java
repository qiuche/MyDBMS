package com.schoolwork.desktopapp.bean;

public class OutColumn {
    private String selectColumn;
    private String alias;

    public String getSelectColumn() {
        return selectColumn;
    }

    public void setSelectColumn(String selectColumn) {
        this.selectColumn = selectColumn;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "OutColumn{" +
                "selectColumn='" + selectColumn + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
