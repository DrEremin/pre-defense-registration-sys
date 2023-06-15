package ru.dreremin.predefense.registration.sys.services.person;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.PersonRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.services.person.CreatePersonService;
import ru.dreremin.predefense.registration.sys.util.enums.Role;


@Slf4j
@SpringBootTest 
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreatePersonServiceTest {

	@Autowired
	private CreatePersonService service;
	@Autowired
	private ActorRepository autor;
	@Autowired
	private EmailRepository box;
	@Autowired
	private PersonRepository person;
	private PersonRequestDto currentDto;
	private PersonRequestDto firstDto;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		firstDto = new TeacherRequestDto(
				"ivanlogin",
				"password12345",
				"Иванов", 
				"Иван", 
				"Иванович", 
				"ivan@mail.ru",   
				"Профессор");
	}
	
	@BeforeEach
    void beforeEach() { time = Instant.now(); }
	
	@AfterEach
    void afterEach() {
        box.deleteAll();
		autor.deleteAll();
		person.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
    }
	
	@Test
	void createPerson_Success() {
		assertDoesNotThrow(() -> service.createPerson(
				firstDto, Role.TEACHER.getRole()));
		assertTrue(autor.existsByLogin("ivanlogin"));
		assertTrue(box.existsByBox("ivan@mail.ru"));
	}
	
	@Test
	void createPerson_LoginExists() throws UniquenessViolationException {
		service.createPerson(firstDto, Role.TEACHER.getRole());
		currentDto = new TeacherRequestDto(
				"ivanlogin", 
				"password6789",
				"Петров", 
				"Петр", 
				"Петрович", 
				"petya@mail.ru",  
				"Доцент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createPerson(
						currentDto, Role.TEACHER.getRole()));
		assertFalse(box.existsByBox("petya@mail.ru"));
	}
	
	@Test
	void createPerson_BoxExists() throws UniquenessViolationException {
		service.createPerson(firstDto, Role.TEACHER.getRole());
		currentDto = new TeacherRequestDto(
				"sidlogin", 
				"password0000",
				"Сидоров", 
				"Сидр", 
				"Сидорович", 
				"ivan@mail.ru",  
				"Ассистент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createPerson(
						currentDto, Role.TEACHER.getRole()));
		assertFalse(autor.existsByLogin("sidlogin"));
	}
}
