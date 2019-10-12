package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    
    
    public Person findPerson(String lastName, String firstName) {
    	
          return PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList()).get(0);
    }
         
    public List<Person> findSurname(String lastName) {
    	
    	List<Person> persons = new ArrayList<Person>();
    	
    	persons = PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    	
    	return persons;
	}
    
    
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Person not found")        
    public static class PersonNotFoundException extends RuntimeException {

        PersonNotFoundException() {
            super("Person not found");
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Person already exists")
    public static class PersonAlreadyExistsException extends RuntimeException {

        PersonAlreadyExistsException() {
            super("Person already exists");
        }
    }

	public void addPerson(Object any) {
		// TODO Auto-generated method stub
		
	}
  
}


