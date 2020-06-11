package com.schoolwork.desktopapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.service.InsertService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/Insert_into", produces = "text/html;charset=UTF-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InsertController {
    @Autowired
    private InsertService insertService;

    @SysLog("往表中插入值")
    @RequestMapping(value = "/Table")
    @ResponseBody
    public String insertTables(@RequestBody JSONObject obj, HttpSession session) throws Exception {
        if (session.getAttribute("nowPath") == null) {
            return Feedback.info("尚未选择数据库", Feedback.STATUS_ERROR).toString();
        } else {
            String data = obj.toJSONString();
            JSONObject json = JSON.parseObject(data);
            String tablename = json.getString("table");
            String values = json.getString("values");
            String limitBody = json.getString("limitBody");
            JSONArray limitBodyArray = JSONArray.parseArray(limitBody);
            int limitLength = limitBodyArray.size();
            boolean conform = true;
            StringBuffer hint = new StringBuffer("插入值为空");
            Map<String, List> valuesmap = new HashMap<String, List>();
            Map<String, List> insertmap = new HashMap<String, List>();
            if (StringUtils.isNotEmpty(values)) {
                JSONArray valuesArray = JSONArray.parseArray(values);
                for (int i = 0; i < valuesArray.size(); i++) {
                    List valuelist = (List) JSONArray.parseArray(JSONObject.toJSONString(valuesArray.get(i)));
                    valuesmap.put(Integer.toString(i), valuelist);
                }
                if (limitLength != 0) {
                    for (int i = 0; i < valuesArray.size(); i++) {
                        if (valuesmap.get(Integer.toString(i)).size() != limitLength) {
                            conform = false;
                            hint = new StringBuffer("插入的值数量与字段数量不一致");
                            break;
                        }
                    }
                }
                if (limitLength == 0) {
                    for (int i = 0; i < valuesArray.size(); i++) {
                        if (valuesmap.get(Integer.toString(i)).size() != valuesmap.get("0").size()) {
                            conform = false;
                            hint = new StringBuffer("插入的多组值列表长度不一致");
                            break;
                        }
                    }
                }
                if (conform) {
                    for (int j = 0; j < valuesmap.get("0").size(); j++) {
                        for (int i = 0; i < valuesArray.size(); i++) {
                            String type = valuesmap.get("0").get(j).getClass().toString();
                            if (!(type.equals(valuesmap.get(Integer.toString(i)).get(j).getClass().toString()))) {
                                conform = false;
                                hint = new StringBuffer("插入的多组值类型不一致");
                                break;
                            }
                        }
                    }
                }
                if (conform) {
                    for (int j = 0; j < valuesmap.get("0").size(); j++) {
                        ArrayList list = new ArrayList();
                        for (int i = 0; i < valuesArray.size(); i++) {
                            list.add(valuesmap.get(Integer.toString(i)).get(j));
                            if (limitLength == 0) {
                                insertmap.put(String.valueOf(j), list);
                            } else {
                                insertmap.put(limitBodyArray.get(j).toString(), list);
                            }
                        }
                    }
                    System.out.println(insertmap);
                    return insertService.InsertTableValue(tablename, insertmap, session).toString();
                }
            }
            return Feedback.info(hint.toString(), Feedback.STATUS_ERROR).toString();
        }
    }
}
