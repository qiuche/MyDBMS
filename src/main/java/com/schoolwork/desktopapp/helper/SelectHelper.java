package com.schoolwork.desktopapp.helper;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.bean.*;
import com.schoolwork.desktopapp.utils.Feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SelectHelper {
    //笛卡尔积
    public static Table getCartesian(List<Table> tableList) {
        Table table0 = new Table();
        Table tablemid = tableList.get(0);

        table0.setTablename(tablemid.getTablename());
        table0.setTableIndex(tablemid.getTableIndex());
        table0.setIndex(tablemid.getIndex());
        table0.setValue(tablemid.getValue());
        table0.setColumnList(tablemid.getColumnList());
        table0.setTableIndex(tablemid.getTableIndex());


        List<List<String>> resultValue = new ArrayList<>();
        List<Index> resultIndex = new ArrayList<>();       //结果各个行的位置
        List<Column> columnList = new ArrayList<>();       //结果属性数组
        List<TableIndex> tableIndexList = new ArrayList<>();  //结果的tableIndex
        TableIndex tableIndex0 = table0.getTableIndex().get(0); //
        tableIndexList.add(new TableIndex(tableIndex0.getTableName(), tableIndex0.getAlias(), tableIndex0.getStart(), tableIndex0.getEnd(), tableIndex0.getPrimarykey()));
        columnList.addAll(table0.getColumnList());
        int rownum = 0;
        for (int i = 1; i < tableList.size(); i++) {//从第二表开始一对多分配
            Table table1 = tableList.get(i);
            columnList.addAll(table1.getColumnList());
            TableIndex tableIndex1 = table1.getTableIndex().get(0);
            tableIndexList.add(new TableIndex(tableIndex1.getTableName(), tableIndex1.getAlias(),
                    tableIndexList.get(i - 1).getEnd(), tableIndexList.get(i - 1).getEnd() + tableIndex1.getEnd()));
            List<List<String>> tempValue = new ArrayList<>();
            List<Index> tempindex = new ArrayList<>();
            for (int j = 0; j < table0.getValue().size(); j++) {
                for (int k = 0; k < table1.getValue().size(); k++) {
                    List<String> row = new ArrayList<>();
                    row.addAll(table0.getValue().get(j));
                    row.addAll(table1.getValue().get(k));
                    tempValue.add(row);

                    Index index = new Index();
                    index.setKey(rownum);
                    List<Integer> integerList = new ArrayList<>();
                    integerList.addAll(table0.getIndex().get(j).getValueList());
                    integerList.addAll(table1.getIndex().get(k).getValueList());
                    index.setValueList(integerList);
                    tempindex.add(index);
                    rownum++;
                }
            }
            table0.setValue(tempValue);
            table0.setIndex(tempindex);
            if (i == tableList.size() - 1) {
                resultValue = tempValue;
                resultIndex = tempindex;
            }
        }
        Table table = new Table();
        table.setTableIndex(tableIndexList);
        table.setColumnList(columnList);
        table.setValue(resultValue);
        table.setIndex(resultIndex);
        return table;
    }
    //获取属性的位置
    public static int getColumnIndex(String key, Table table) {
        int index = 0;
        int flag=-1;//没有找到该属性
        int num=0;//计算查找到这个属性的数量
        int result=0;//保存在属性的第几个位置
        for (Column column : table.getColumnList()) {
            if (column.getAliasColumn() == null) {
                if (column.getColumn().equals(key) || column.getTableColumn().equals(key)) {
                    flag=1;
                    num++;
                    result=index;
                }
            } else {
                if (column.getColumn().equals(key) || column.getTableColumn().equals(key) || column.getAliasColumn().equals(key)) {
                    flag=1;
                    num++;
                    result=index;
                }
            }
            index++;
        }
        if(flag==-1) return -1;
        if(num>1) return -2;
        return result;
    }
    //去掉单引号
    public static String getSubString(String value) {
        String result = "";
        if (isString(value))
            result = value.substring(1, value.length() - 1);
        else result = value;
        return result;
    }
    //计算结果（key对key）
    public static Table getTableResult(int leftIndex, int rightIndex, Table table, String operatopr) {
        int index = 0;
        List<Index> indexList = new ArrayList<>();
        List<List<String>> valueList = new ArrayList<>();
        switch (operatopr) {
            case "=":
                for (List<String> row : table.getValue()) {
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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
                    String leftValue = row.get(leftIndex);
                    String rightValue = row.get(rightIndex);
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

        return new Table(indexList, table.getTableIndex(), table.getColumnList(), valueList);
    }
    //计算结果（key对value）
    public static Table getTableResult(int leftIndex, String rightValue, Table table, String operatopr) {
        int index = 0;
        List<Index> indexList = new ArrayList<>();
        List<List<String>> valueList = new ArrayList<>();
        switch (operatopr) {
            case "=":
                for (List<String> row : table.getValue()) {
                    String leftValue = row.get(leftIndex);
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
                    String leftValue = row.get(leftIndex);
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
                    String leftValue = row.get(leftIndex);
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
                    String leftValue = row.get(leftIndex);
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
                    String leftValue = row.get(leftIndex);
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
                    String leftValue = row.get(leftIndex);
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

        return new Table(indexList, table.getTableIndex(), table.getColumnList(), valueList);
    }
    //and 和 or 操作
    public static Table getTableCount(Table leftTable,Table rightTable,String operator){
        System.out.println(leftTable);
        System.out.println(operator);
        System.out.println(rightTable);
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
                        break;
                    }
                    else if(leftIndex.get(i).getKey() > rightIndex.get(j).getKey()){
                        resultIndex.add(rightIndex.get(j));
                        DoubleIndex doubleIndex=new DoubleIndex();
                        doubleIndex.setRightIndex(String.valueOf(j));
                        doubleIndices.add(doubleIndex);
                        j++;
                    }
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
                doubleIndex.setRightIndex(String.valueOf(j));
                doubleIndices.add(doubleIndex);
            }
        }

        List<List<String>> resultValue=new ArrayList<>();
        for(DoubleIndex doubleIndex:doubleIndices){
            if(doubleIndex.getLeftIndex()!=null){
                resultValue.add(leftTable.getValue().get(Integer.parseInt(doubleIndex.getLeftIndex())));
            }else{
                resultValue.add(rightTable.getValue().get(Integer.parseInt(doubleIndex.getRightIndex())));
            }
        }
        Table resultTable=new Table();
        resultTable.setColumnList(leftTable.getColumnList());
        resultTable.setIndex(resultIndex);
        resultTable.setValue(resultValue);
        resultTable.setTableIndex(rightTable.getTableIndex());
        System.out.println(resultTable);
        return resultTable;
    }
    //整理输出形式（全部）
    public static OutPutTable outPutAll(Table table, int num) {
        OutPutTable outPutTable = new OutPutTable();
        List<String> columns = new ArrayList<>();
        if (num == 1) {
            for (Column column : table.getColumnList()) {
                columns.add(column.getColumn());
            }
        } else {
            for (Column column : table.getColumnList()) {
                columns.add(column.getTableColumn());
            }
        }
        outPutTable.setColumns(columns);
        outPutTable.setValues(table.getValue());
        return outPutTable;
    }
    //整理输出形式（部分）
    public static OutPutTable outPutColumn(Table table, List<OutColumn> outColumnList) {
        OutPutTable outPutTable = new OutPutTable();
        List<String> columns = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (OutColumn outColumn : outColumnList) {
            int index = 0;
            for (Column column : table.getColumnList()) {
                if (outColumn.getSelectColumn().equals(column.getColumn()) ||
                        outColumn.getSelectColumn().equals(column.getTableColumn()) ||
                        outColumn.getSelectColumn().equals(column.getAliasColumn())) {
                    if (outColumn.getAlias() != null)
                        columns.add(outColumn.getAlias());
                    else columns.add(column.getTableColumn());
                    integerList.add(index);
                }
                index++;
            }
        }
        List<List<String>> resultValue = new ArrayList<>();
        for (List<String> values : table.getValue()) {
            List<String> row = new ArrayList<>();
            for (Integer integer : integerList) {
                row.add(values.get(integer));
            }
            resultValue.add(row);
        }
        outPutTable.setColumns(columns);
        outPutTable.setValues(resultValue);
        return outPutTable;
    }
    //逆波兰式运算
    public static Object calculateTable(String formual, Table startTable) {
        Stack<Table> stack = new Stack<>();
        Formual formuals = JSONObject.parseObject(formual, Formual.class);
        //查询到uid
        for (String uid : formuals.getPointStr()) {
            if (SelectHelper.isOperation(uid)) {
                Table leftTable = stack.pop();
                Table rightTable = stack.pop();
                Table midTable = SelectHelper.getTableCount(leftTable, rightTable, uid);
                stack.push(midTable);
            } else {
                Table countTable = new Table();
                for (OpValue opValue : formuals.getArrRes()) {
                    if (opValue.getUid().equals(uid)) {
                        if (SelectHelper.isKey(opValue.getKey())) {
                            int leftIndex = SelectHelper.getColumnIndex(opValue.getKey(), startTable);
                            if (leftIndex == -1)
                                return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "错误", "501");
                            else if (leftIndex == -2)
                                return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "中" + opValue.getKey() + "不能确定", "501");
                            if (SelectHelper.isKey(opValue.getValue())) {
                                int rightIndex = SelectHelper.getColumnIndex(opValue.getValue(), startTable);
                                if (rightIndex == -1)
                                    return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "错误", "501");
                                else if (rightIndex == -2)
                                    return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "中" + opValue.getValue() + "不能确定", "501");
                                countTable = SelectHelper.getTableResult(leftIndex, rightIndex, startTable, opValue.getOperator());
                                stack.push(countTable);
                            } else {
                                String rightValue = SelectHelper.getSubString(opValue.getValue());
                                countTable = SelectHelper.getTableResult(leftIndex, rightValue, startTable, opValue.getOperator());
                                stack.push(countTable);
                            }
                        } else {
                            String rightValue = SelectHelper.getSubString(opValue.getValue());
                            if (SelectHelper.isKey(opValue.getValue())) {
                                int leftIndex = SelectHelper.getColumnIndex(opValue.getValue(), startTable);
                                if (leftIndex == -1)
                                    return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "错误", "501");
                                else if(leftIndex==-2)
                                    return Feedback.info(opValue.getKey() + opValue.getOperator() + opValue.getValue() + "中"+opValue.getValue()+"不能确定", "501");
                                countTable = SelectHelper.getTableResult(leftIndex, rightValue, startTable, opValue.getOperator());
                                stack.push(countTable);
                            } else {
                            }
                        }
                    }
                }
            }
        }
        return stack;
    }

    //是不是and或者or
    public static boolean isOperation(String uid) {
        return uid.equals("and") || uid.equals("or");
    }
    //是不是键
    public static boolean isKey(String word) {
        return word.matches("^[a-zA-Z]\\w*(\\.?\\w+|\\w*)$");
    }
    //是不是数字
    public static boolean isDigit(String word) {
        return word.matches("^([-+])?\\d+(\\.\\d+)?$");
    }
    //是不是字符串
    public static boolean isString(String word) {
        return word.matches("^\'\\S*\'$");
    }

    public static boolean isConstant(String word) {
        return isDigit(word) || isString(word);
    }

}
