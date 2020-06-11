package com.schoolwork.desktopapp.controller;

import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/update", produces = "text/html;charset=UTF-8")
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @SysLog("更新表")
    @RequestMapping("/table")
    public String updateTable(String updateItem, String table, String formual) throws IOException {
        return updateService.update(updateItem,table,formual).toString();
    }
}
