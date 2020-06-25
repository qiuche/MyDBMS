package com.schoolwork.desktopapp.controller;

import com.github.pagehelper.util.StringUtil;
import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.utils.Feedback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/Drop", produces = "text/html;charset=UTF-8")
public class DropController {
    @SysLog("删除数据库")
    @RequestMapping("/Database")
    public String DropDatabase(String Databasename) {
        if (StringUtil.isNotEmpty(Databasename)) {
            File file = new File(SQLConstant.getPath() + "\\" + Databasename);
            if (file.exists()) {
                try {
                    file.delete();
                    return Feedback.info("删除数据库成功", Feedback.STATUS_SUCCESS).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return Feedback.info("数据库不存在", Feedback.STATUS_ERROR).toString();
        }
        return Feedback.info("数据库名为空", Feedback.STATUS_ERROR).toString();
    }

    @SysLog("删除表")
    @RequestMapping("/Table")
    public String DropTable(String Tablename, HttpSession session) throws IOException {
        if (StringUtil.isNotEmpty(Tablename)) {
            if (session.getAttribute("nowPath") == null) {
                return Feedback.info("尚未选择数据库", Feedback.STATUS_ERROR).toString();
            } else {
                File file = new File(session.getAttribute("nowPath") + "\\" + Tablename + ".txt");
                if (file.exists()) {
                    String id=SQLConstant.readAppointedLineNumber(file,1);
                    List<String> grantlist = (List) session.getAttribute("Power");
                    if(!grantlist.contains(id))
                    {
                        return Feedback.info("无权利删除该表", Feedback.STATUS_ERROR).toString();
                    }
                    try {
                        file.delete();
                        return Feedback.info("删除表成功", Feedback.STATUS_SUCCESS).toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return Feedback.info("表不存在", Feedback.STATUS_ERROR).toString();
            }
        }
        return Feedback.info("表名为空", Feedback.STATUS_ERROR).toString();
    }
}
