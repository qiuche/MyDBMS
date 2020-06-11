package com.schoolwork.desktopapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.entity.tableSet;
import com.schoolwork.desktopapp.service.CreateService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/Create", produces = "text/html;charset=UTF-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CreateController {
    @Autowired
    private CreateService createService;

    @SysLog("创建数据库")
    @RequestMapping("/Databases")
    public String createDatabase(String databasename) {
        return createService.createDatabase(databasename).toString();
    }


    @SysLog("创建表")
    @RequestMapping("/Tables")
    @ResponseBody
    public String createTables(@RequestBody JSONObject obj, HttpSession session) throws Exception {
        if (session.getAttribute("nowPath") == null) {
            return Feedback.info("尚未选择数据库", Feedback.STATUS_ERROR).toString();
        } else {
            String data = obj.toJSONString();
            JSONObject json = JSON.parseObject(data);
            String tablename = json.getString("table");
            String rowParameter = json.getString("rowParameter");
            List<tableSet> tableSets = new ArrayList<tableSet>();
            if (StringUtils.isNotEmpty(rowParameter)) {
                JSONArray rowParameterArray = JSONArray.parseArray(rowParameter);
                for (int i = 0; i < rowParameterArray.size(); i++) {
                    int limitNum = JSONObject.parseObject(JSONObject.toJSONString(rowParameterArray.get(i))).getInteger("limitNum");
                    //字段长度
                    String arrLimit = JSONObject.parseObject(JSONObject.toJSONString(rowParameterArray.get(i))).getString("arrLimit");
                    //字段限制
                    String key = JSONObject.parseObject(JSONObject.toJSONString(rowParameterArray.get(i))).getString("key");
                    //字段名
                    String limitStyle = JSONObject.parseObject(JSONObject.toJSONString(rowParameterArray.get(i))).getString("limitStyle");
                    //字段类型
                    tableSet tt = new tableSet(limitNum, arrLimit, key, limitStyle);
                    tableSets.add(tt);
                }
            }
            return createService.createTable(tablename, tableSets, session).toString();
        }
    }
}
