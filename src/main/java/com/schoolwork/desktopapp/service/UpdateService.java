package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface UpdateService {
    public JSONObject update(String updateItem, String table, String formual) throws IOException;
}
