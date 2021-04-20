package ca.sheridancollege.exporter;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ca.sheridancollege.beans.Employee;

public class EmployeeExcelExporter {
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
    private List<Employee> listEmployees;
	
    public EmployeeExcelExporter(List<Employee> listEmployees) {
        this.listEmployees = listEmployees;
        workbook = new XSSFWorkbook();
    }
    
    private void writeHeaderLine() {
    	String nameOfTheSheet = "Employees";
    	
        sheet = workbook.createSheet(nameOfTheSheet);
         
        Row row = sheet.createRow(0);
        
        // You can change the style and font
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID", style);      
        createCell(row, 1, "Name", style);       
        createCell(row, 2, "Age", style);
         
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
    	// The first row is a header
        int rowCount = 1;
        
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        
        font.setFontHeight(14);
        style.setFont(font);
      
        for (Employee emp : listEmployees) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
  
            createCell(row, columnCount++, emp.getId(), style);
            createCell(row, columnCount++, emp.getName(), style);
            createCell(row, columnCount++, emp.getAge(), style);
   
        }
    }
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
