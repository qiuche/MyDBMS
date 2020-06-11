package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface SelectService {
    public JSONObject select(String resultColumns,String table,String formual) throws IOException;


}
