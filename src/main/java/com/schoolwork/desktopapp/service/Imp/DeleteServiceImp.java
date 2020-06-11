package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.schoolwork.desktopapp.bean.*;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.helper.DeleteHelper;
import com.schoolwork.desktopapp.helper.SelectHelper;
import com.schoolwork.desktopapp.service.DeleteService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class DeleteServiceImp implements DeleteService {
    @Override
    @Transactional
    public JSONObject delete(String deleteTable,String table, String formual) throws IOException {
        boolean tableExist = true;

        StringBuilder builder = new StringBuilder();
        List<Table> tables = JSON.parseArray(table, Table.class);
        String sep = SQLConstant.getSeparate();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path =request.getSession().getAttribute("nowPath").toString();
        List grantlist = (List) request.getSession().getAttribute("Power");
        List<OutColumn> outColumnList = new ArrayList<>();
        for (Table item : tables) {
//                File file = new File(path+item.getTablename()+".txt");
            File file = new File(path +"\\"+ item.getTablename() + ".txt");

            if (!file.exists()) {
                builder.append(item.getTablename() + ",");
                tableExist = false;
            }
            if (tableExist) {
                String id = SQLConstant.readAppointedLineNumber(file, 1);
                if (!(grantlist.contains(id))) {
                    return Feedback.info("无权利操作该表", Feedback.STATUS_ERROR);
                }
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                bufferedReader.readLine();

                //读取属性
                String strings = bufferedReader.readLine();
                String[] columns = strings.split(sep);
//                List<TableValue> tableValueList=new ArrayList<>();
                List<Column> columnList = new ArrayList<>();
                for (String column : columns) {
                    Column column1 = new Column(column, item.getTablename() + "." + column);
                    if (item.getAlias() != null) {
                        column1.setAliasColumn(item.getAlias() + "." + column);
                    }
                    columnList.add(column1);
                }
                item.setColumnList(columnList);

                List<String> type = Arrays.asList(bufferedReader.readLine().split(sep));
                List<String> constraint = Arrays.asList(bufferedReader.readLine().split(sep));
                int i = 0;
                for(Column column:item.getColumnList()){
                    column.setType(type.get(i));
                    column.setConstraint(constraint.get(i));
                    i++;
                }
                //读取列数据

                String s = "";
                i=0;
                List<List<String>> valueList = new ArrayList<>();
                List<Index> indexList = new ArrayList<>();
                while ((s = bufferedReader.readLine()) != null) {
                    if(s.equals("")) break;
                    List<String> rows = new ArrayList<>(Arrays.asList(s.split(sep)));
                    for (int num = rows.size(); num < item.getColumnList().size(); num++) {
                        rows.add("");
                    }
                    Index index = new Index(i, i);
                    indexList.add(index);
                    //保存索引
                    i++;
                    valueList.add(rows);
                }
                item.setIndex(indexList);
                TableIndex tableIndex = new TableIndex(item.getTablename(),item.getAlias(), 0, item.getColumnList().size());
                List<TableIndex> tableIndexList = new ArrayList<>();
                tableIndexList.add(tableIndex);
                item.setTableIndex(tableIndexList);
                item.setValue(valueList);
                bufferedReader.close();
                System.out.println(item);
            }
        }
        if(!tableExist)
            return Feedback.info(builder+"表不存在","500");
        Table startTable;
        if(tables.size()==1){
            startTable=tables.get(0);
        }
        else {
            startTable= SelectHelper.getCartesian(tables);
        }
        Stack<Table> stack = new Stack<>();

        if(StringUtil.isNotEmpty(formual)) {
            Formual formuals = JSONObject.parseObject(formual, Formual.class);
            //查询到uid
            for (String uid : formuals.getPointStr()) {
                if (SelectHelper.isOperation(uid)) {
                    Table leftTable = stack.pop();
                    Table rightTable = stack.pop();
                    Table midTable=SelectHelper.getTableCount(leftTable,rightTable,uid);
                    stack.push(midTable);
                }
                else {
                    Table countTable=new Table();
                    for(OpValue opValue:formuals.getArrRes()){
                        if(opValue.getUid().equals(uid)){
                            if(SelectHelper.isKey(opValue.getKey())){
                                if(SelectHelper.isKey(opValue.getKey())){
                                    int leftIndex=SelectHelper.getColumnIndex(opValue.getKey(),startTable);
                                    if(SelectHelper.isKey(opValue.getValue())){
                                        int rightIndex=SelectHelper.getColumnIndex(opValue.getValue(),startTable);
                                        countTable=SelectHelper.getTableResult(leftIndex,rightIndex,startTable,opValue.getOperator());
                                        stack.push(countTable);
//                                        JSONObject jsonObject = new JSONObject();
//                                        jsonObject.put("tables", countTable);
//                                        return Feedback.jsonObject(jsonObject, "200");
                                    }
                                    else {
                                        String rightValue=SelectHelper.getSubString(opValue.getValue());
                                        countTable=SelectHelper.getTableResult(leftIndex,rightValue,startTable,opValue.getOperator());
                                        stack.push(countTable);
//                                        JSONObject jsonObject = new JSONObject();
//                                        jsonObject.put("tables", countTable);
//                                        return Feedback.jsonObject(jsonObject, "200");
                                    }
                                }
                            }
                            else {
                                String rightValue=SelectHelper.getSubString(opValue.getValue());
                                if(SelectHelper.isKey(opValue.getValue())){
                                    int leftIndex=SelectHelper.getColumnIndex(opValue.getValue(),startTable);
                                    countTable=SelectHelper.getTableResult(leftIndex,rightValue,startTable,opValue.getOperator());
                                    stack.push(countTable);
//                                        JSONObject jsonObject = new JSONObject();
//                                        jsonObject.put("tables", countTable);
//                                        return Feedback.jsonObject(jsonObject, "200");
                                }
                                else {
                                }
                            }
                        }
                    }
                }
            }
        }
        List<String> needTableList=JSON.parseArray(deleteTable,String.class);
        JSONObject jsonObject = new JSONObject();
        List<ChangeTable> deleteTableList=new ArrayList<>();
        if(!stack.empty()){
            deleteTableList=DeleteHelper.getNeedDelete(needTableList,stack.pop());
        }
        else{
            deleteTableList=DeleteHelper.getNeedDelete(needTableList,startTable);
        }
        ;
        jsonObject.put("deleteResult", DeleteHelper.deleteValue(deleteTableList));
        return Feedback.jsonObject(jsonObject,"500");
    }
}
