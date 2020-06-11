package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.entity.tableSet;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface CreateService {
    public JSONObject createDatabase(String databasename);
    public JSONObject createTable(String tablename, List<tableSet> tableSets, HttpSession session) throws IOException;
}
