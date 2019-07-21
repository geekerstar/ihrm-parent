package com.geekerstar.common.poi;

import com.geekerstar.domain.poi.ExcelAttribute;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 导出Excel工具类
 * 基于模板打印的方式导出：
 */
@Getter
@Setter
public class ExcelExportUtil<T> {

    private int rowIndex;       //写入数据的起始行
    private int styleIndex;     //需要提取的样式所在的行号
    private Class clazz;        //对象的字节码
    private Field fields[];     //对象中的所有属性

    public ExcelExportUtil(Class clazz, int rowIndex, int styleIndex) {
        this.clazz = clazz;
        this.rowIndex = rowIndex;
        this.styleIndex = styleIndex;
        fields = clazz.getDeclaredFields();
    }

    /**
     * 基于注解导出
     * 参数：
     * response：
     * InputStream:模板的输入流
     * objs：数据
     * fileName：生成的文件名
     */
    public void export(HttpServletResponse response, InputStream is, List<T> objs, String fileName) throws Exception {

        //1.根据模板创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        //2.读取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //3.提取公共的样式
        CellStyle[] styles = getTemplateStyles(sheet.getRow(styleIndex));
        //4.根据数据创建每一行和每一个单元格的数据2
        AtomicInteger datasAi = new AtomicInteger(rowIndex); //数字
        for (T t : objs) {
            //datasAi.getAndIncrement()  ：获取数字，并++    i++
            Row row = sheet.createRow(datasAi.getAndIncrement());
            for (int i = 0; i < styles.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styles[i]);
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelAttribute.class)) {
                        field.setAccessible(true);
                        ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                        if (i == ea.sort()) {
                            if (field.get(t) != null) {
                                cell.setCellValue(field.get(t).toString());
                            }
                        }
                    }
                }
            }
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
    }

    public CellStyle[] getTemplateStyles(Row row) {
        CellStyle[] styles = new CellStyle[row.getLastCellNum()];
        for (int i = 0; i < row.getLastCellNum(); i++) {
            styles[i] = row.getCell(i).getCellStyle();
        }
        return styles;
    }
}
