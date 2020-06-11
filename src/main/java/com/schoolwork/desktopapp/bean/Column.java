package com.schoolwork.desktopapp.bean;

public class Column {
    private String column;
    private String tableColumn;
    private String aliasColumn;
    private String type;
    private String constraint;
    public Column(String column, String tableColumn, String aliasColumn) {
        this.column = column;
        this.tableColumn = tableColumn;
        this.aliasColumn = aliasColumn;
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

    public Column() {
    }

    public Column(String column, String tableColumn) {
        this.column = column;
        this.tableColumn = tableColumn;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getAliasColumn() {
        return aliasColumn;
    }

    public void setAliasColumn(String aliasColumn) {
        this.aliasColumn = aliasColumn;
    }

    @Override
    public String toString() {
        return "Column{" +
                "column='" + column + '\'' +
                ", tableColumn='" + tableColumn + '\'' +
                ", aliasColumn='" + aliasColumn + '\'' +
                '}';
    }
}
