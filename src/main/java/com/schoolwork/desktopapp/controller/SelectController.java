package com.schoolwork.desktopapp.controller;

import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.service.SelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/select",produces = "text/html;charset=UTF-8")
public class SelectController {
    @Autowired
    private SelectService selectService;

    @SysLog("查询表")
    @RequestMapping("/table")
    public String selectTable(String resultColumns, String table, String formula) throws IOException {
        return selectService.select(resultColumns,table,formula).toString();
    }
}
