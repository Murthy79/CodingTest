package uk.co.huntersix.spring.rest.referencedata;

import static org.junit.Assert.*;

import java.util.List;
import java.util.*;

import org.junit.Test;

import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService.PersonAlreadyExistsException;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService.PersonNotFoundException;

public class PersonDataServiceTest {
	private PersonDataService service = new PersonDataService();
	
	@Test
	public void findPerson_shouldReturnPerson() {
		Person p = service.findPerson("Smith", "Mary");
		assertEquals("Mary",p.getFirstName());
		assertEquals("Smith",p.getLastName());
	}
	
	// Test if the Given Name ignores Case sensitive
	@Test
	public void shouldIgnoreCase()
	{
		Person p = service.findPerson("smiTh", "MaRy");
		assertEquals("Mary",p.getFirstName());
		assertEquals("Smith",p.getLastName());
	}
	
	// Test if the service throws exception if there is no matching Names
	@Test(expected = PersonNotFoundException.class)
	public void shouldThrowForNoMatchingPerson() {
		service.findPerson("Murthy", "Narasimhan");
	}
	
	// Should return multiple people if it matches
	@Test
	public void shouldReturnMultiplePersonsForMatch()
	{
		Person smith1 = service.findPerson("Smith", "Mary");
		Person smith2 = new Person("Gina","Smith");
		service.addPerson(smith2);
		
		List<Person> actual = service.findSurname("Smith");
		List<Person> expected = Arrays.asList(smith1,smith2);
		assertEquals(expected,actual);
	}
	
	// Should return Empty list for No Matches
	@Test
	public void shouldReturnEmptyListForNoMatches() {
		List<Person> p = service.findSurname("Rohit");
		assertTrue(p.isEmpty());
	}
	
	// Add New person
	@Test
	public void addNewPerson() {
		Person murthy = new Person("Murthy","Narasimhan");
		service.addPerson(murthy);
		
		Person p = service.findPerson("Narasimhan", "Murthy"); 
		assertEquals(murthy,p);	
	}
	
	// Throw exception if person already exists
	@Test(expected=PersonAlreadyExistsException.class)
	public void addPerson_shouldFailForExisting() {
		Person anotherSmith = new Person("Mary","Smith");
		service.addPerson(anotherSmith);		
	}
}

