package com.schoolwork.desktopapp.entity;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ Description   :  与数据库相关的常量
 * @ Author        :  马驰
 * @ CreateDate    :  2019/12/27 10:27
 */
public class SQLConstant {
    //数据库的根路径
    private static final String path = "C:\\DBMS";

    private static final String sumpath = path + "\\" + "Sum.txt";
    //数据库的当前路径
    private static final String grantpath = path + "\\" + "Grant.txt";

    private static String nowPath = path;
    
    //自定义的分隔符
    private static final String separate = "~";

    public static String getGrantpath() {
        return grantpath;
    }

    public static String getPath() {
        File file = new File(path);
        if (!(file.exists())) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * @param : 无
     * @return : 无
     * @Description ： 返回当前路径
     * @author : 马驰
     */
    public static void setNowPath(String name) {
        nowPath = path + "\\" + name;
        System.out.println(Thread.currentThread().getName() + nowPath);
    }

    public static String getNowPath() {
        return nowPath;
    }

    /**
     * @param : name 数据库名
     * @return : 无
     * @Description ： 设置当前路径
     * @author : 马驰
     */
    public static String getSeparate() {
        return separate;
    }

    public static void setSum(int number) throws Exception {
        File file = new File(sumpath);
        if (file.exists()) {
            file.delete();
        } else if (!file.exists()) {
            file.createNewFile();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(Integer.toString(number).getBytes());
            fos.close();
        } catch (Exception e) {
        }
    }

    public static int getSum() throws Exception {
        File file = new File(sumpath);
        try {
            if (file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file)); // 建立一个输入流对象reader
                BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
                String line = "";
                int sum = 0;
                try {
                    line = br.readLine();
                    if (line != null) {
                        sum = Integer.valueOf(line.trim());
                    }
                    reader.close();
                    br.close();
                    return sum;
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<TableValue> getExitValue(File file, String[] names) throws IOException {
        FileReader fileReader = new FileReader(file);
        //读文件
        BufferedReader reader = new BufferedReader(fileReader);
        String teststr = "";
        List<TableValue> TableValuelist = new ArrayList<>();
        for (String col : names) {
            TableValue tableValue = new TableValue(col);
            TableValuelist.add(tableValue);
        }
        while ((teststr = reader.readLine()) != null) {
            int index = 0;
            String[] row = teststr.split(SQLConstant.getSeparate());
            List<String> rows = new ArrayList<>(Arrays.asList(teststr.split(SQLConstant.getSeparate())));
            for(int i=rows.size();i<names.length;i++)
            {
                rows.add("");
            }
            if (rows.size() == names.length) {
                for (TableValue tableValue : TableValuelist) {
                    tableValue.setValue((String) rows.get(index));
                    index++;
                }
            }
        }
        reader.close();
        fileReader.close();
        return TableValuelist;
    }

    public static String readAppointedLineNumber(File sourceFile, int lineNumber)
            throws IOException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = "";
        String t = "";
        if (lineNumber <= 0 || lineNumber > getTotalLines(sourceFile)) {
            System.out.println("不在文件的行数范围(1至总行数)之内。");
            System.exit(0);
        }
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
            if ((lines - lineNumber) == 0) {
                t = s;
                break;
            }
        }
        reader.close();
        in.close();
        return t;
    }

    public static int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }
}
