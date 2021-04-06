package ca.sheridancollege.controllers;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import ca.sheridancollege.beans.*;

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
	
	/*
	@GetMapping("/edit/{name}") 
	public String goEditPlayer(@PathVariable int id, Model model){
		
		if (player.isPresent()) {
			Player selectedPlayer = player.get();
			model.addAttribute("player", selectedPlayer);
			
			return "editPlayer.html";
		} else {
			model.addAttribute("players", playerRepo.findAll());
			model.addAttribute("player", new Player());
			
			return "redirect:/viewPlayer";
		}
	}
	*/
	
	/*
	@GetMapping("/delete/{name}")
    public String deleteEmployee(@PathVariable String name, RestTemplate restTemplate) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Employee objEmp = new Employee();

		HttpEntity<Employee> httpEntity = new HttpEntity<Employee>(objEmp, headers);
		
		
		String url = "http://localhost:8080/deleteEmployee";
		ResponseEntity<String> responseEntity = 
			restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class, name); 
        
        return "redirect:/viewEmployees";
    }
    */
	

}
