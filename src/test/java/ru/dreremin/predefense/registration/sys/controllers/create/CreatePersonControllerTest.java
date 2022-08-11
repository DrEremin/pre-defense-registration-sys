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
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.factories.EntitiesFactory;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories.AuthorizationRepository;
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
	
	@Autowired private AuthorizationRepository authorRepo;
	
	@Autowired private EmailRepository boxRepo;
	
	@Autowired private MockMvc mockMvc; 
	
	@Autowired private ObjectMapper mapper;
	
	private TeacherDto firstDto;
	
	private Instant time;

	@BeforeAll void beforeAll() throws Exception {
		this.firstDto = new TeacherDto("Иванов", 
								  	   "Иван", 
								  	   "Иванович", 
								  	   "ivanov@mail.ru", 
								  	   "IvanovII", 
								  	   "1234", 
								  	   "Преподаватель");
	}

	@BeforeEach void beforeEach() throws Exception {
		this.time = Instant.now();
	}

	@AfterEach void afterEach() throws Exception {
		this.boxRepo.deleteAll();
        this.authorRepo.deleteAll();
		this.personRepo.deleteAll();
		log.info("run time: " + Duration.between(time, Instant.now()));
	}

	@Test void createTeacher_Success() throws Exception {
		mockMvc.perform(put("/person-create/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(this.firstDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Ok")));
				
		assertTrue(this.personRepo.findAll().size() == 1);
		assertTrue(this.authorRepo.findAll().size() == 1);
		assertTrue(this.boxRepo.findAll().size() == 1);
	}
	
	@Test void createTeacher_LoginExists() throws Exception {
		TeacherDto dto = new TeacherDto("Петров", 
										"Петр", 
										"Петрович", 
										"petrpetrov@mail.ru", 
										"IvanovII", 
										"0987", 
										"Старший преподаватель");
	
		Person person = this.personRepo.save(EntitiesFactory
				.createPerson(this.firstDto));
		
		this.authorRepo.save(EntitiesFactory
				.createAuthorization(this.firstDto, person.getId()));
		this.boxRepo.save(new Email(firstDto.getEmail(), person.getId()));
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
		assertTrue(this.personRepo.findAll().size() == 1);
		assertTrue(this.authorRepo.findAll().size() == 1);
		assertTrue(this.boxRepo.findAll().size() == 1);
	}

	@Test void createTeacher_EmailExists() throws Exception {
		TeacherDto dto = new TeacherDto("Петров", 
										"Петр", 
										"Петрович", 
										"ivanov@mail.ru", 
										"PetrovPP", 
										"4567", 
										"Стар. преподаватель");
	
		Person person = this.personRepo.save(EntitiesFactory
				.createPerson(this.firstDto));
		
		this.authorRepo.save(EntitiesFactory
				.createAuthorization(this.firstDto, person.getId()));
		this.boxRepo.save(new Email(firstDto.getEmail(), person.getId()));
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
		assertTrue(this.personRepo.findAll().size() == 1);
		assertTrue(this.authorRepo.findAll().size() == 1);
		assertTrue(this.boxRepo.findAll().size() == 1);
	}
}
