package ca.sheridancollege.importer;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.beans.Employee;

public class ExcelToFirebase {
	
	public static List<Employee> convertExcelToFirebaseData(MultipartFile file) {
        
        String userDirectory = Paths.get("")
                .toAbsolutePath()
                .toString();
        
        System.out.println("###################################################################");
        System.out.println("Path: ");
        
        // For debuging purposes
        // System.out.println("Current Directory: " + userDirectory);
 
        try {
            long start = System.currentTimeMillis();
             
            
            /**
            InputStream initialStream = file.getInputStream();
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);
			**/

            File targetFile = new File("src/main/resources/targetFile.tmp");
            
            
            FileInputStream inputStream = new FileInputStream(targetFile);
 
            Workbook workbook = new XSSFWorkbook(inputStream);
 
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();   
             
            int count = 0;
             
            rowIterator.next(); // skip the header row
            
            List<Employee> empList = new ArrayList<>();
            
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
 
                Employee emp = new Employee();
                
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
 
                    int columnIndex = nextCell.getColumnIndex();
 
                    switch (columnIndex) {
                    case 0:
                    	// This id is String
                    	String id = nextCell.getStringCellValue();
                    	emp.setId(id);
                        break;
                        
                    case 1:
                    	String name = nextCell.getStringCellValue();
                    	emp.setName(name);
                    	break;
                    	
                    case 2:
                    	int age = (int) nextCell.getNumericCellValue();
                    	emp.setAge(age);
                        break;
                    }
                }
               
                if (emp.getName() != null && emp.getName() != "") {
                	empList.add(emp);
                }
            }
 
            workbook.close();
             
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
            
            return empList;
             
        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        }
        
        return null;
    }
}
