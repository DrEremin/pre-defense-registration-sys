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
import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateCommissionService;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.teachers
		 .CreateTeacherService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherEntryRepositoryTest {

	@Autowired
	private TeacherEntryRepository teacherEntryRepo;
	
	@Autowired
	private CreateTeacherService createTeacherService; 
	
	@Autowired
	private CreateCommissionService createCommissionService; 
	
	@Autowired
	private CreateRegistrationService createRegistrationService;
	
	@Autowired
	private CommissionRepository comissionRepo;
	
	@Autowired
	private TeacherCommissionRepository teacherComissionRepo;
	
	@Autowired
	private TeacherRepository studentRepo;
	
	@Autowired
	private ActorRepository authorizationRepo;
	
	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private EmailRepository emailRepo;
	
	private Instant time;
	
	private String placeholder;
	
	private String[] placeholders;
	
	private List<Commission> commissions;
	
	private static final int SIZE;
	
	static {
		SIZE = 4;
	}
	
	@BeforeAll
	public void beforeAll() throws Exception {
		
		placeholder = "placeholder";
		placeholders = new String[SIZE];
		
		CommissionDto dto = new CommissionDto(
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
			createTeacherService.createTeacher(new TeacherDto(
					placeholders[i],
					placeholders[i],
					placeholders[i],
					placeholders[i] + "@mail.ru",
					placeholders[i],
					placeholders[i],
					placeholder));
			createCommissionService.createComission(dto);
			builder.delete(0, builder.length());
		}
		commissions = comissionRepo.findAll();
		for (int i = 0; i < SIZE - 1; i++) {
			createRegistrationService.createTeacherRegistration(
					new RegistrationDto(placeholders[i], 
										placeholders[i], 
										commissions.get(0).getId()));
		}
		createRegistrationService.createTeacherRegistration(
				new RegistrationDto(placeholders[SIZE - 1], 
									placeholders[SIZE - 1], 
									commissions.get(1).getId()));
	}
	
	@BeforeEach
	public void beforeEach() { time = Instant.now(); }
	
	@AfterEach
	public void afterEach() {
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	@AfterAll
	public void afterAll() {
		teacherComissionRepo.deleteAll();
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		authorizationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
	}
	
	@Test
	void findByComissionId_Success() {
		List<TeacherEntry> entries = teacherEntryRepo.findByComissionId(
				commissions.get(0).getId(), Sort.by(
						Sort.Order.desc("p.lastName")));
		assertTrue(entries.size() == 3);
	}
}
