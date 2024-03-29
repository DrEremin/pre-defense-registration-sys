package ru.dreremin.predefense.registration.sys.services.commission;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
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
import ru.dreremin.predefense.registration.sys.dto.request.CommissionRequestDto;
//import ru.dreremin.predefense.registration.sys.dto.request.RegistrationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
//import ru.dreremin.predefense.registration.sys.dto.request.impl.AuthenticationDto;
//import ru.dreremin.predefense.registration.sys.dto.response.RegistrationsForStudentDto;
//import ru.dreremin.predefense.registration.sys.dto.response.CurrentCommissionResponseDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
//import ru.dreremin.predefense.registration.sys.models.StudentEntry;
//import ru.dreremin.predefense.registration.sys.models.TeacherEntry;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .TeacherCommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;
import ru.dreremin.predefense.registration.sys.services.commission.CreateCommissionService;
import ru.dreremin.predefense.registration.sys.services.commission.ReadCommissionService;
import ru.dreremin.predefense.registration.sys.services.registration.CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher.CreateTeacherService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadCommissionServiceTest {
/*
	@Autowired private ReadCommissionService readCommissionService;
	
	@Autowired private CreateStudentService createStudentService;
	
	@Autowired private CreateTeacherService createTeacherService;
	
	@Autowired private CreateCommissionService createCommissionService; 
	
	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private CommissionRepository comissionRepo;
	
	@Autowired private StudentCommissionRepository studentComissionRepo;
	
	@Autowired private TeacherCommissionRepository teacherComissionRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	@Autowired private ActorRepository authenticationRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	private Instant time;
	
	private String placeholder;
	
	private String[] lastNames = 
		{ "Казаков", "Иванов", "Игнатьев", "Бурлаков" };
	
	private String[] firstNames = 
		{ "Всеволод", "Сергей", "Андрей", "Илья" };
	
	private String[] patronymics = 
		{ "Михайлович", "Александрович", "Петрович", "Сергеевич" };
	
	private String[][] placeholders;
	
	private List<Commission> commissions;
	
	private static final int SIZE = 4;
	
	private ZonedDateTime[] timestamps;
	
	@BeforeAll
	public void beforeAll() throws Exception {
		
		placeholder = "placeholder";
		placeholders = new String[2][SIZE];
		timestamps = new ZonedDateTime[SIZE];
		
		ZonedDateTime timestamp = ZonedDateTime.now().plusMonths(1);
		CommissionRequestDto dto;
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < SIZE; i++) {
			placeholders[0][i] = builder
					.append(placeholder)
					.append(i)
					.toString();
			createStudentService.createStudent(new StudentRequestDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
					placeholders[0][i] + "@mail.ru",
					placeholders[0][i],
					placeholders[0][i],
					placeholder,
					placeholder,
					"ЗИ98" + i));
			builder.delete(0, builder.length());
			placeholders[1][i] = builder
					.append(placeholder)
					.append(i + SIZE)
					.toString();
			createTeacherService.createTeacher(new TeacherRequestDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
					placeholders[1][i] + "@mail.ru",
					placeholders[1][i],
					placeholders[1][i],
					placeholder));
			timestamp = timestamp.minusDays(i);
			timestamps[i] = timestamp;
			dto = new CommissionRequestDto(
					timestamp,
					timestamp.plusHours(2),
					(i % 2 == 1) ? placeholder + "1234" : placeholder,
					"Аудитория №7",
					(short)10);
			createCommissionService.createComission(dto);
			builder.delete(0, builder.length());
		}
		commissions = comissionRepo.findAll();
		for (int i = 0; i < SIZE - 1; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationRequestDto(placeholders[0][i], 
										placeholders[0][i], 
										commissions.get(0).getId()));
		}
		for (int i = 0, k = 0; i < SIZE; i++, k++) {
			for (int j = 0; j < SIZE - k; j++) {
				createRegistrationService.createTeacherRegistration(
						new RegistrationRequestDto(placeholders[1][j], 
											placeholders[1][j], 
											commissions.get(i).getId()));
			}
		}
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
		teacherComissionRepo.deleteAll();
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		teacherRepo.deleteAll();
		authenticationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
	}
	
	@Test
	public void getCurrentComissionOfStudent_Success() throws Exception {
		
		assertDoesNotThrow(() -> readCommissionService
				.getCurrentComissionOfStudent(new AuthenticationRequestDto(
						placeholders[0][0], placeholders[0][0])));
		
		CurrentCommissionResponseDto dto = readCommissionService
				.getCurrentComissionOfStudent(new AuthenticationRequestDto(
						placeholders[0][0], placeholders[0][0]));
		
		assertTrue(commissions.get(0).getStudyDirection().equals(
				dto.getStudyDirection()));
		assertTrue(commissions.get(0).getStartDateTime().toLocalDate().equals(
				dto.getDate()));
		assertTrue(commissions.get(0).getStartDateTime().toLocalTime().equals(
				dto.getStartTime()));
		assertTrue(commissions.get(0).getEndDateTime().toLocalTime().equals(
				dto.getEndTime()));
		assertTrue(commissions.get(0).getLocation().equals(
				dto.getLocation()));
		
		List<StudentEntry> students = dto.getStudents();
		
		assertTrue("Иванов Сергей Александрович".equals(
				students.get(0).getFullName()));
		assertTrue("Игнатьев Андрей Петрович".equals(
				students.get(1).getFullName()));
		assertTrue("Казаков Всеволод Михайлович".equals(
				students.get(2).getFullName()));
	}   
	
	@Test
	public void getActualComissionsListForStudent_Success() throws Exception {
		assertDoesNotThrow(() -> readCommissionService
				.getActualComissionsListForStudent(new AuthenticationRequestDto(
						placeholders[0][0], placeholders[0][0])));
		
		List<RegistrationsForStudentDto> dtoList = readCommissionService
				.getActualComissionsListForStudent(new AuthenticationRequestDto(
						placeholders[0][0], placeholders[0][0]));
		
		int i = SIZE / 2 + SIZE % 2, j = 0;
		
		assertTrue(dtoList.size() == i);
		
		List<TeacherEntry> teachers;
		String[] s = {"Иванов С.А. Казаков В.М. ", 
				"Бурлаков И.С. Иванов С.А. Игнатьев А.П. Казаков В.М. "};
		StringBuilder builder;
		
		for (RegistrationsForStudentDto dto : dtoList) {
			assertTrue(dto.getDate().equals(timestamps[i].toLocalDate()));
			i -= 2;
			teachers = dto.getTeachers();
			builder = new StringBuilder();
			for (TeacherEntry teacher : teachers) {
				builder.append(teacher.getFullName()).append(" ");
			}
			assertEquals(s[j++], builder.toString());
		}
	}
	
	@Test
	public void getCurrentComissionOfStudent_StudentDontRegistered() 
			throws Exception {
		
		CurrentCommissionResponseDto dto = null;
		
		try {
			dto = readCommissionService
					.getCurrentComissionOfStudent(new AuthenticationRequestDto(
							placeholders[0][SIZE - 1], 
							placeholders[0][SIZE - 1]));
		} catch (EntityNotFoundException e) {
			assertEquals(e.getMessage(), "This student is not "
					+ "registered for any commission");
			assertNull(dto);
		}
		assertNull(dto);
	}*/
}
