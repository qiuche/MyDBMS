package com.schoolwork.desktopapp.controller;

import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.service.UseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/Use",produces = "text/html;charset=UTF-8" )
@CrossOrigin(origins = "*",maxAge = 3600)
public class UseController {
    @Autowired
    private UseService useService;

    @SysLog("选择数据库")
    @RequestMapping(value = "/Database")
    public String chooseDatabase(String database, HttpSession session) throws Exception {
        return useService.choose(database,session).toString();
    }
}
