package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.service.CreateService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@Transactional
public class CreateServiceImp implements CreateService {
    private static final String localpath = "E:\\DBMS";

    @Transactional
    @Override
    public JSONObject createDatabase(String databasename) {
        if (databasename.length() == 0) {
            return Feedback.info("数据库名为空", Feedback.STATUS_ERROR);
        } else {
            File file = new File(localpath + "\\" + databasename);
            if (!(file.exists())) {
                file.mkdirs();
                return Feedback.info("创建数据库成功", Feedback.STATUS_SUCCESS);
            } else {
                return Feedback.info("数据库已存在", Feedback.STATUS_ERROR);
            }
        }
    }
}
