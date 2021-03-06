package ca.sheridancollege.controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.beans.*;
import ca.sheridancollege.exporter.EmployeeExcelExporter;
import ca.sheridancollege.importer.ExcelToFirebase;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String goHome() {
		return "index.html";
	}
	
	@GetMapping("/addEmployee")
	public String loadAddStore(Model model) {
		model.addAttribute("employee", new Employee());
		
		return "addEmployee.html";
	}

	@PostMapping("/addEmployee")
	public String newEmployee(@ModelAttribute Employee employee, RestTemplate restTemplate) {
		restTemplate.postForEntity("http://localhost:8080/createEmployee", employee, String.class);
		
		return "redirect:/addEmployee";
    }

	@GetMapping("/viewEmployees")
    public String goHome(Model model, RestTemplate restTemplate) {
		
		ResponseEntity<Employee[]> responseEntity = restTemplate
                .getForEntity("http://localhost:8080/getAllEmployees", Employee[].class);

		model.addAttribute("employees", responseEntity.getBody());

		return "viewEmployees.html";
    }
	
	@GetMapping("/addEmployeeFromExcel")
	public String loadAddEmployeeFromExcel() {
		
		return "addEmployeeFromExcel.html";
	}
	
	@GetMapping("/edit/{id}")
    public String goEditEmployee(@PathVariable String id, Model model, RestTemplate restTemplate) {
        ResponseEntity<Employee> employee = restTemplate.getForEntity("http://localhost:8080/getEmployeeDetails" + "/" + id, Employee.class);
        model.addAttribute("employee", employee.getBody());
        return "editEmployee.html";
    }
	
	@PostMapping("/editEmployee")
    public String editEmployee(@ModelAttribute Employee employee, RestTemplate restTemplate) {
        restTemplate.postForEntity("http://localhost:8080/updateEmployee", employee, String.class);
        
        return "redirect:/viewEmployees";
    }
	
	@GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id, RestTemplate restTemplate) {
        restTemplate.postForEntity("http://localhost:8080/deleteEmployee", id, String.class);
        return "redirect:/viewEmployees";
    }
	
	@GetMapping("/employees/export/excel")
    public void exportToExcel(HttpServletResponse response, RestTemplate restTemplate) throws IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        ResponseEntity<Employee[]> responseEntity = restTemplate
                .getForEntity("http://localhost:8080/getAllEmployees", Employee[].class);
        
        List<Employee> empList = Arrays.asList(responseEntity.getBody());
        
        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(empList);
         
        excelExporter.export(response);    
    }
	
	@PostMapping("/employees/import/excel")
    public String importFromExcelToFirebase(@RequestParam("file") MultipartFile file, RestTemplate restTemplate) throws IOException {
		
		File temp = new File("src/main/resources/targetFile.tmp");

		try (OutputStream os = new FileOutputStream(temp)) {
		    os.write(file.getBytes());
		}
		
		if (ExcelToFirebase.convertExcelToFirebaseData(file) != null) {
			List<Employee> empList = ExcelToFirebase.convertExcelToFirebaseData(file);

			System.out.println(empList.toString());

			restTemplate.postForEntity("http://localhost:8080/createMultipleEmployees", empList, String.class);
			
		} else {
			System.out.println("Import failed");
		}
		
		return "redirect:/addEmployeeFromExcel";
    }
	
	

}
