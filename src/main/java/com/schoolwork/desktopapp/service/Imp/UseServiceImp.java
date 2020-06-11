package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.service.UseService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UseServiceImp implements UseService {
    SQLConstant sqlConstant;
    @Transactional
    @Override
    public JSONObject choose(String database, HttpSession session) throws Exception {
        if (database.length() == 0) {
            return Feedback.info("数据库名为空", Feedback.STATUS_ERROR);
        } else {
            if (getDatabase(SQLConstant.getPath()).contains(database)) {
                SQLConstant.setNowPath(database);
                session.setAttribute("nowPath",SQLConstant.getNowPath());
                return Feedback.info("已切换数据库", Feedback.STATUS_SUCCESS);
            } else {
                return Feedback.info("该数据库不存在", Feedback.STATUS_ERROR);
            }
        }

    }

    public static List<String> getDatabase(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                files.add(tempList[i].getName().toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
        }
        return files;
    }

    public static List<String> getTable(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].getName().toString().replace(".txt",""));
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
        }
        return files;
    }
}
