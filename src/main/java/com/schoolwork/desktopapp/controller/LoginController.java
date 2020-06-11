package com.schoolwork.desktopapp.controller;

import com.schoolwork.desktopapp.Log.SysLog;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.entity.TableValue;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class LoginController {
    private String seq = SQLConstant.getSeparate();

    @RequestMapping(value = "/Login", produces = "text/html;charset=UTF-8")
    public String Login(String username, String pwd, HttpSession session) throws IOException {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(pwd)) {
            File file = new File(SQLConstant.getGrantpath());
            String name = SQLConstant.readAppointedLineNumber(file, 1);
            String[] names = name.split(seq);
            List<TableValue> GrantValueList = SQLConstant.getExitValue(file, names);
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(username)) {
                    String MD5Pwd = DigestUtils.md5Hex(pwd);
                    if (GrantValueList.get(i).getValue().get(1).equals(MD5Pwd)) {
                        int size=GrantValueList.get(i).getValue().size();
                        session.setAttribute("User", username);
                        System.out.println(size);
                        session.setAttribute("Power", GrantValueList.get(i).getValue().subList(2,size));
                        System.out.println(GrantValueList.get(i).getValue().subList(2,size));
                        return Feedback.info("登录成功", Feedback.STATUS_SUCCESS).toString();
                    }
                }
            }
        } else {
            return Feedback.info("输入为空", Feedback.STATUS_ERROR).toString();
        }
        return Feedback.info("登录失败", Feedback.STATUS_ERROR).toString();
    }

    @SysLog("注销")
    @RequestMapping(value = "/logout",produces = "text/html;charset=UTF-8")
    public String Logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
            return Feedback.info("注销成功", Feedback.STATUS_SUCCESS).toString();
        }
        return Feedback.info("不存在用户", Feedback.STATUS_ERROR).toString();
    }

}
