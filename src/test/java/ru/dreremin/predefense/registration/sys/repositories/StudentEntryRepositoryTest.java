package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.requestdto.ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateComissionService;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.students
		 .CreateStudentService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentEntryRepositoryTest {

	@Autowired
	private StudentEntryRepository studentEntryRepo;
	
	@Autowired
	private CreateStudentService createStudentService; 
	
	@Autowired
	private CreateComissionService createComissionService; 
	
	@Autowired
	private CreateRegistrationService createRegistrationService;
	
	@Autowired
	private ComissionRepository comissionRepo;
	
	@Autowired
	private StudentComissionRepository studentComissionRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private ActorRepository authorizationRepo;
	
	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private EmailRepository emailRepo;
	
	private Instant time;
	
	private String placeholder;
	
	private String[] placeholders;
	
	private List<Comission> comissions;
	
	private static final int SIZE;
	
	static {
		SIZE = 4;
	}
	
	@BeforeAll
	public void beforeAll() throws Exception {
		
		placeholder = "placeholder";
		placeholders = new String[SIZE];
		
		ComissionDto dto = new ComissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				placeholder,
				"Аудитория №7",
				(short)10);
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < SIZE; i++) {
			placeholders[i] = builder.append(placeholder).append(i).toString();
			createStudentService.createStudent(new StudentDto(
					placeholders[i],
					placeholders[i],
					placeholders[i],
					placeholders[i] + "@mail.ru",
					placeholders[i],
					placeholders[i],
					placeholder,
					placeholder,
					"ЗИ98" + i));
			createComissionService.createComission(dto);
			builder.delete(0, builder.length());
		}
		comissions = comissionRepo.findAll();
		for (int i = 0; i < SIZE - 1; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationDto(placeholders[i], 
										placeholders[i], 
										comissions.get(0).getId()));
		}
		createRegistrationService.createStudentRegistration(
				new RegistrationDto(placeholders[SIZE - 1], 
									placeholders[SIZE - 1], 
									comissions.get(1).getId()));
	}
	
	@BeforeEach
	public void beforeEach() { time = Instant.now(); }
	
	@AfterEach
	public void afterEach() {
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	@AfterAll
	public void afterAll() {
		studentComissionRepo.deleteAll();
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		authorizationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
	}
	
	@Test
	void findByComissionId_Success() {
		List<StudentEntry> entries = studentEntryRepo.findByComissionId(
				comissions.get(0).getId(), Sort.by(Sort.Order.desc("p.lastName")));
		assertTrue(entries.size() == 3);
	}
}
