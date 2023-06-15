package ru.dreremin.predefense.registration.sys.controllers.commission;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request
				 .MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
				 .content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
				 .jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
				 .status;

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
import org.springframework.boot.test.autoconfigure.web.servlet
		  .AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.AdministratorRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherCommission;
import ru.dreremin.predefense.registration.sys.repositories
		 .ActorRepository;
import ru.dreremin.predefense.registration.sys.repositories.AdministratorRepository;
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
import ru.dreremin.predefense.registration.sys.services.admin.CreateAdministratorService;
import ru.dreremin.predefense.registration.sys.services.auth.AuthenticationService;
import ru.dreremin.predefense.registration.sys.services.commission.CreateCommissionService;
import ru.dreremin.predefense.registration.sys.services.registration.CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher.CreateTeacherService;
import ru.dreremin.predefense.registration.sys.util.ZonedDateTimeProvider;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadCommissionControllerTest {
	
	@Autowired private AuthenticationService authenticationService;
	
	@Autowired private CreateAdministratorService createAdministratorService;
	
	@Autowired private CreateStudentService createStudentService;
	
	@Autowired private CreateTeacherService createTeacherService;
	
	@Autowired private CreateCommissionService createCommissionService; 
	
	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private CommissionRepository comissionRepo;
	
	@Autowired private StudentCommissionRepository studentCommissionRepo;
	
	@Autowired private TeacherCommissionRepository teacherCommissionRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	@Autowired private ActorRepository actorRepo;
	
	@Autowired private AdministratorRepository administratorRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private MockMvc mockMvc;
	
	@Autowired private ObjectMapper mapper;
	
	@Autowired private ZonedDateTimeProvider zonedDateTimeProvider; 
	
	private String administratorToken, studentToken, teacherToken;
	
	private Instant time;
	
	private String[] lastNames =
		{ "Казаков", "Иванов", "Игнатьев", "Бурлаков" };
	
	private String[] firstNames = 
		{ "Всеволод", "Сергей", "Андрей", "Илья" };
	
	private String[] patronymics = 
		{ "Михайлович", "Александрович", "Петрович", "Сергеевич" };
	
	private String[][] emails;
	
	private final String[][] logins = {
			{ "kazakovStud", "ivanovStud", "ignatStud", "burlakStud" },
			{ "kazakovTeach", "ivanovTeach", "ignatTeach", "burlakTeach" }};
	
	private final String pswd = "passw1234";
	
	private ZonedDateTime[] timestamps;
	
	private List<Commission> commissions;
	
	private static final int SIZE = 4;
	
	@BeforeAll
	void beforeAll() throws Exception {
		
		emails = new String[2][SIZE];
		timestamps = new ZonedDateTime[SIZE + 1];
		
		ZonedDateTime timestamp = ZonedDateTime.now().plusMonths(1);
		CommissionRequestDto dto;
		
		createAdministratorService.createAdmin(
				new AdministratorRequestDto("admin", "123"));
		administratorToken = "Bearer_" + authenticationService.getToken(
				new AuthenticationRequestDto("admin", "123"));
		
		for (int i = 0; i < SIZE; i++) {
			emails[0][i] = logins[0][i] + "@mail.ru";
			emails[1][i] = logins[1][i] + "@mail.ru";
			createStudentService.createStudent(new StudentRequestDto(
					logins[0][i],
					pswd,
					lastNames[i],
					firstNames[i],
					patronymics[i],
					emails[0][i],
					"ПИ",
					"очное",
					"ЗИ98" + i));
			createTeacherService.createTeacher(new TeacherRequestDto(
					logins[1][i],
					pswd,
					lastNames[i],
					firstNames[i],
					patronymics[i],
					emails[1][i],
					"Преподаватель"));
			timestamp = timestamp.minusDays(i);
			timestamps[i] = timestamp;
			dto = new CommissionRequestDto(
					timestamp,
					timestamp.plusHours(2),
					(i % 2 == 1) ? "ИСИТ" : "ПИ",
					"Аудитория №" + i,
					(short)10);
			createCommissionService.createComission(dto);
		}
		timestamps[SIZE] = ZonedDateTime.now();
		dto = new CommissionRequestDto(
				timestamps[SIZE],
				timestamps[SIZE].plusHours(2),
				"ПИ",
				"Аудитория №7",
				(short)10);
		createCommissionService.createComission(dto);
		commissions = comissionRepo.findAll();
		
		List<Student> students = studentRepo.findAll();
		List<Teacher> teachers = teacherRepo.findAll();
		
		for (int i = 0; i < SIZE - 1; i++) {
			studentCommissionRepo.save(new StudentCommission(
					students.get(i).getId(), 
					commissions.get(0).getId()));
			teacherCommissionRepo.save(new TeacherCommission(
					teachers.get(i).getId(), 
					commissions.get(1).getId()));
		}
	}
	
	@BeforeEach
	void beforeEach() {
		time = Instant.now();
	}
	
	@AfterEach
	void afterEach() {
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	void afterAll() {
		studentCommissionRepo.deleteAll();
		teacherCommissionRepo.deleteAll();
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		teacherRepo.deleteAll();
		administratorRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
		actorRepo.deleteAll();
	}
	
	@Test
	void getCommissionsListByTimePeriod_Success() throws Exception {
		
		mockMvc.perform(get(
						"/commission/list?page=0&amountOf"
						+ "ItemsOnPage=6&startDateTime=" 
						+ zonedDateTimeProvider.convertToString(ZonedDateTime.now()) 
						+ "&endDateTime=" 
						+ zonedDateTimeProvider.convertToString(
								ZonedDateTime.now().plusMonths(1)))
						.header("Authorization", administratorToken))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items.length()", is(4)))
				.andExpect(jsonPath("$.items[0].studyDirection", is("ИСИТ")))
				.andExpect(jsonPath("$.items[0].location", is("Аудитория №3")))
				.andExpect(jsonPath("$.items[1].studyDirection", is("ПИ")))
				.andExpect(jsonPath("$.items[1].location", is("Аудитория №2")))
				.andExpect(jsonPath("$.items[2].studyDirection", is("ИСИТ")))
				.andExpect(jsonPath("$.items[2].location", is("Аудитория №1")))
				.andExpect(jsonPath("$.items[3].studyDirection", is("ПИ")))
				.andExpect(jsonPath("$.items[3].location", is("Аудитория №0")))
				.andExpect(jsonPath("$.page", is(0)))
				.andExpect(jsonPath("$.totalAmountOfItems", is(4)))
				.andExpect(jsonPath("$.amountOfItemsOnPage", is(4)));
	}
	/*
	@Test
	void getCurrentComissionOfStudent_Success() throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.studyDirection", is("ПИ")))
				.andExpect(jsonPath("$.date", is(timestamps[0]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$.startTime", is(timestamps[0]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$.endTime", is(timestamps[0]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$.location", is("Аудитория №7")))
				.andExpect(jsonPath("$.students[0].fullName", 
						is("Иванов Сергей Александрович")))
				.andExpect(jsonPath("$.students[0].groupNumber", is("ЗИ981")))
				.andExpect(jsonPath("$.students[1].fullName", 
						is("Игнатьев Андрей Петрович")))
				.andExpect(jsonPath("$.students[1].groupNumber", is("ЗИ982")))
				.andExpect(jsonPath("$.students[2].fullName", 
						is("Казаков Всеволод Михайлович")))
				.andExpect(jsonPath("$.students[2].groupNumber", is("ЗИ980")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}

	@Test
	void getActualComissionsListForStudent_Success() throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(commissions.get(2).getId())))
				.andExpect(jsonPath("$[0].date", is(timestamps[2]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[0].startTime", is(timestamps[2]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[0].endTime", is(timestamps[2]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[0].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[0].teachers[0].fullName", 
						is("Иванов С.А.")))
				.andExpect(jsonPath("$[0].teachers[1].fullName", 
						is("Казаков В.М.")))
				.andExpect(jsonPath("$[1].id", is(commissions.get(0).getId())))
				.andExpect(jsonPath("$[1].date", is(timestamps[0]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[1].startTime", is(timestamps[0]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[1].endTime", is(timestamps[0]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[1].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[1].teachers[0].fullName", 
						is("Бурлаков И.С.")))
				.andExpect(jsonPath("$[1].teachers[1].fullName", 
						is("Иванов С.А.")))
				.andExpect(jsonPath("$[1].teachers[2].fullName", 
						is("Игнатьев А.П.")))
				.andExpect(jsonPath("$[1].teachers[3].fullName", 
						is("Казаков В.М.")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_Success() throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[1][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[3].id", is(commissions.get(0).getId())))
				.andExpect(jsonPath("$[3].date", is(timestamps[0]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[3].startTime", is(timestamps[0]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[3].endTime", is(timestamps[0]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[3].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[3].teachers[0].fullName", 
						is("Бурлаков И.С.")))
				.andExpect(jsonPath("$[3].teachers[1].fullName", 
						is("Иванов С.А.")))
				.andExpect(jsonPath("$[3].teachers[2].fullName", 
						is("Игнатьев А.П.")))
				.andExpect(jsonPath("$[3].teachers[3].fullName", 
						is("Казаков В.М.")))
				.andExpect(jsonPath("$[3].note", is("")))
				.andExpect(jsonPath("$[2].id", is(commissions.get(1).getId())))
				.andExpect(jsonPath("$[2].date", is(timestamps[1]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[2].startTime", is(timestamps[1]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[2].endTime", is(timestamps[1]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[2].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[2].teachers[0].fullName", 
						is("Иванов С.А.")))
				.andExpect(jsonPath("$[2].teachers[1].fullName", 
						is("Игнатьев А.П.")))
				.andExpect(jsonPath("$[2].teachers[2].fullName", 
						is("Казаков В.М.")))
				.andExpect(jsonPath("$[2].note", is("")))
				
				.andExpect(jsonPath("$[1].id", is(commissions.get(2).getId())))
				.andExpect(jsonPath("$[1].date", is(timestamps[2]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[1].startTime", is(timestamps[2]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[1].endTime", is(timestamps[2]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[1].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[1].teachers[0].fullName", 
						is("Иванов С.А.")))
				.andExpect(jsonPath("$[1].teachers[1].fullName", 
						is("Казаков В.М.")))
				.andExpect(jsonPath("$[1].note", is("")))
				.andExpect(jsonPath("$[0].id", is(commissions.get(3).getId())))
				.andExpect(jsonPath("$[0].date", is(timestamps[3]
						.toLocalDate()
						.format(DateTimeFormatter.ISO_LOCAL_DATE))))
				.andExpect(jsonPath("$[0].startTime", is(timestamps[3]
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[0].endTime", is(timestamps[3]
						.plusHours(2)
						.toLocalTime()
						.format(DateTimeFormatter.ISO_LOCAL_TIME))))
				.andExpect(jsonPath("$[0].location", is("Аудитория №7")))
				.andExpect(jsonPath("$[0].teachers[0].fullName", 
						is("Казаков В.М.")))
				.andExpect(jsonPath("$[0].note", is("")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}
	
	@Test
	void getCurrentComissionOfStudent_StudentDontRegistered() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("This student is not "
								+ "registered for any commission")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getCurrentComissionOfStudent_StudentDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[1][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists student with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForStudent_StudentDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[1][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists student with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_TeacherDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists teacher with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getCurrentComissionOfStudent_PersonDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists person with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForStudent_PersonDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists person with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_PersonDoesNotExists() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists person with this login")))
				.andExpect(r -> assertInstanceOf(EntityNotFoundException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getCurrentComissionOfStudent_InvalidPassword() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("Сlient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForStudent_InvalidPassword() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[0][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("Сlient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_InvalidPassword() 
			throws Exception {
		
		AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
				logins[1][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationRequestDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("Сlient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
	*/
}
