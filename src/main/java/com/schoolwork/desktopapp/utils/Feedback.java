package com.schoolwork.desktopapp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



public class Feedback {

    public static final String STATUS_SUCCESS           = "200"; // 成功
    public static final String STATUS_PERMISSION_DENIED = "401"; // 未经授权
    public static final String STATUS_PAGE_NOT_FOUND    = "404"; // 请求不存在
    public static final String STATUS_UNKNOWN_ERROR     = "500"; // 未预见的错误
    public static final String STATUS_ERROR             = "501"; // 可控的错误
    public static final String STATUS_NOT_LOGGED_IN     = "502"; // 未登录

    private static String getInfoFromStatus(String status) {
        String info;
        switch (status) {

            case STATUS_SUCCESS:            info = "操作成功";   break;
            case STATUS_PERMISSION_DENIED:  info = "权限不足";   break;
            case STATUS_PAGE_NOT_FOUND:     info = "找不到页面"; break;
            case STATUS_UNKNOWN_ERROR:      info = "未知错误";   break;
            case STATUS_ERROR:              info = "操作失败";   break;
            case STATUS_NOT_LOGGED_IN:      info = "未登录";     break;
            default: info = "系统繁忙";
        }
        return info;
    }

    public static JSONObject status(String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", getInfoFromStatus(status));
        jsonObject.put("data", "");
        jsonObject.put("status", status);
        return jsonObject;
}

    public static JSONObject info(String info, String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", info);
        jsonObject.put("data", "");
        jsonObject.put("status", status);
        return jsonObject;
    }

    public static JSONObject object(Object object, String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", "");
        jsonObject.put("data", JSON.toJSON(object));
        jsonObject.put("status", status);
        return jsonObject;
    }

    public static JSONObject jsonArray(JSONArray jsonArray, String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", "");
        jsonObject.put("data", jsonArray);
        jsonObject.put("status", status);
        return jsonObject;
    }

    public static JSONObject jsonObject(JSONObject feedbackJsonObject, String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", "");
        jsonObject.put("data", feedbackJsonObject);
        jsonObject.put("status", status);
        return jsonObject;
    }

}
