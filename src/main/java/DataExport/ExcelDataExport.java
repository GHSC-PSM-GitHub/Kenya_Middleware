/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataExport;

import com.odkdhis.model.dashboard.FacilitiesWithMatchingData;
import java.util.List;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import static org.apache.poi.ss.util.CellUtil.createCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Paul Omboi
 */
public class ExcelDataExport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<FacilitiesWithMatchingData> listoffacilitieswithmatchingdata;
    
    public ExcelDataExport(List<FacilitiesWithMatchingData> listoffacilitieswithmatchingdata){
       this.listoffacilitieswithmatchingdata=listoffacilitieswithmatchingdata;
       workbook=new XSSFWorkbook();
       
    }
    private void writeHeaderLine(){
        CellStyle style = workbook.createCellStyle();
        
        Row row = sheet.createRow(0);
        
        XSSFFont font = workbook.createFont();
        
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "User", style);      
        createCell(row, 1, "Eproduct_name", style);       
        createCell(row, 2, "allfacilities Name", style);    
        createCell(row, 3, "matching", style);
        createCell(row, 4, "notmatching", style);
        

    }
    
     private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (FacilitiesWithMatchingData dataexport : listoffacilitieswithmatchingdata) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, dataexport.getCounty(), style);
            createCell(row, columnCount++, dataexport.getProduct_name(), style);
            createCell(row, columnCount++, dataexport.getAllfacilities(), style);
            createCell(row, columnCount++, dataexport.getMatching(), style);
            createCell(row, columnCount++, dataexport.getNotmatching(), style);
             
        }
        
       /* public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }*/
}}
