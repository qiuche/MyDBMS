package com.schoolwork.desktopapp.controller;

import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/delete", produces = "text/html;charset=UTF-8")
public class DeleteController {
    @Autowired
    private DeleteService deleteService;

    @SysLog("删除表项")
    @RequestMapping("/table")
    public String deleteTable(String deleteTable, String table, String formula) throws IOException {
        return deleteService.delete(deleteTable, table,formula).toString();
    }
}