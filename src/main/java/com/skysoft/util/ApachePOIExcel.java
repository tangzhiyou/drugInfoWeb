package com.skysoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author pinaster.tang@hotmail.com
 * @date 2015年4月12日
 */
public class ApachePOIExcel {
    public static void writeExcelToFile(List<LinkedHashMap<String, String>> valueMap) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        Sheet sheet = workbook.createSheet("国产药品");
        int count = 0;
        for (Map<String, String> value : valueMap) {
            for (Map.Entry<String, String> text : value.entrySet()) {
                Row row = sheet.createRow(count++);
                Cell cellName = row.createCell(0);
                cellName.setCellValue(text.getKey());
                Cell cellValue = row.createCell(1);
                cellValue.setCellValue(text.getValue());
            }
        }

        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("D:/33.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
