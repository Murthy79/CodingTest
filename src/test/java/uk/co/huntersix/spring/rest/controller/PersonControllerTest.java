package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService.PersonAlreadyExistsException;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService.PersonNotFoundException;

import java.util.Arrays;
import java.util.Collections;

// Static imports - does not need to be qualified by Class Name
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }
    
    @Test
    public void shouldReturnNoContentWhenPersonNotFound() throws Exception
    {
    	when(personDataService.findPerson(any(), any())).thenThrow(PersonNotFoundException.class);
    	this.mockMvc.perform(get("/person/lName/fName"))
    	.andDo(print())
    	.andExpect(status().reason("Person not found"))
    	.andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldReturnPeopleFromService() throws Exception {
    	when(personDataService.findSurname(any())).thenReturn(Arrays.asList(
    			new Person("Mary","Smith"),
    			new Person("Adam", "Smith")
    			));
    	this.mockMvc.perform(get("/person/smith"))
    	.andDo(print())
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$").isArray())
    	.andExpect(jsonPath("$",hasSize(2)))
    	.andExpect(jsonPath("$[0].firstName").value("Mary"))
    	.andExpect(jsonPath("$[0].lastName").value("Smith"))
    	.andExpect(jsonPath("$[1].firstName").value("Adam"))
    	.andExpect(jsonPath("$[1].lastName").value("Smith"));    	
    }
    
    @Test
    public void shouldReturnEmptyArrayForNoPeople() throws Exception {
    	when(personDataService.findSurname(any())).thenReturn(Collections.emptyList());
    	
    	this.mockMvc.perform(get("/person/smith"))
    	.andDo(print())
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$").isArray())
    	.andExpect(jsonPath("$",hasSize(0)));    
    }
    
    @Test
    public void shouldReturnArrayForSinglePerson() throws Exception {
        when(personDataService.findSurname(any())).thenReturn(Collections.singletonList(new Person("Mary", "Smith")));

        this.mockMvc.perform(get("/person/smith"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].firstName").value("Mary"))
            .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }
    
    @Test
    public void shouldCreateNewPerson() throws Exception {
        Person murthy = new Person("Murthy", "Narasimhan");
        String jsonBody = new ObjectMapper().writeValueAsString(murthy);

        this.mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonBody))
            .andDo(print())
            .andExpect(status().isOk());

        verify(personDataService, times(1)).addPerson(any());
    }
    
    @Test
    public void shouldReturnErrorWhenPersonExists() throws Exception {
        Person jonSnow = new Person("Murthy", "Narasimhan");
        String jsonBody = new ObjectMapper().writeValueAsString(jonSnow);

        doThrow(PersonAlreadyExistsException.class).when(personDataService).addPerson(any());

        this.mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonBody))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(status().reason("Person already exists"));
    }
       
}