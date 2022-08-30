package ru.dreremin.predefense.registration.sys.services.registrations;

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

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .AuthenticationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.TeacherDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .ActualComissionForStudentDto;
import ru.dreremin.predefense.registration.sys.dto.responsedto
		 .CurrentComissionOfStudentDto;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;
import ru.dreremin.predefense.registration.sys.repositories
		 .AuthenticationRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;
import ru.dreremin.predefense.registration.sys.repositories.PersonRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .StudentComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.StudentRepository;
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
class ReadRegistrationServiceTest {

	@Autowired private ReadRegistrationService readRegistrationService;
	
	@Autowired private CreateStudentService createStudentService;
	
	@Autowired private CreateTeacherService createTeacherService;
	
	@Autowired private CreateComissionService createComissionService; 
	
	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private StudentComissionRepository studentComissionRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private AuthenticationRepository authorizationRepo;
	
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
	
	private List<Comission> comissions;
	
	private static final int SIZE = 4;
	
	private ZonedDateTime[] timestamps;
	
	@BeforeAll
	public void beforeAll() throws Exception {
		
		placeholder = "placeholder";
		placeholders = new String[2][SIZE];
		timestamps = new ZonedDateTime[SIZE];
		
		ZonedDateTime timestamp = ZonedDateTime.now().plusMonths(1);
		ComissionDto dto;
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < SIZE; i++) {
			placeholders[0][i] = builder
					.append(placeholder)
					.append(i)
					.toString();
			createStudentService.createStudent(new StudentDto(
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
			createTeacherService.createTeacher(new TeacherDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
					placeholders[1][i] + "@mail.ru",
					placeholders[1][i],
					placeholders[1][i],
					placeholder));
			timestamp = timestamp.minusDays(i);
			timestamps[i] = timestamp;
			dto = new ComissionDto(
					timestamp,
					timestamp.plusHours(2),
					true,
					(i % 2 == 1) ? placeholder + "1234" : placeholder,
					"Аудитория №7",
					(short)10);
			createComissionService.createComission(dto);
			builder.delete(0, builder.length());
		}
		comissions = comissionRepo.findAll();
		for (int i = 0; i < SIZE - 1; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationDto(placeholders[0][i], 
										placeholders[0][i], 
										comissions.get(0).getId()));
		}
		for (int i = 0, k = 0; i < SIZE; i++, k++) {
			for (int j = 0; j < SIZE - k; j++) {
				createRegistrationService.createTeacherRegistration(
						new RegistrationDto(placeholders[1][j], 
											placeholders[1][j], 
											comissions.get(i).getId()));
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
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		authorizationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
	}
	
	@Test
	public void getCurrentComissionOfStudent_Success() throws Exception {
		
		assertDoesNotThrow(() -> readRegistrationService
				.getCurrentComissionOfStudent(new AuthenticationDto(
						placeholders[0][0], placeholders[0][0])));
		
		CurrentComissionOfStudentDto dto = readRegistrationService
				.getCurrentComissionOfStudent(new AuthenticationDto(
						placeholders[0][0], placeholders[0][0]));
		
		assertTrue(comissions.get(0).getStudyDirection().equals(
				dto.getStudyDirection()));
		assertTrue(comissions.get(0).getStartDateTime().toLocalDate().equals(
				dto.getDate()));
		assertTrue(comissions.get(0).getStartDateTime().toLocalTime().equals(
				dto.getStartTime()));
		assertTrue(comissions.get(0).getEndDateTime().toLocalTime().equals(
				dto.getEndTime()));
		assertTrue(comissions.get(0).getLocation().equals(
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
		assertDoesNotThrow(() -> readRegistrationService
				.getActualComissionsListForStudent(new AuthenticationDto(
						placeholders[0][0], placeholders[0][0])));
		
		List<ActualComissionForStudentDto> dtoList = readRegistrationService
				.getActualComissionsListForStudent(new AuthenticationDto(
						placeholders[0][0], placeholders[0][0]));
		
		int i = SIZE / 2 + SIZE % 2, j = 0;
		
		assertTrue(dtoList.size() == i);
		
		List<TeacherEntry> teachers;
		String[] s = {"Иванов С.А. Казаков В.М. ", 
				"Бурлаков И.С. Иванов С.А. Игнатьев А.П. Казаков В.М. "};
		StringBuilder builder;
		
		for (ActualComissionForStudentDto dto : dtoList) {
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
		
		CurrentComissionOfStudentDto dto = null;
		
		try {
			dto = readRegistrationService
					.getCurrentComissionOfStudent(new AuthenticationDto(
							placeholders[0][SIZE - 1], 
							placeholders[0][SIZE - 1]));
		} catch (EntityNotFoundException e) {
			assertEquals(e.getMessage(), "This student is not "
					+ "registered for any commission");
			assertNull(dto);
		}
		assertNull(dto);
	}
}
