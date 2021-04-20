package ca.sheridancollege.beans;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Employee {

	@DocumentId
	private String id;
	
	private String name;
	private int age;
}
