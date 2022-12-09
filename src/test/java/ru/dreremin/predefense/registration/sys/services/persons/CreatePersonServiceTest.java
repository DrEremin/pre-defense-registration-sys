package ru.dreremin.predefense.registration.sys.services.persons;

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

import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;


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
	private PersonDto currentDto;
	private PersonDto firstDto;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		firstDto = new TeacherDto(
				"Иванов", 
				"Иван", 
				"Иванович", 
				"ivan@mail.ru", 
				"ivanlogin", 
				"password12345", 
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
		assertDoesNotThrow(() -> service.createPerson(firstDto));
		assertTrue(autor.existsByLogin("ivanlogin"));
		assertTrue(box.existsByBox("ivan@mail.ru"));
	}
	
	@Test
	void createPerson_LoginExists() throws UniquenessViolationException {
		service.createPerson(firstDto);
		currentDto = new TeacherDto(
				"Петров", 
				"Петр", 
				"Петрович", 
				"petya@mail.ru", 
				"ivanlogin", 
				"password6789", 
				"Доцент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createPerson(currentDto));
		assertFalse(box.existsByBox("petya@mail.ru"));
	}
	
	@Test
	void createPerson_BoxExists() throws UniquenessViolationException {
		service.createPerson(firstDto);
		currentDto = new TeacherDto(
				"Сидоров", 
				"Сидр", 
				"Сидорович", 
				"ivan@mail.ru", 
				"sidlogin", 
				"password0000", 
				"Ассистент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createPerson(currentDto));
		assertFalse(autor.existsByLogin("sidlogin"));
	}
}
