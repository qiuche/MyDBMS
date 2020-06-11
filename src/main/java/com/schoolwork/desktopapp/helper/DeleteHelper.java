package com.schoolwork.desktopapp.helper;

import com.schoolwork.desktopapp.bean.ChangeTable;
import com.schoolwork.desktopapp.bean.Index;
import com.schoolwork.desktopapp.bean.Table;
import com.schoolwork.desktopapp.bean.TableIndex;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteHelper {
    public static  List<ChangeTable> getNeedDelete(List<String> deleteTables, Table table){
        System.out.println("ddd"+table);
        List<ChangeTable> changeTableList =new ArrayList<>();
        for(String deleteTableName:deleteTables){
            System.out.println(deleteTableName);
            int i=0;
            ChangeTable changeTable =new ChangeTable();
            for(TableIndex tableIndex:table.getTableIndex()){
                List<Integer> rows=new ArrayList<>();
                if(tableIndex.getAlias().equals(deleteTableName)||tableIndex.getTableName().equals(deleteTableName)){
                    System.out.println("dddddddd");
                    for(Index index:table.getIndex()){
                        int rowIndex=index.getValueList().get(i);
                        if(!rows.contains(rowIndex))
                            rows.add(rowIndex);

                    }
                    changeTable =new ChangeTable(tableIndex.getTableName(),rows);
                    break;
                }
                i++;
            }
            changeTableList.add(changeTable);
        }
        for(ChangeTable changeTable : changeTableList){
            System.out.println(changeTable);
        }
        return changeTableList;
    }
    public static HashMap<String,String> deleteValue(List<ChangeTable> changeTableList) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path =request.getSession().getAttribute("nowPath").toString();
        HashMap<String,String> result=new HashMap<>();
        for(ChangeTable changeTable : changeTableList){
            File file=new File(path +"\\"+ changeTable.getTableName() + ".txt");
            if(!file.exists()){
                result.put("Error", changeTable.getTableName()+"表不存在");
                return result;
            }
            List<String> valueList=new ArrayList<>();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for(int i=0;i<4;i++) {
                String s=bufferedReader.readLine();
                valueList.add(s);
            }
            int i=0;
            int j=0;
            String s;
            while ((s=bufferedReader.readLine())!=null){
                System.out.println(i);
                if(j== changeTable.getRows().size()){
                    valueList.add(s);
                }
                for(;j< changeTable.getRows().size();){
                    int index= changeTable.getRows().get(j);
                    System.out.println(i+"    "+index);
                    if(i==index) {
                        j++;
                        break;
                    }
                    else {
                        System.out.println(s);
                        valueList.add(s);
                        break;
                    }
                }
                i++;
            }
            fileReader.close();
            bufferedReader.close();
            FileWriter fileWriter=new FileWriter(file);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            for(String row:valueList){
                bufferedWriter.write(row);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }
        result.put("Success","成功删除");
        return result;
    }

}
