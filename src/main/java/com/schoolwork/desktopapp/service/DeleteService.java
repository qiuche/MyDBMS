package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface DeleteService {
    public JSONObject delete(String deleteTable,String table, String formual) throws IOException;
}
