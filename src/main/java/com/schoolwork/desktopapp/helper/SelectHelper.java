package com.schoolwork.desktopapp.helper;

import com.schoolwork.desktopapp.bean.*;

import java.util.ArrayList;
import java.util.List;

public class SelectHelper {
    public static  Table getCartesian(List<Table> tableList){
        Table table0=new Table();
        Table tablemid=tableList.get(0);

        table0.setTablename(tablemid.getTablename());
        table0.setTableIndex(tablemid.getTableIndex());
        table0.setIndex(tablemid.getIndex());
        table0.setValue(tablemid.getValue());
        table0.setColumnList(tablemid.getColumnList());
        table0.setTableIndex(tablemid.getTableIndex());


        List<List<String>> resultValue=new ArrayList<>();
        List<Index> resultIndex=new ArrayList<>();
        List<Column> columnList=new ArrayList<>();
        List<TableIndex> tableIndexList=new ArrayList<>();
        TableIndex tableIndex0=table0.getTableIndex().get(0);
        tableIndexList.add(new TableIndex(tableIndex0.getTableName(),tableIndex0.getAlias(),tableIndex0.getStart(),tableIndex0.getEnd(),tableIndex0.getPrimarykey()));
        columnList.addAll(table0.getColumnList());
        int rownum=0;
        for(int i=1;i<tableList.size();i++){
            Table table1=tableList.get(i);
            columnList.addAll(table1.getColumnList());
            TableIndex tableIndex1=table1.getTableIndex().get(0);
            tableIndexList.add(new TableIndex(tableIndex1.getTableName(),tableIndex1.getAlias(),
                    tableIndexList.get(i-1).getEnd(), tableIndexList.get(i-1).getEnd()+tableIndex1.getEnd()));
            List<List<String>> tempValue=new ArrayList<>();
            List<Index> tempindex=new ArrayList<>();
            for(int j=0;j<table0.getValue().size();j++){
                for(int k=0;k<table1.getValue().size();k++){
                    List<String> row=new ArrayList<>();
                    row.addAll(table0.getValue().get(j));
                    row.addAll(table1.getValue().get(k));
                    tempValue.add(row);

                    Index index=new Index();
                    index.setKey(rownum);
                    List<Integer> integerList=new ArrayList<>();
                    integerList.addAll(table0.getIndex().get(j).getValueList());
                    integerList.addAll(table1.getIndex().get(k).getValueList());
                    index.setValueList(integerList);
                    tempindex.add(index);
                    rownum++;
                }
            }
            table0.setValue(tempValue);
            table0.setIndex(tempindex);
            if(i==tableList.size()-1) {
                resultValue=tempValue;
                resultIndex=tempindex;
            }
        }
        Table table=new Table();
        table.setTableIndex(tableIndexList);
        table.setColumnList(columnList);
        table.setValue(resultValue);
        table.setIndex(resultIndex);
        return table;
    }
    public static int getColumnIndex(String key, Table table){
        int index=0;
        for(Column column:table.getColumnList()){
            if(column.getColumn().equals(key)||column.getTableColumn().equals(key)||column.getAliasColumn().equals(key)){
                break;
            }
            index++;
        }
        return index;
    }
    public static String getSubString(String value){
        String result = "";
        if (isString(value))
            result = value.substring(1, value.length() - 1);
        else result = value;
        return result;
    }
    public static Table getTableResult(int leftIndex,int rightIndex,Table table,String operatopr){
        int index=0;
        List<Index> indexList=new ArrayList<>();
        List<List<String>> valueList=new ArrayList<>();
        switch (operatopr) {
            case "=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) == Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.equals(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "<":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(row.get(leftIndex)) < Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) < 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case ">":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) > Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) > 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "<=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(row.get(rightIndex))) {
                        if (Double.parseDouble(leftValue) <= Double.parseDouble(row.get(rightIndex))) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) <= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case ">=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) >= Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) >= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "!=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    String rightValue=row.get(rightIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) != Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) >= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
        }

        return new Table(indexList,table.getTableIndex(),table.getColumnList(),valueList);
    }
    public static Table getTableResult(int leftIndex,String rightValue,Table table,String operatopr){
        int index=0;
        List<Index> indexList=new ArrayList<>();
        List<List<String>> valueList=new ArrayList<>();
        switch (operatopr) {
            case "=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) == Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.equals(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "<":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(row.get(leftIndex)) < Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) < 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case ">":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) > Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) > 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "<=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) <= Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) <= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case ">=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) >= Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) >= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
            case "!=":
                for (List<String> row : table.getValue()) {
                    String leftValue=row.get(leftIndex);
                    if (isDigit(leftValue) && isDigit(rightValue)) {
                        if (Double.parseDouble(leftValue) != Double.parseDouble(rightValue)) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    } else {
                        if (leftValue.compareTo(rightValue) >= 0) {
                            valueList.add(row);
                            indexList.add(table.getIndex().get(index));
                        }
                    }
                    index++;
                }
                break;
        }

        return new Table(indexList,table.getTableIndex(),table.getColumnList(),valueList);
    }
    public static Table getTableCount(Table leftTable,Table rightTable,String operator){
        List<Index> leftIndex=leftTable.getIndex();
        List<Index> rightIndex=rightTable.getIndex();
        List<DoubleIndex> doubleIndices=new ArrayList<>();
        List<Index> resultIndex=new ArrayList<>();
        int j=0;
        if(operator.equals("and")){
            int i=0;
            for (Index index : leftIndex) {
                for (; j < rightIndex.size(); j++) {
                    if (index.getKey()==rightIndex.get(j).getKey()) {
                        resultIndex.add(index);
                        DoubleIndex doubleIndex=new DoubleIndex();
                        doubleIndex.setLeftIndex(String.valueOf(i));
                        doubleIndices.add(doubleIndex);
                        break;
                    } else if (index.getKey() < rightIndex.get(j).getKey()) {
                        break;
                    }
                }
                i++;
            }
        }
        else {
            for (int i=0;i<leftIndex.size();i++) {
                while (j < rightIndex.size()) {
                   if (leftIndex.get(i).getKey() <= rightIndex.get(j).getKey()) {
                        resultIndex.add(leftIndex.get(i));
                        DoubleIndex doubleIndex=new DoubleIndex();
                        doubleIndex.setLeftIndex(String.valueOf(i));
                        doubleIndices.add(doubleIndex);
                    }
                   else if(leftIndex.get(i).getKey() > rightIndex.get(j).getKey()){
                        resultIndex.add(rightIndex.get(j));
                        DoubleIndex doubleIndex=new DoubleIndex();
                        doubleIndex.setLeftIndex(String.valueOf(j));
                        doubleIndices.add(doubleIndex);
                    }
                    j++;
                    break;
                }
                if(j==rightIndex.size()){
                    resultIndex.add(leftIndex.get(i));
                    DoubleIndex doubleIndex=new DoubleIndex();
                    doubleIndex.setLeftIndex(String.valueOf(i));
                    doubleIndices.add(doubleIndex);
                }
            }
            for(; j < rightIndex.size(); j++){
                resultIndex.add(rightIndex.get(j));
                DoubleIndex doubleIndex=new DoubleIndex();
                doubleIndex.setLeftIndex(String.valueOf(j));
                doubleIndices.add(doubleIndex);
            }
        }

        List<List<String>> resultValue=new ArrayList<>();
        for(DoubleIndex doubleIndex:doubleIndices){
            System.out.println(doubleIndex);
            if(doubleIndex.getLeftIndex()!=null){
                resultValue.add(leftTable.getValue().get(Integer.parseInt(doubleIndex.getLeftIndex())));
            }else{
                resultValue.add(leftTable.getValue().get(Integer.parseInt(doubleIndex.getRightIndex())));
            }
        }
        Table resultTable=new Table();
        resultTable.setColumnList(leftTable.getColumnList());
        resultTable.setIndex(resultIndex);
        resultTable.setValue(resultValue);
        resultTable.setTableIndex(rightTable.getTableIndex());
        return resultTable;
    }
    public static OutPutTable outPutAll(Table table,int num){
        OutPutTable outPutTable=new OutPutTable();
        List<String> columns=new ArrayList<>();
        if(num==1){
            for(Column column:table.getColumnList()){
                columns.add(column.getColumn());
            }
        }
        else {
            for(Column column:table.getColumnList()){
                columns.add(column.getTableColumn());
            }
        }
        outPutTable.setColumns(columns);
        outPutTable.setValues(table.getValue());
        return outPutTable;
    }
    public static OutPutTable outPutColumn(Table table,List<OutColumn> outColumnList){
        OutPutTable outPutTable=new OutPutTable();
        List<String> columns=new ArrayList<>();
        List<Integer> integerList=new ArrayList<>();
        for(OutColumn outColumn:outColumnList){
            int index=0;
            for(Column column:table.getColumnList()){
                if(outColumn.getSelectColumn().equals(column.getColumn())||
                        outColumn.getSelectColumn().equals(column.getTableColumn())||
                        outColumn.getSelectColumn().equals(column.getAliasColumn())){
                    if(outColumn.getAlias()!=null)
                        columns.add(outColumn.getAlias());
                    else columns.add(column.getTableColumn());
                    integerList.add(index);
                }
                index++;
            }
        }
        List<List<String>> resultValue=new ArrayList<>();
        for(List<String> values:table.getValue()){
            List<String> row=new ArrayList<>();
            for(Integer integer:integerList){
                row.add(values.get(integer));
            }
            resultValue.add(row);
        }

        outPutTable.setColumns(columns);
        outPutTable.setValues(resultValue);
        return outPutTable;
    }



    public static boolean isOperation(String uid){
        return uid.equals("and") || uid.equals("or");
    }
    public static boolean isKey(String word){
        return word.matches("^[a-zA-Z]\\w*(\\.?\\w+|\\w*)$");
    }
    public static boolean isDigit(String word) { return word.matches("^([-+])?\\d+(\\.\\d+)?$"); }
    public static boolean isString(String word){
        return word.matches("^\'\\S*\'$");
    }
    public static boolean isConstant(String word){
        return isDigit(word)||isString(word);
    }

}
