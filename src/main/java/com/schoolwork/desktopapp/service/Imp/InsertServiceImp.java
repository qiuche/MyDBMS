package com.schoolwork.desktopapp.service.Imp;

import com.alibaba.fastjson.JSONObject;
import com.schoolwork.desktopapp.entity.SQLConstant;
import com.schoolwork.desktopapp.bean.TableValue;
import com.schoolwork.desktopapp.service.InsertService;
import com.schoolwork.desktopapp.utils.Feedback;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;

import static java.lang.Thread.sleep;

@Service
public class InsertServiceImp implements InsertService {
    private static String seq = SQLConstant.getSeparate();
    private static String newline = "\r\n";

    //对没有字段名的insert语句，进行按列顺序插入字段名
    private static Map<String, List> veer(Map valuesmap, String[] names) {
        Map<String, List> tidai = new HashMap<>();
        int ind = 0;
        for (Object i : valuesmap.keySet()) {
            if (i.equals(String.valueOf(ind))) {
                ind++;
            }
        }
        if (ind == names.length) {
            for (Object key : valuesmap.keySet()) {
                tidai.put(names[Integer.valueOf(key.toString())], (List) valuesmap.get(key));
            }
            valuesmap.clear();
        }
        return tidai;
    }

    //对无输入的列进行null替代
    private static Map<String, List> completion(Map<String, List> valuesmap, String[] names, int valuelistsize) {
        List nulllist = new ArrayList();
        for (int i = 0; i < valuelistsize; i++) {
            nulllist.add("null");
        }
        for (String namekey : names) {
            if (valuesmap.get(namekey) == null) {
                valuesmap.put(namekey, nulllist);
            }
        }
        return valuesmap;
    }

