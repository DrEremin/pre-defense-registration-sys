package ru.dreremin.predefense.registration.sys.services.registrations;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthenticationRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateComissionService;
import ru.dreremin.predefense.registration.sys.services.students
		 .CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teachers
		 .CreateTeacherService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteRegistrationServiceTest {

	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private DeleteRegistrationService deleteRegistrationService;
	
	@Autowired private CreateStudentService studentService;
	
	@Autowired private CreateComissionService comissionService;
	
	@Autowired private CreateTeacherService teacherService;
	
	@Autowired private StudentComissionRepository studentComissionRepo;
	
	@Autowired private TeacherComissionRepository teacherComissionRepo;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private AuthenticationRepository authorizationRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	private StudentDto studentDto;
	
	private TeacherDto teacherDto;
	
	private ComissionDto comissionDto;
	
	private List<Comission> comissions;
	
	private String s;
	
	private String[] emails, logins;
	
	private Instant time;

	@BeforeAll
	void beforeAll() throws Exception {
		s = "plholder";
		final int LENGTH = 3;
		emails = new String[LENGTH << 1];
		logins = new String[LENGTH << 1];
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < LENGTH << 1; i++) {
			logins[i] = builder.append("person").append(i + 1).toString();
			emails[i] = builder.append("@mail.ru").toString();
			builder = builder.delete(0, builder.length());
		}
		comissionDto = new ComissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				s,
				"Аудитория №7",
				(short)2);
		for (int i = 0; i < LENGTH; i++) {
			studentDto = new StudentDto(
					s, s, s, emails[i], logins[i], s, s, s, s);
			teacherDto = new TeacherDto(
					s, s, s, emails[i + 3], logins[i + 3], s, s);
			studentService.createStudent(studentDto);
			teacherService.createTeacher(teacherDto);
			comissionService.createComission(comissionDto);
		}

		comissions = comissionRepo.findAll();
	}
	
	@BeforeEach
    void beforeEach() throws Exception{ 
		time = Instant.now();
		for (int i = 0; i < 2; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationDto(
							logins[i], s, comissions.get(i).getId()));
			createRegistrationService.createTeacherRegistration(
					new RegistrationDto(
							logins[i + 3], s, comissions.get(i).getId()));
		} 
	}
	
	@AfterEach
    void afterEach() {
		studentComissionRepo.deleteAll();
		teacherComissionRepo.deleteAll();
		log.info("testing time : " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
    void afterAll() {
		authorizationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
		studentRepo.deleteAll();
		teacherRepo.deleteAll();
		comissionRepo.deleteAll();
	}

	@Test
	void deleteStudentRegistration_Success() throws Exception {
		assertTrue(studentComissionRepo.count() == 2);
		assertDoesNotThrow(() -> deleteRegistrationService
				.deleteStudentRegistration(new AuthenticationDto(
						logins[1], s)));
		assertTrue(studentComissionRepo.count() == 1);
	}
	
	@Test
	void deleteTeacherRegistration_Success() throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		assertDoesNotThrow(() -> deleteRegistrationService
				.deleteTeacherRegistration(new RegistrationDto(
						logins[3], s, comissions.get(0).getId())));
		assertTrue(teacherComissionRepo.count() == 1);
	}
	
	@Test
	void deleteStudentRegistration_PersonDoesNotExist() throws Exception {
		assertTrue(studentComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteStudentRegistration(
					new AuthenticationDto("non-existent login", s));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("There is not exists person with this login", 
						 e.getMessage());
		}
		assertTrue(studentComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_PersonDoesNotExist() throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteTeacherRegistration(
					new RegistrationDto("non-existent login", 
										s, 
										comissions.get(0).getId()));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("There is not exists person with this login", 
						 e.getMessage());
		}
		assertTrue(teacherComissionRepo.count() == 2);
	}
	
	@Test
	void deleteStudentRegistration_PasswordDoesNotMatchLogin() 
			throws Exception {
		assertTrue(studentComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteStudentRegistration(
					new AuthenticationDto(logins[0], "invalid password"));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(FailedAuthenticationException.class, e);
			assertEquals("Сlient is not authenticated", 
						 e.getMessage());
		}
		assertTrue(studentComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_PasswordDoesNotMatchLogin() 
			throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteTeacherRegistration(
					new RegistrationDto(logins[3], 
										"invalid password", 
										comissions.get(0).getId()));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(FailedAuthenticationException.class, e);
			assertEquals("Сlient is not authenticated", 
						 e.getMessage());
		}
		assertTrue(teacherComissionRepo.count() == 2);
	}
	
	@Test
	void deleteStudentRegistration_StudentDoesNotExist() 
			throws Exception {
		assertTrue(studentComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteStudentRegistration(
					new AuthenticationDto(logins[3], s));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("There is not exists student with this login", 
						 e.getMessage());
		}
		assertTrue(studentComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_TeacherDoesNotExist() 
			throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteTeacherRegistration(
					new RegistrationDto(logins[0], 
										s, 
										comissions.get(0).getId()));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("There is not exists teacher with this login", 
						 e.getMessage());
		}
		assertTrue(teacherComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_СomissionDoesNotExist() 
			throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteTeacherRegistration(
					new RegistrationDto(logins[3], 
										s, 
										comissions.get(0).getId() + 10));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("There is not exists comission with this Id", 
						 e.getMessage());
		}
		assertTrue(teacherComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_DoesNotSuchRegistration() 
			throws Exception {
		assertTrue(teacherComissionRepo.count() == 2);
		try {
			deleteRegistrationService.deleteTeacherRegistration(
					new RegistrationDto(logins[3], 
										s, 
										comissions.get(1).getId()));
		} catch (EntityNotFoundException | FailedAuthenticationException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals("The registration for this teacher "
					+ "for such comission does not exist", 
						 e.getMessage());
		}
		assertTrue(teacherComissionRepo.count() == 2);
	}
}
