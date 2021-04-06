package ca.sheridancollege.beans;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Employee {

	private Integer id;
	
	private String name;
	private int age;
}
