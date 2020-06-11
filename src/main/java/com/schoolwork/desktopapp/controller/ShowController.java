package com.schoolwork.desktopapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.service.Imp.UseServiceImp;
import com.schoolwork.desktopapp.utils.Feedback;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/Show", produces = "text/html;charset=UTF-8")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ShowController {
    @SysLog("查找所有数据库")
    @RequestMapping("/Database")
    @ResponseBody
    public String showDatabase()
    {
        JSONObject obj=new JSONObject();
        obj.put("AllDatabase",UseServiceImp.getDatabase(SQLConstant.getPath()));
        return Feedback.jsonObject(obj,Feedback.STATUS_SUCCESS).toString();
    }

    @SysLog("查找所有表")
    @RequestMapping("/Table")
    @ResponseBody
    public String showTable(HttpSession session)
    {
        if (session.getAttribute("nowPath")==null) {
            return Feedback.info("尚未选择数据库", Feedback.STATUS_ERROR).toString();
        }
        JSONObject obj=new JSONObject();
        obj.put("AllTable",UseServiceImp.getTable(session.getAttribute("nowPath").toString()));
        return Feedback.jsonObject(obj,Feedback.STATUS_SUCCESS).toString();
    }
}
