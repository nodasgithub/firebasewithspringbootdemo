package ca.sheridancollege.controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import ca.sheridancollege.beans.Employee;
import ca.sheridancollege.service.EmployeeService;
import ca.sheridancollege.service.FirebaseInitializer;

@RestController
public class APIController {

	@Autowired
	FirebaseInitializer db;
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() throws InterruptedException, ExecutionException {
		List<Employee> empList = new ArrayList<Employee>();
		
		CollectionReference employee = db.getFirebase()
				.collection("Employee");
		
		ApiFuture<QuerySnapshot> querySnapshot = employee.get();
		
		for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
			Employee emp = doc.toObject(Employee.class);
			empList.add(emp);
		}
		
		return empList;
	}
	
	
	@PostMapping("/createEmployee")
    public String createPatient(@RequestBody Employee employee) throws InterruptedException, ExecutionException {
       return employeeService.saveEmployeeDetails(employee);
    }
	
	@PutMapping("/updateEmployee")  
	public String updatePatient(@RequestBody Employee employee) throws InterruptedException, ExecutionException {  
		return employeeService.updateEmployeeDetails(employee);  
	}  
	 
	@GetMapping("/getEmployeeDetails")  
	public Employee getPatient(@RequestParam String name) throws InterruptedException, ExecutionException {  
		return employeeService.getEmployeeDetails(name);  
	}
	
	/*
	@GetMapping("/getEmployeeDetails/{id}")  
	public Employee getPatient(@PathVariable int id) throws InterruptedException, ExecutionException {  
		return employeeService.getEmployeeDetails(id);  
	}
	*/
	 
	@DeleteMapping("/deleteEmployee")  
	public String deletePatient(@RequestParam String name) {  
		return employeeService.deleteEmployee(name);  
	}
}
