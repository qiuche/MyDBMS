package com.schoolwork.desktopapp.Log;

import com.schoolwork.desktopapp.entity.SQLConstant;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Log {
    public static void CteateLog() throws IOException, WriteException {
        String url = SQLConstant.getPath() + "//" + "Log.xls";
        File xlsFile = new File(url);
        // 创建一个工作簿
        WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
        // 创建一个工作表
        WritableSheet sheet = workbook.createSheet("Log", 0);
        WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
        sheet.addCell(new Label(0, 0, "用户   ", titleFormat));
        sheet.addCell(new Label(1, 0, "操作   ", titleFormat));
        sheet.addCell(new Label(2, 0, "时间   ", titleFormat));
        sheet.addCell(new Label(3, 0, "状态   ", titleFormat));
        workbook.write();
        workbook.close();
    }

    public static void WriteLog(String who, String Do, String time, String status) throws IOException {
        String url = SQLConstant.getPath() + "//" + "Log.xls";
        if (!(new File(url).exists())) {
            try {
                Log.CteateLog();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
        FileInputStream fs;
        try {
            fs = new FileInputStream(url);
            POIFSFileSystem ps = new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
            HSSFWorkbook wb = new HSSFWorkbook(ps);
            HSSFSheet sheet = wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
            HSSFRow row = sheet.getRow(0);
            int hang = 0;
            if ("".equals(row) || row == null) {
                hang = 0;
            } else {
                hang = sheet.getLastRowNum();
                hang = hang + 1;
            }
            //分别得到最后一行的行号，和一条记录的最后一个单元格
            FileOutputStream out = new FileOutputStream(url);  //向d://test.xls中写数据
            row = sheet.createRow((short) (hang)); //在现有行号后追加数据
            row.createCell(0).setCellValue(who+"    "); //设置第一个（从0开始）单元格的数据
            row.createCell(1).setCellValue(Do+"    "); //设置第二个（从0开始）单元格的数据
            row.createCell(2).setCellValue(time+"    ");
            row.createCell(3).setCellValue(status+"    ");
            sheet.autoSizeColumn(0); //调整第一列宽度
            sheet.autoSizeColumn(1); //调整第二列宽度
            sheet.autoSizeColumn(2); //调整第三列宽度
            sheet.autoSizeColumn(3); //调整第四列宽度
            out.flush();
            wb.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readLog() throws IOException, BiffException {
        String url = SQLConstant.getPath() + "//" + "Log.xls";
        ;
        File xlsFile = new File(url);
        // 获得工作簿对象
        Workbook workbook = Workbook.getWorkbook(xlsFile);
        // 获得所有工作表
        Sheet[] sheets = workbook.getSheets();
        // 遍历工作表
        if (sheets != null) {
            for (Sheet sheet : sheets) {
                // 获得行数
                int rows = sheet.getRows();
                // 获得列数
                int cols = sheet.getColumns();
                // 读取数据
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        System.out.printf("%10s", sheet.getCell(col, row)
                                .getContents());
                    }
                    System.out.println();
                }
            }
        }
        workbook.close();
    }
}