    //进行字符类型（int，String）校准以及长度校准
    private static boolean decideStyle(Map<String, List> valuesmap, String[] names, String[] limitStyles) {
        List namelist = Arrays.asList(names);
        List limitStylelist = Arrays.asList(limitStyles);
        boolean flag = true;
        for (int i = 0; i < namelist.size(); i++) {
            if (valuesmap.get(namelist.get(i)).get(0).getClass().toString().contains("String")) {
                if (!(limitStylelist.get(i).toString().contains("varchar"))) {
                    flag = false;
                    break;
                } else {
                    int length = Integer.valueOf(limitStylelist.get(i).toString().substring(7));
                    for (Object o : valuesmap.get(namelist.get(i))) {
                        if (o.toString().length() > length) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            if (valuesmap.get(namelist.get(i)).get(0).getClass().toString().contains("Integer")) {
                if (!(limitStylelist.get(i).toString().contains("int"))) {
                    flag = false;
                    break;
                } else {
                    int length = Integer.valueOf(limitStylelist.get(i).toString().substring(3));
                    for (Object o : valuesmap.get(namelist.get(i))) {
                        if (o.toString().length() > length) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }

    //进行主键校准
    private static boolean decidePrimaryKey(Map<String, List> valuesmap, String[] names, String[] limits, List<TableValue> TableValuelist) {
        System.out.println(valuesmap);
        System.out.println(names);
        System.out.println(limits);
        System.out.println(TableValuelist);
        List<Integer> addprimary = new ArrayList<>();
        List<String> addstring = new ArrayList<>();
        List namelist = Arrays.asList(names);
        List limitlist = Arrays.asList(limits);
        int size=TableValuelist.get(0).getValue().size();
        boolean flag = true;
        for (int i = 0; i < limitlist.size(); i++) {
            if (limitlist.get(i).toString().contains("primary")) {
                valuesmap.get(namelist.get(i)).addAll(TableValuelist.get(i).getValue().subList(4,size));
                addprimary.add(i);
            }
        }
        for (int j = 0; j < valuesmap.get(namelist.get(addprimary.get(0))).size(); j++) {
            String addstr = "";
            for (Integer o : addprimary) {
                addstr += valuesmap.get(namelist.get(o)).get(j);
            }
            addstring.add(addstr);
        }
        boolean isRepeat = addstring.size() != new HashSet<String>(addstring).size();
        if (isRepeat) {
            flag = false;
        }
        return flag;
    }

    //写文件
    private boolean writein(Map<String, List> valuesmap, String[] names, File file, int valuelistsize) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        FileLock fileLock = null;
        boolean flag = true;
        try {
            while (true) {
                try {
                    fileLock = fileChannel.tryLock();
                    break;
                } catch (Exception e) {
                    System.out.println("有其他线程正在操作该文件，当前线程" + Thread.currentThread().getName() + "休眠1000毫秒");
                    sleep(1000);
                }

            }
            for (int i = 0; i < valuelistsize; i++) {
                String valuestr = "";
                for (String namek : names) {
                    valuestr += valuesmap.get(namek).get(i).toString() + seq;
                }
                valuestr += newline;
                randomAccessFile.seek(randomAccessFile.length());
                randomAccessFile.write(valuestr.getBytes());
            }
        } catch (Exception e) {
            flag = false;
        } finally {
            fileLock.release();
            fileChannel.close();
        }
        return flag;
    }

    @Transactional
    @Override
    public JSONObject InsertTableValue(String tablename, Map<String, List> valuesmap, HttpSession session) throws IOException {
        String nowPath = session.getAttribute("nowPath").toString();
        List<String> grantlist = (List) session.getAttribute("Power");
        if (tablename.trim().length() == 0 || StringUtils.isEmpty(tablename)) {
            return Feedback.info("表名为空", Feedback.STATUS_ERROR);
        } else {
            File file = new File(nowPath + "\\" + tablename + ".txt");
            if (file.exists()) {
                String id = SQLConstant.readAppointedLineNumber(file, 1);
                System.out.println(id);
                System.out.println(grantlist.contains(id));
                if (!grantlist.contains(id)) {
                    System.out.println(grantlist);
                    return Feedback.info("无权利操作该表", Feedback.STATUS_ERROR);
                }
                String name = SQLConstant.readAppointedLineNumber(file, 2);
                String[] names = name.split(seq);
                String limit = SQLConstant.readAppointedLineNumber(file, 3);
                String[] limits = limit.split(seq);
                String limitStyle = SQLConstant.readAppointedLineNumber(file, 4);
                String[] limitStyles = limitStyle.split(seq);
                List namelist = Arrays.asList(names);
                List<TableValue> TableValuelist = SQLConstant.getExitValue(file, names);
                try {
                    int valuelistsize = 0;
                    if (names.length == valuesmap.keySet().size()) {
                        Map<String, List> tidai = veer(valuesmap, names);
                        if (valuesmap.keySet().size() == 0) {
                            valuesmap = tidai;
                        }
                    }
                    System.out.println(valuesmap);
                    for (List value : valuesmap.values()) {
                        valuelistsize = value.size();
                        break;
                    }
                    System.out.println(valuelistsize);
                    if (namelist.containsAll(valuesmap.keySet())) {
                        valuesmap = completion(valuesmap, names, valuelistsize);
                        boolean Styleflag = decideStyle(valuesmap, names, limitStyles);
                        boolean PrimaryKeyflag = decidePrimaryKey(valuesmap, names, limits, TableValuelist);
                        if (!Styleflag) {
                            return Feedback.info("数据类型不一致", Feedback.STATUS_ERROR);
                        }
                        if (!PrimaryKeyflag) {
                            return Feedback.info("主键冲突", Feedback.STATUS_ERROR);
                        }
                        if (Styleflag && PrimaryKeyflag) {
                            if (writein(valuesmap, names, file, valuelistsize)) {
                                return Feedback.info("插入值成功", Feedback.STATUS_SUCCESS);
                            } else {
                                return Feedback.info("插入值异常", Feedback.STATUS_ERROR);
                            }
                        }
                    } else {
                        return Feedback.info("插入的字段名和表内的字段名不一致", Feedback.STATUS_ERROR);
                    }
                } catch (Exception e) {
                    return Feedback.info(e.toString(), Feedback.STATUS_ERROR);
                }
            } else {
                return Feedback.info("该表不存在", Feedback.STATUS_ERROR);
            }
        }
        return Feedback.info("插入值失败", Feedback.STATUS_ERROR);
    }
}