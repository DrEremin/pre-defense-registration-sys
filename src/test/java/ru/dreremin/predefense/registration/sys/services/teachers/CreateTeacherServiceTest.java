package ru.dreremin.predefense.registration.sys.services.teachers;

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

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthorizationRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateTeacherServiceTest {
	
	@Autowired private CreateTeacherService service;
	@Autowired private AuthorizationRepository author;
	@Autowired private EmailRepository box;
	@Autowired private PersonRepository person;
	private TeacherDto currentDto;
	private TeacherDto firstDto;
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
        author.deleteAll();
		person.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
    }
	
	@Test
	void createTeacher_Success() {
		assertDoesNotThrow(() -> service.createTeacher(firstDto));
		assertTrue(author.existsByLogin("ivanlogin"));
		assertTrue(box.existsByBox("ivan@mail.ru"));
	}
	
	@Test
	void createTeacher_LoginExists() throws UniquenessViolationException {
		service.createTeacher(firstDto);
		currentDto = new TeacherDto(
				"Петров", 
				"Петр", 
				"Петрович", 
				"petya@mail.ru", 
				"ivanlogin", 
				"password6789", 
				"Доцент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createTeacher(currentDto));
		assertFalse(box.existsByBox("petya@mail.ru"));
		assertTrue(((List<Person>)person.findAll()).size() == 1);
	}
	
	@Test
	void createTeacher_BoxExists() throws UniquenessViolationException {
		service.createTeacher(firstDto);
		currentDto = new TeacherDto(
				"Петров", 
				"Петр", 
				"Петрович", 
				"ivan@mail.ru", 
				"petyalogin", 
				"password6789", 
				"Доцент");
		assertThrowsExactly(UniquenessViolationException.class, 
				() -> service.createTeacher(currentDto));
		assertFalse(author.existsByLogin("petya@mail.ru"));
		assertTrue(((List<Person>)person.findAll()).size() == 1);
	}

}
