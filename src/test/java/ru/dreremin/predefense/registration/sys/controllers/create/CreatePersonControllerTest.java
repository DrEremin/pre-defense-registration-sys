package ru.dreremin.predefense.registration.sys.controllers.create;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request
				 .MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result
				 .MockMvcResultMatchers.*;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet
		  .AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthenticationRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreatePersonControllerTest {
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private AuthenticationRepository authorRepo;
	
	@Autowired private EmailRepository boxRepo;
	
	@Autowired private MockMvc mockMvc; 
	
	@Autowired private ObjectMapper mapper;
	
	private TeacherDto firstDto;
	
	private Instant time;

	@BeforeAll 
	void beforeAll() throws Exception {
		firstDto = new TeacherDto(
				"????????????", 
				"????????", 
				"????????????????", 
				"ivanov@mail.ru", 
				"IvanovII", 
				"1234", 
				"??????????????????????????");
	}

	@BeforeEach 
	void beforeEach() throws Exception { time = Instant.now(); }

	@AfterEach void afterEach() throws Exception {
		boxRepo.deleteAll();
        authorRepo.deleteAll();
		personRepo.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}

	@Test 
	void createTeacher_Success() throws Exception {
		mockMvc.perform(put("/person-create/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(firstDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Ok")));
				
		assertTrue(personRepo.findAll().size() == 1);
		assertTrue(authorRepo.findAll().size() == 1);
		assertTrue(boxRepo.findAll().size() == 1);
	}
	
	@Test 
	void createTeacher_LoginExist() throws Exception {
		TeacherDto dto = new TeacherDto(
				"????????????", 
				"????????", 
				"????????????????", 
				"petrpetrov@mail.ru", 
				"IvanovII", 
				"0987", 
				"?????????????? ??????????????????????????");
	
		Person person = personRepo.save(EntitiesFactory
				.createPerson(firstDto));
		
		authorRepo.save(EntitiesFactory
				.createAuthorization(firstDto, person.getId()));
		boxRepo.save(new Email(firstDto.getEmail(), person.getId()));
		mockMvc.perform(put("/person-create/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("The user with this login already exists")))
				.andExpect(r -> assertInstanceOf(
						UniquenessViolationException.class, 
						r.getResolvedException()));
		assertTrue(personRepo.findAll().size() == 1);
		assertTrue(authorRepo.findAll().size() == 1);
		assertTrue(boxRepo.findAll().size() == 1);
	}

	@Test 
	void createTeacher_EmailExist() throws Exception {
		TeacherDto dto = new TeacherDto(
				"????????????", 
				"????????", 
				"????????????????", 
				"ivanov@mail.ru", 
				"PetrovPP", 
				"4567", 
				"????????. ??????????????????????????");
	
		Person person = personRepo.save(EntitiesFactory
				.createPerson(firstDto));
		
		authorRepo.save(EntitiesFactory
				.createAuthorization(firstDto, person.getId()));
		boxRepo.save(new Email(firstDto.getEmail(), person.getId()));
		mockMvc.perform(put("/person-create/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("The user with this email already exists")))
				.andExpect(r -> assertInstanceOf(
						UniquenessViolationException.class, 
						r.getResolvedException()));
		assertTrue(personRepo.findAll().size() == 1);
		assertTrue(authorRepo.findAll().size() == 1);
		assertTrue(boxRepo.findAll().size() == 1);
	}
}
