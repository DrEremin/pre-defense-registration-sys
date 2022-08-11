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

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.exceptions.UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories.AuthorizationRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateStudentServiceTest {

	@Autowired private CreateStudentService service;
	@Autowired private AuthorizationRepository author;
	@Autowired private EmailRepository box;
	@Autowired private PersonRepository person;
	private StudentDto currentDto;
	private StudentDto firstDto;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		this.firstDto = new StudentDto("Иванов", 
									   "Иван", 
									   "Иванович", 
									   "ivan@mail.ru", 
									   "ivanlogin", 
									   "password12345",
									   "ПИ",
									   "Очно",
									   "ЗИ981");
	}
	
	@BeforeEach
    void beforeEach() { 
		this.time = Instant.now();
	}
	
	@AfterEach
    void afterEach() {
        this.box.deleteAll();
        this.author.deleteAll();
		this.person.deleteAll();
		log.info("run time: " + Duration.between(time, Instant.now()));
    }
	
	@Test
	void createStudent_Success() {
		assertDoesNotThrow(() -> this.service.createStudent(this.firstDto));
		assertTrue(this.author.existsByLogin("ivanlogin"));
		assertTrue(this.box.existsByBox("ivan@mail.ru"));
	}
	
	@Test
	void createStudent_LoginExists() throws UniquenessViolationException {
		this.service.createStudent(this.firstDto);
		this.currentDto = new StudentDto("Петров", 
										 "Петр", 
										 "Петрович", 
										 "petya@mail.ru", 
										 "ivanlogin", 
										 "password6789", 
										 "ПИ",
										 "Очно",
										 "ЗИ981");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> this.service.createStudent(this.currentDto));
		assertFalse(this.box.existsByBox("petya@mail.ru"));
		assertTrue(((List<Person>)this.person.findAll()).size() == 1);
	}
	
	@Test
	void createStudent_BoxExists() throws UniquenessViolationException {
		this.service.createStudent(this.firstDto);
		this.currentDto = new StudentDto("Петров", 
										 "Петр", 
										 "Петрович", 
										 "ivan@mail.ru", 
										 "petyalogin", 
										 "password6789", 
										 "ПИ",
										 "Очно",
										 "ЗИ981");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> this.service.createStudent(this.currentDto));
		assertFalse(this.author.existsByLogin("petya@mail.ru"));
		assertTrue(((List<Person>)this.person.findAll()).size() == 1);
	}
}
