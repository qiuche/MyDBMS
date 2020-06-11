package com.schoolwork.desktopapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.service.Imp.UseServiceImp;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/Grant", produces = "text/html;charset=UTF-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GrantController {
    private static String seq = SQLConstant.getSeparate();
    private static String newline = "\r\n";

    @SysLog("授权")
    @RequestMapping("/Authority")
    public String grantAuthority(@RequestBody JSONObject obj, HttpSession session) throws IOException {
        List grantlist = (List) session.getAttribute("Power");
        String data = obj.toJSONString();
        JSONObject json = JSON.parseObject(data);
        String tables = json.getString("tables");
        String users = json.getString("users");
        JSONArray tablesArray = JSONArray.parseArray(tables);
        JSONArray usersArray = JSONArray.parseArray(users);
        Map<String, List> grantMap = new HashMap<>();
        ArrayList<String> tableIdList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(tables)) {
            for (int i = 0; i < tablesArray.size(); i++) {
                String database = JSONObject.parseObject(JSONObject.toJSONString(tablesArray.get(i))).getString("database");
                String table = JSONObject.parseObject(JSONObject.toJSONString(tablesArray.get(i))).getString("table");
                List tablelist = (List) JSONArray.parseArray(table);
                grantMap.put(database, tablelist);
            }
        }
        for (String database : grantMap.keySet()) {
            for (Object tabs : grantMap.get(database)) {
                String tableid="";
                if(tabs.equals("*"))
                {
                    List<String> xinghao=UseServiceImp.getTable(SQLConstant.getPath()+"\\"+database);
                    for(String tabname:xinghao)
                    {
                        File file = new File(SQLConstant.getPath() + "\\" + database + "\\" + tabname + ".txt");
                        tableid = SQLConstant.readAppointedLineNumber(file, 1);
                        tableIdList.add(tableid);
                    }
                }
                File file = new File(SQLConstant.getPath() + "\\" + database + "\\" + tabs + ".txt");
                if (file.exists()) {
                    tableid = SQLConstant.readAppointedLineNumber(file, 1);
                    tableIdList.add(tableid);
                }
            }
        }
        if (grantlist.containsAll(tableIdList)) {
            try {
                File file = new File(SQLConstant.getGrantpath());
                String[] names = SQLConstant.readAppointedLineNumber(file, 1).split(SQLConstant.getSeparate());
                FileOutputStream fos = new FileOutputStream(file, true);
                for (String tid : tableIdList) {
                    String str = "";
                    for (String na : names) {
                        if (usersArray.contains(na)) {
                            str += tid;
                        }
                        str += seq;
                    }
                    str += newline;
                    fos.write(str.getBytes());
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Feedback.info("授权成功", Feedback.STATUS_SUCCESS).toString();
        } else {
            return Feedback.info("未拥有操纵表的权利，故无法授权", Feedback.STATUS_ERROR).toString();
        }
    }
}
