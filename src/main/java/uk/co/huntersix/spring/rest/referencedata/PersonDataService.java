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

    private final List<Person> persons = new ArrayList<>(PERSON_DATA);
    
    public Person findPerson(String lastName, String firstName) {
    	  // Check if the person exists in the list of Persons
    	  List<Person> foundPerson = persons.stream()
    			  .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
    		                && p.getLastName().equalsIgnoreCase(lastName))
    			  .collect(Collectors.toList());
    	  if(foundPerson.isEmpty()) {
    		  System.out.println("PersonDataService: Person Not found");
    		  throw new PersonNotFoundException();
    	  }
    	  return foundPerson.get(0);
       }
         
    public List<Person> findSurname(String lastName) {
    	   	 	
    	return persons.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
    
    
    public void addPerson(Person p) {
		// If person already exist, throw an exception
    	if(existPerson(p))
    	{
    		throw new PersonAlreadyExistsException();
    	}
		persons.add(p);
	}
    
    
    private boolean existPerson(Person p) {
		
		return existPerson(p.getFirstName(),p.getLastName());
	}

	private boolean existPerson(String firstName, String lastName) {
		return persons.stream()
				.anyMatch(p -> p.getFirstName().equalsIgnoreCase(firstName) 
						&& p.getLastName().equalsIgnoreCase(lastName));
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
}


