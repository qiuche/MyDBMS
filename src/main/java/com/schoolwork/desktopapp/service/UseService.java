package com.schoolwork.desktopapp.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;

public interface UseService {
    public JSONObject choose(String database, HttpSession session) throws Exception;
}
