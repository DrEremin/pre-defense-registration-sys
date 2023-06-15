package ru.dreremin.predefense.registration.sys.services.students;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

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
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateStudentServiceTest {

	@Autowired private CreateStudentService service;
	@Autowired private ActorRepository author;
	@Autowired private EmailRepository box;
	@Autowired private PersonRepository person;
	private StudentRequestDto currentDto;
	private StudentRequestDto firstDto;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		firstDto = new StudentRequestDto(
				"ivanlogin", 
				"password12345",
				"Иванов", 
				"Иван", 
				"Иванович", 
				"ivan@mail.ru", 
				"ПИ",
				"Очно",
				"ЗИ981");
	}
	
	@BeforeEach
    void beforeEach() { time = Instant.now(); }
	
	@AfterEach
    void afterEach() {
        box.deleteAll();
        author.deleteAll();
		person.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
    }
	
	@Test
	void createStudent_Success() {
		assertDoesNotThrow(() -> service.createStudent(firstDto));
		assertTrue(author.existsByLogin("ivanlogin"));
		assertTrue(box.existsByBox("ivan@mail.ru"));
	}
	
	@Test
	void createStudent_LoginExists() throws UniquenessViolationException {
		service.createStudent(firstDto);
		currentDto = new StudentRequestDto(
				"ivanlogin", 
				"password6789", 
				"Петров", 
				"Петр", 
				"Петрович", 
				"petya@mail.ru", 
				"ПИ",
				"Очно",
				"ЗИ981");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createStudent(currentDto));
		assertFalse(box.existsByBox("petya@mail.ru"));
		assertTrue(((List<Person>)person.findAll()).size() == 1);
	}
	
	@Test
	void createStudent_BoxExists() throws UniquenessViolationException {
		service.createStudent(firstDto);
		currentDto = new StudentRequestDto(
				"petyalogin", 
				"password6789",
				"Петров", 
				"Петр", 
				"Петрович", 
				"ivan@mail.ru",  
				"ПИ",
				"Очно",
				"ЗИ981");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createStudent(currentDto));
		assertFalse(author.existsByLogin("petya@mail.ru"));
		assertTrue(((List<Person>)person.findAll()).size() == 1);
	}
}
