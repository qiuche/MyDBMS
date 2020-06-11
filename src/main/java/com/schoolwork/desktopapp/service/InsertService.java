package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InsertService {
    public JSONObject InsertTableValue(String tablename, Map<String, List> valuesmap, HttpSession session) throws IOException;
}
