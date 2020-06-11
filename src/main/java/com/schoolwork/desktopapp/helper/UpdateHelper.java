package com.schoolwork.desktopapp.helper;

import com.schoolwork.desktopapp.bean.*;
import com.schoolwork.desktopapp.entity.SQLConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateHelper {
    public static boolean typeCheck(String value, String type,String constraint){
        if(isIntType(type)){
            if(value==null){
                return isNullConstraint(constraint);
            }
            else return isIntValue(value);
        }
        else if(isDoubleType(type)){
            if(value==null){
                return isNullConstraint(constraint);
            }
            return isDigit(value);
        }
        else if(isVarcharType(type)){
            if(value==null){
                return isNullConstraint(constraint);
            }
            return true;
        }
        return false;
    }
    public static List<ChangeTable> getUpdateRow(List<UpdateItem> updateItemList, Table table){
        List<ChangeTable> changeTableList=new ArrayList<>();
        for(UpdateItem updateItem:updateItemList){
            System.out.println(updateItem);
            boolean flag=true;
            for(ChangeTable item:changeTableList){
                if(updateItem.getTableName().equals(item.getTableName())){
//                    if(isPrimaryKeyConstraint(updateItem.getConstraint())){
//                        List<Integer> integerList=item.getPrimaryKey();
//                        integerList.add(updateItem.getIndex());
//                        item.setPrimaryKey(integerList);
//                    }

                    List<UpdateItem> updateItems=item.getUpdateItems();
                    updateItems.add(updateItem);
                    item.setUpdateItems(updateItems);
                    flag=false;
                    break;
                }
            }
            if(flag){
                ChangeTable changeTable=new ChangeTable();
                changeTable.setTableName(updateItem.getTableName());
                List<UpdateItem> updateItems=changeTable.getUpdateItems();
                updateItems.add(updateItem);
                changeTable.setUpdateItems(updateItems);
                changeTableList.add(changeTable);
            }
        }
        for(ChangeTable changeTable:changeTableList){
            int index=0;
//            for(Column column:table.getColumnList()){
//                if(changeTable.getTableName().equals(column.getTableColumn().split("\\.")[0])){
//                    changeTable.setIndex(index);
//                }
//                if(isPrimaryKeyConstraint(column.getConstraint())){
//                    List<Integer> integerList = changeTable.getPrimaryKey();
//                integerList.add(index);
//                changeTable.setPrimaryKey(integerList);
//            }
//            index++;
//            }
            for(TableIndex tableIndex:table.getTableIndex()){
                if(changeTable.getTableName().equals(tableIndex.getTableName())){
                    changeTable.setIndex(index);
                    changeTable.setPrimaryKey(tableIndex.getPrimarykey());
                }
                index++;
            }
        }
        for(Index index:table.getIndex()){
            for(ChangeTable changeTable:changeTableList){
                List<Integer> rows=changeTable.getRows();
                int row=index.getValueList().get(changeTable.getIndex());
                if(rows.contains(row))
                    continue;
                rows.add(row);
                changeTable.setRows(rows);
            }
        }
        return changeTableList;
    }
    public static HashMap<String,String> updateTable(List<Table> tables, List<ChangeTable> changeTables) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path =request.getSession().getAttribute("nowPath").toString();
        String seq=SQLConstant.getSeparate();
        HashMap<String,String> result=new HashMap<>();
        HashMap<String,List<String>> updateValueList=new HashMap<>();
        int changeRowNum=0;
        for(ChangeTable changeTable:changeTables){
            changeRowNum+=changeTable.getRows().size();
            File file=new File(path +"\\"+ changeTable.getTableName() + ".txt");
            if(!file.exists()){
                result.put("Error", changeTable.getTableName()+"表不存在");
                return result;
            }
            List<String> valueList=getFourRow(file);
            Table table=tables.get(changeTable.getIndex());
            List<Integer> primaryKey=new ArrayList<>();
            for(UpdateItem updateItem:changeTable.getUpdateItems()){//计算要修改的有几个主键
                if(isPrimaryKeyConstraint(updateItem.getConstraint())){
                    primaryKey.add(updateItem.getIndex());
                }
            }
            if(changeTable.getPrimaryKey().size()>0){
                if(primaryKey.size()>0){
                    if(changeTable.getPrimaryKey().size()==primaryKey.size()&&changeTable.getRows().size()>1){
                        System.out.println("pri1");
                        //修改的主键与表的主键数量相同相当于只能改一个主键，如果多了主键则不能修改
                        result.put("Error", "存在主键错误影响0行");
                        return result;
                    }
                }

            }
            List<String> keyList=new ArrayList<>();

            for(List<String> value:table.getValue()){
                StringBuilder keyBulilder=new StringBuilder();
                StringBuilder valueBuilder=new StringBuilder();
                for(int keyIndex:changeTable.getPrimaryKey()){
                    if(primaryKey.contains(keyIndex)){
                        String string = null;
                        for(UpdateItem updateItem:changeTable.getUpdateItems()){
                            if(updateItem.getIndex()==keyIndex){
                                string=updateItem.getValue();
                            }
                        }
                        keyBulilder.append(string+seq);
                    }
                    else {
                        keyBulilder.append(value.get(keyIndex)+seq);
                    }
                }
                if(keyList.contains(keyBulilder.toString())&&!keyBulilder.toString().equals("")){
                    System.out.println("pri2");
                    result.put("Error", "存在主键错误影响0行");
                    return result;
                }
                if(!keyBulilder.toString().equals("")) keyList.add(keyBulilder.toString());
                for(int index=0;index<value.size();index++){
                    boolean flag=true;
                    String string = null;
                    for(UpdateItem updateItem:changeTable.getUpdateItems()){
                        if(updateItem.getIndex()==index){
                            string=updateItem.getValue();
                            flag=false;
                            break;
                        }
                    }
                    if(flag) string=value.get(index);
                    valueBuilder.append(string+seq);
                }
                valueList.add(valueBuilder.toString());
            }
            updateValueList.put(path + "\\"+changeTable.getTableName() + ".txt",valueList);
        }
        for(String tableUrl:updateValueList.keySet()){
            File file=new File(tableUrl);
            FileWriter fileWriter=new FileWriter(file);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            for(String row:updateValueList.get(tableUrl)){
                bufferedWriter.write(row);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }
        result.put("SUCCESS","影响了"+changeRowNum+"行");
        return result;
    }
    public static List<String> getFourRow(File file) throws IOException {
        List<String> valueList=new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        for(int i=0;i<4;i++) {
            String s=bufferedReader.readLine();
            valueList.add(s);
        }
        return valueList;
    }

    public static boolean isVarcharType(String word){
        return word.matches("^varchar\\d+$");
    }
    public static boolean isIntType(String word){return  word.matches("^int$");}
    public static boolean isDoubleType(String word){return  word.matches("^double$");}
    public static boolean isDigit(String word) { return word.matches("^([-+])?\\d+(\\.\\d+)?$"); }
    public static boolean isIntValue(String word){return word.matches("^([-+])?\\d+$");}

    public static boolean isNullConstraint(String word){
        return !word.contains("primary key") && !word.contains("not null");
    }
    public static boolean isPrimaryKeyConstraint(String word){
        return word.contains("primary key");
    }
}
