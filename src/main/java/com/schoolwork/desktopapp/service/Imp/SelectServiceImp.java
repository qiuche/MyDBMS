package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.schoolwork.desktopapp.bean.*;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.helper.SelectHelper;
import com.schoolwork.desktopapp.service.SelectService;
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
public class SelectServiceImp implements SelectService {

    @Override
    @Transactional
    public JSONObject select(String resultColumns, String table, String formual) throws IOException {
        boolean tableExist = true;
        //用于保存不存在的table
        StringBuilder builder = new StringBuilder();
        //利用FastJson解析，获取得到的表
        List<Table> tables = JSON.parseArray(table, Table.class);
        //默认分隔符~
        String sep = SQLConstant.getSeparate();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getSession().getAttribute("nowPath").toString(); //获取路径
        List<String> grantlist = (List) request.getSession().getAttribute("Power");  //获取权限
        List<OutColumn> outColumnList = new ArrayList<>();
        for (Table item : tables) {
            File file = new File(path +"\\"+ item.getTablename() + ".txt");
            if (!file.exists()) {
                //保存不存在的表名
                builder.append(item.getTablename() + ",");
                tableExist = false;
            }
        }
        if (!tableExist)
            return Feedback.info(builder + "表不存在", "500");
        for(Table item:tables){
            File file = new File(path +"\\"+ item.getTablename() + ".txt");
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
            List<Column> columnList = new ArrayList<>();
            for (String column : columns) {
                Column column1 = new Column(column, item.getTablename() + "." + column);
                if (item.getAlias() != null) {
                    column1.setAliasColumn(item.getAlias() + "." + column);
                }
                columnList.add(column1);
            }
            item.setColumnList(columnList);
            bufferedReader.close();
        }
        //如果查询的不是*要先确认查询的属性是否符合
        if (!resultColumns.equals("[*]")) {
            outColumnList = JSON.parseArray(resultColumns, OutColumn.class);
            HashMap<String, Integer> columnIndex = new HashMap<>();
            for (OutColumn outColumn : outColumnList) {
                String key = outColumn.getSelectColumn();
                int index = 0;
                for (Table table1 : tables) {
                    for (Column column : table1.getColumnList()) {
                        if(column.getAliasColumn()==null){
                            if (key.equals(column.getColumn()) ||
                                    key.equals(column.getTableColumn())) {
                                index++;
                            }
                        }
                        else {
                            if (key.equals(column.getColumn()) ||
                                    key.equals(column.getTableColumn()) ||
                                    key.equals(column.getAliasColumn())) {
                                index++;
                            }
                        }

                    }
                }
                columnIndex.put(key, index);
            }
            StringBuilder errorColumn = new StringBuilder();
            for (String column : columnIndex.keySet()) {
                if (columnIndex.get(column) > 1)
                    errorColumn.append(column + "不能确定 ");
                else if (columnIndex.get(column) == 0)
                    errorColumn.append(column + "不存在 ");
            }
            if (errorColumn.length() > 0) {
                return Feedback.info(errorColumn.toString(), "500");
            }
        }
        //判断where子句中的属性是否存在或者冲突
        if(!StringUtil.isEmpty(formual)){
            Object object=SelectHelper.whereCheck(formual,tables);
            if(object instanceof JSONObject) return (JSONObject) object;
        }
        //读取表的数据
        for(Table item:tables){
            File file = new File(path +"\\"+ item.getTablename() + ".txt");
            String id = SQLConstant.readAppointedLineNumber(file, 1);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            String[] type = bufferedReader.readLine().split(sep);
            String[] constraint = bufferedReader.readLine().split(sep);
            //读取列数据
            String s = "";
            int i = 0;
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
            TableIndex tableIndex = new TableIndex(item.getTablename(), item.getAlias(), 0, item.getColumnList().size());
            List<TableIndex> tableIndexList = new ArrayList<>();
            tableIndexList.add(tableIndex);
            item.setTableIndex(tableIndexList);
            item.setValue(valueList);
        }

        Table startTable;
        if(tables.size()==1){
            startTable=tables.get(0);
        }
        else {
            //进行笛卡尔积
            startTable= SelectHelper.getCartesian(tables);
        }
        Stack<Table> stack = new Stack<>();


        //where子句解析
        if(!StringUtil.isEmpty(formual)) {
            Object object=SelectHelper.calculateTable(formual,startTable);
            if(object instanceof JSONObject) return (JSONObject) object;
            else if(object instanceof Stack) stack= (Stack<Table>) object;
        }

        if (resultColumns.equals("[*]")) {
            JSONObject jsonObject = new JSONObject();
            if (!stack.empty()) {
                OutPutTable outPutTable = SelectHelper.outPutAll(stack.pop(), tables.size());
                jsonObject.put("tables", outPutTable);
            } else {
                OutPutTable outPutTable = SelectHelper.outPutAll(startTable, tables.size());
                jsonObject.put("tables", outPutTable);
            }
            return Feedback.jsonObject(jsonObject, "200");
        } else {
            JSONObject jsonObject = new JSONObject();
            if (!stack.empty()) {
                OutPutTable outPutTable = SelectHelper.outPutColumn(stack.pop(), outColumnList);
                jsonObject.put("tables", outPutTable);
            } else {
                OutPutTable outPutTable = SelectHelper.outPutColumn(startTable, outColumnList);
                jsonObject.put("tables", outPutTable);
            }
            return Feedback.jsonObject(jsonObject, "200");
        }
    }
}
