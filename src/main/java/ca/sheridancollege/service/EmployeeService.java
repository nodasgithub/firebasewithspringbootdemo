package ca.sheridancollege.service;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Component;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import ca.sheridancollege.beans.Employee;

@Component
public class EmployeeService {
	
	public static final String COL_NAME="Employee";
	
	public String saveEmployeeDetails(Employee employee) throws InterruptedException, ExecutionException {  
		Firestore dbFirestore = FirestoreClient.getFirestore();  
		String newId = dbFirestore.collection(COL_NAME).document().getId();
		employee.setId(newId);
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(newId).set(employee);  
		
		return collectionsApiFuture.get().getUpdateTime().toString();  
	}
	
	public String saveEmployees(List<Employee> empList) throws InterruptedException, ExecutionException {  
		Firestore dbFirestore = FirestoreClient.getFirestore();
		
		for (Employee employee: empList) {
			//String newId = dbFirestore.collection(COL_NAME).document().getId();
			//employee.setId(newId);
			ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(employee.getId()).set(employee);
		}
		
		return "Collection added";  
	}
	
	public Employee getEmployeeDetails(String id) throws InterruptedException, ExecutionException {  
		Firestore dbFirestore = FirestoreClient.getFirestore();  
		DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(id);  
		ApiFuture<DocumentSnapshot> future = documentReference.get();  
		DocumentSnapshot document = future.get();  
		
		Employee employee = null;
		
		if(document.exists()) {  
			employee = document.toObject(Employee.class); 
			return employee;
		}else {  
			return null;  
		}
	}
	
	public String updateEmployeeDetails(Employee employee) throws InterruptedException, ExecutionException {  
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(employee.getId()).set(employee);
		return collectionsApiFuture.get().getUpdateTime().toString();   
	}

	public String deleteEmployee(String name) {  
		Firestore dbFirestore = FirestoreClient.getFirestore();  
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(name).delete();  
		return "Document with Employee ID "+name+" has been deleted";  
	}
}  
