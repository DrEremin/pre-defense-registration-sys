package ru.dreremin.predefense.registration.sys.services.registrations;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthorizationRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
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
class CreateRegistrationServiceTest {
	
	@Autowired private CreateRegistrationService registrationService;
	
	@Autowired private CreateStudentService studentService;
	
	@Autowired private CreateComissionService comissionService;
	
	@Autowired private CreateTeacherService teacherService;
	
	@Autowired private StudentComissionRepository studentComissionRepo;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private AuthorizationRepository authorizationRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	private StudentDto studentDto;
	
	private TeacherDto teacherDto;
	
	private ComissionDto comissionDto;
	
	private Comission comission;
	
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
		for (int i = 0; i < LENGTH; i++) {
			studentDto = new StudentDto(
					s, s, s, emails[i], logins[i], s, s, s, s);
			teacherDto = new TeacherDto(
					s, s, s, emails[i + 3], logins[i + 3], s, s);
			studentService.createStudent(studentDto);
			teacherService.createTeacher(teacherDto);
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
		comissionService.createComission(comissionDto);
		comission = comissionRepo.findAll().get(0);
	}
	
	@BeforeEach
    void beforeEach() { 
		time = Instant.now();
	}
	
	@AfterEach
    void afterEach() {
		studentComissionRepo.deleteAll();
		log.info("test time : " + Duration.between(time, Instant.now()));
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
	void studentRegistration_Success() throws Exception {
		for (int i = 0; i < 2; i++) {
			RegistrationDto dto =  new RegistrationDto(
					logins[i], s, comission.getId());
			assertDoesNotThrow(
					() -> registrationService.studentRegistration(dto));
		}
		assertTrue(studentComissionRepo.findAll().size() == 2);
	}
	
	@Test
	void studentRegistration_PersonDoesNotExist() throws Exception {
		RegistrationDto dto =  new RegistrationDto(
				"non-existent login", s, comission.getId());
		assertThrows(EntityNotFoundException.class, 
				() -> registrationService.studentRegistration(dto));
		assertTrue(studentComissionRepo.findAll().size() == 0);
	}
	
	@Test
	void studentRegistration_StudentDoesNotExist() throws Exception {
		RegistrationDto dto =  new RegistrationDto(
				logins[3], s, comission.getId());
		assertThrows(EntityNotFoundException.class, 
				() -> registrationService.studentRegistration(dto));
		assertTrue(studentComissionRepo.findAll().size() == 0);
	}
	
	@Test
	void studentRegistration_СomissionDoesNotExist() throws Exception {
		RegistrationDto dto =  new RegistrationDto(
				logins[0], s, comission.getId() + 1);
		assertThrows(EntityNotFoundException.class, 
				() -> registrationService.studentRegistration(dto));
		assertTrue(studentComissionRepo.findAll().size() == 0);
	}
	
	@Test
	void studentRegistration_MismatchedStudyDirections() throws Exception {
		ComissionDto otherComissionDto = new ComissionDto(
				ZonedDateTime.parse("2022-08-03T10:45:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:45:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				"other study direction",
				"Аудитория №7",
				(short)2);
		comissionService.createComission(otherComissionDto);
		Comission otherComission = comissionRepo.findAll().get(1);
		
		RegistrationDto dto =  new RegistrationDto(
				logins[0], s, otherComission.getId());
		assertThrows(EntitiesMismatchException.class, 
				() -> registrationService.studentRegistration(dto));
		assertTrue(studentComissionRepo.findAll().size() == 0);
		comissionRepo.delete(otherComission);
	}
	
	@Test
	void studentRegistration_RecordingOverLimit() throws Exception {
		
		RegistrationDto dto;
		
		for (int i = 0; i < 2; i++) {
			dto =  new RegistrationDto(logins[i], s, comission.getId());
			registrationService.studentRegistration(dto);
		}
		assertThrows(OverLimitException.class, 
				() -> registrationService.studentRegistration(
						new RegistrationDto(logins[2], s, comission.getId())));
		assertTrue(studentComissionRepo.findAll().size() == 2);
	}
	
	@Test
	void studentRegistration_SuchRegistrationAlreadyExist() throws Exception {
		RegistrationDto dto =  new RegistrationDto(
				logins[0], s, comission.getId());
		registrationService.studentRegistration(dto);
		assertThrows(UniquenessViolationException.class, 
				() -> registrationService.studentRegistration(dto));
		assertTrue(studentComissionRepo.findAll().size() == 1);
	}
}
