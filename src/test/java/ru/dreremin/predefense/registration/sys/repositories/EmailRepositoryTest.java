package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.AfterAll;
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
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher.CreateTeacherService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailRepositoryTest {

	@Autowired private EmailRepository emailRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	@Autowired private ActorRepository authenticationRepo;
	
	@Autowired private PersonRepository personRepo;	
	
	@Autowired private CreateStudentService createStudentService;
	
	@Autowired private CreateTeacherService createTeacherService;
	
	private String login;
	
	private String suffix;
	
	private String password;
	
	private Instant time;
	
	@BeforeAll
	void beforeAll() throws Exception {
		suffix = "@mail.ru";
		login = "name";
		password = "password";
		for (int i = 0; i < 3; i++) {
			login = new StringBuilder("name").append(i).toString();
			createStudentService.createStudent(new StudentRequestDto(
					login,
					password,
					"lastname", 
					"firstname", 
					"patronymic", 
					new StringBuilder(login).append(suffix).toString(),
					"ПИ",
					"Заочное",
					"111"));
		}
		for (int i = 3; i < 5; i++) {
			login = new StringBuilder("name").append(i).toString();
			createTeacherService.createTeacher(new TeacherRequestDto(
					login,
					password,
					"lastname", 
					"firstname", 
					"patronymic", 
					new StringBuilder(login).append(suffix).toString(),
					"Professor"));
		}
	}
	
	@BeforeEach
    void beforeEach() { time = Instant.now(); }

    @AfterEach
    void afterEach() {
        log.info("testing time: " + Duration.between(time, Instant.now()));
    }
    
    @AfterAll
    void afterAll() { 
    	teacherRepo.deleteAll();
    	studentRepo.deleteAll();
    	authenticationRepo.deleteAll();
    	emailRepo.deleteAll(); 
    	personRepo.deleteAll();
    }
    
	@Test
	void findByBox_Success() {
		assertTrue(emailRepo.findByAddress(new StringBuilder("name")
						.append(2)
						.append(suffix)
						.toString())
				.isPresent());
	}
	
	@Test
	void findByBox_DoesNotExists() {
		assertFalse(emailRepo.findByAddress(new StringBuilder("name")
						.append(10)
						.append(suffix)
						.toString())
				.isPresent());
	}
	
	@Test
	void findAllByStudent_Success() {
		assertTrue(emailRepo.findAllByStudent().size() == 3);
	}
	
	@Test
	void findAllByTeacher_Success() {
		assertTrue(emailRepo.findAllByTeacher().size() == 2);
	}
}
