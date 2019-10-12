package uk.co.huntersix.spring.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) {
        return personDataService.findPerson(lastName, firstName);
    }
    
    @GetMapping("/person/{lastName}")
    public List<Person> person(@PathVariable(value="lastName") String lastName) {
        return personDataService.findSurname(lastName);
    }
    
    @PostMapping("/person")
    public Person add(@RequestBody Person p)
    {
    	 // ID will be generated only if a new Person model is generated
        //Person person = new Person(p.getFirstName(),p.getLastName());
    	System.out.println("PersonController person & ID "+ p.toString() + "  "+ p.getId());
    	personDataService.addPerson(p);
        return personDataService.findPerson(p.getFirstName(),p.getLastName());
     }
}