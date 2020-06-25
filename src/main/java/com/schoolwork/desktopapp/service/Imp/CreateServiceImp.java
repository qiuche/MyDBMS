package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.bean.tableSet;
import com.schoolwork.desktopapp.service.CreateService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class CreateServiceImp implements CreateService {
    private static String seq = SQLConstant.getSeparate();
    private static String newline = "\r\n";

    @Transactional
    @Override
    public JSONObject createDatabase(String databasename) {
        if (databasename.trim().length() == 0 || StringUtils.isEmpty(databasename)) {
            return Feedback.info("数据库名为空", Feedback.STATUS_ERROR);
        } else {
            File file = new File(SQLConstant.getPath() + "\\" + databasename);
            if (!(file.exists())) {
                file.mkdirs();
                return Feedback.info("创建数据库成功", Feedback.STATUS_SUCCESS);
            } else {
                return Feedback.info("数据库已存在", Feedback.STATUS_ERROR);
            }
        }
    }

    @Transactional
    @Override
    public JSONObject createTable(String tablename, List<tableSet> tableSets, HttpSession session) throws IOException {
        String nowPath=session.getAttribute("nowPath").toString();
        if (tablename.trim().length() == 0 || StringUtils.isEmpty(tablename)) {
            return Feedback.info("表名为空", Feedback.STATUS_ERROR);
        } else {
            File file = new File(nowPath + "\\" + tablename + ".txt");
            if (!(file.exists())) {
                try {
                    file.createNewFile();
                    int id = SQLConstant.getSum();
                    InsertGrant(id);
                    String str = "";
                    String str1 = "";
                    String str2 = "";
                    FileOutputStream fos = new FileOutputStream(file, true);
                    fos.write(Integer.toString(id).getBytes());
                    fos.write(newline.getBytes());
                    for (int i = 0; i < tableSets.size(); i++) {
                        str += tableSets.get(i).getKey() + seq;
                        if("".equals(tableSets.get(i).getArrLimit())) {
                            str1 += "null" + seq;
                        }
                        else
                        {
                            str1 += tableSets.get(i).getArrLimit() + seq;
                        }
                        str2 += tableSets.get(i).getLimitStyle() + tableSets.get(i).getLimitNum() + seq;
                    }
                    str += newline;
                    str1 += newline;
                    str2 += newline;
                    fos.write(str.getBytes());
                    fos.write(str1.getBytes());
                    fos.write(str2.getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return Feedback.info("创建表失败", Feedback.STATUS_ERROR);
                }
                return Feedback.info("创建表成功", Feedback.STATUS_SUCCESS);
            } else {
                return Feedback.info("该表已存在", Feedback.STATUS_ERROR);
            }
        }
    }

    //创建表的人拥有表的全部权限
    private void InsertGrant(int id) throws IOException {
        File file = new File(SQLConstant.getGrantpath());
        FileOutputStream fos = new FileOutputStream(file, true);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String user = request.getSession().getAttribute("User").toString();
        String name = SQLConstant.readAppointedLineNumber(file, 1);
        String[] names = name.split(seq);
        String str = "";
        for (int i = 0; i < names.length; i++) {
            if (user.equals(names[i])) {
                str += id;
            }
            str += seq;
        }
        str += newline;
        fos.write(str.getBytes());
        fos.flush();
        fos.close();
        HttpServletRequest reques = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        List<String> grantlist = (List) reques.getSession().getAttribute("Power");
        grantlist.add(String.valueOf(id));
        System.out.println("新的权限表");
        System.out.println(grantlist);
        reques.getSession().setAttribute("Power",grantlist);
    }
}
