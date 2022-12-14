package ru.dreremin.predefense.registration.sys.controllers.read;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.students
		 .CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teachers
		 .CreateTeacherService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadComissionControllerTest {
	
	@Autowired private CreateStudentService createStudentService;
	
	@Autowired private CreateTeacherService createTeacherService;
	
	@Autowired private CreateComissionService createComissionService; 
	
	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private StudentComissionRepository studentComissionRepo;
	
	@Autowired private TeacherComissionRepository teacherComissionRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	@Autowired private AuthenticationRepository authenticationRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private MockMvc mockMvc;
	
	@Autowired private ObjectMapper mapper;
	
	private Instant time;
	
	private String[] lastNames =
		{ "??????????????", "????????????", "????????????????", "????????????????" };
	
	private String[] firstNames = 
		{ "????????????????", "????????????", "????????????", "????????" };
	
	private String[] patronymics = 
		{ "????????????????????", "??????????????????????????", "????????????????", "??????????????????" };
	
	private String[][] emails;
	
	private final String[][] logins = {
			{ "kazakovStud", "ivanovStud", "ignatStud", "burlakStud" },
			{ "kazakovTeach", "ivanovTeach", "ignatTeach", "burlakTeach" }};
	
	private final String pswd = "passw1234";
	
	private ZonedDateTime[] timestamps;
	
	private List<Comission> comissions;
	
	private static final int SIZE = 4;
	
	@BeforeAll
	void beforeAll() throws Exception {
		
		emails = new String[2][SIZE];
		timestamps = new ZonedDateTime[SIZE];
		
		ZonedDateTime timestamp = ZonedDateTime.now().plusMonths(1);
		ComissionDto dto;
		
		for (int i = 0; i < SIZE; i++) {
			emails[0][i] = logins[0][i] + "@mail.ru";
			emails[1][i] = logins[1][i] + "@mail.ru";
			createStudentService.createStudent(new StudentDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
					emails[0][i],
					logins[0][i],
					pswd,
					"????",
					"??????????",
					"????98" + i));
			createTeacherService.createTeacher(new TeacherDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
					emails[1][i],
					logins[1][i],
					pswd,
					"??????????????????????????"));
			timestamp = timestamp.minusDays(i);
			timestamps[i] = timestamp;
			dto = new ComissionDto(
					timestamp,
					timestamp.plusHours(2),
					true,
					(i % 2 == 1) ? "????????" : "????",
					"?????????????????? ???7",
					(short)10);
			createComissionService.createComission(dto);
		}
		comissions = comissionRepo.findAll();
		for (int i = 0; i < SIZE - 1; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationDto(logins[0][i], 
										pswd, 
										comissions.get(0).getId()));
		}
		for (int i = 0, k = 0; i < SIZE; i++, k++) {
			for (int j = 0; j < SIZE - k; j++) {
				createRegistrationService.createTeacherRegistration(
						new RegistrationDto(logins[1][j], 
											pswd, 
											comissions.get(i).getId()));
			}
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
	void getCurrentComissionOfStudent_Success() throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.studyDirection", is("????")))
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
				.andExpect(jsonPath("$.location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$.students[0].fullName", 
						is("???????????? ???????????? ??????????????????????????")))
				.andExpect(jsonPath("$.students[0].groupNumber", is("????981")))
				.andExpect(jsonPath("$.students[1].fullName", 
						is("???????????????? ???????????? ????????????????")))
				.andExpect(jsonPath("$.students[1].groupNumber", is("????982")))
				.andExpect(jsonPath("$.students[2].fullName", 
						is("?????????????? ???????????????? ????????????????????")))
				.andExpect(jsonPath("$.students[2].groupNumber", is("????980")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}

	@Test
	void getActualComissionsListForStudent_Success() throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(comissions.get(2).getId())))
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
				.andExpect(jsonPath("$[0].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[0].teachers[0].fullName", 
						is("???????????? ??.??.")))
				.andExpect(jsonPath("$[0].teachers[1].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(jsonPath("$[1].id", is(comissions.get(0).getId())))
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
				.andExpect(jsonPath("$[1].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[1].teachers[0].fullName", 
						is("???????????????? ??.??.")))
				.andExpect(jsonPath("$[1].teachers[1].fullName", 
						is("???????????? ??.??.")))
				.andExpect(jsonPath("$[1].teachers[2].fullName", 
						is("???????????????? ??.??.")))
				.andExpect(jsonPath("$[1].teachers[3].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_Success() throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[1][1], 
				pswd);
		
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[3].id", is(comissions.get(0).getId())))
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
				.andExpect(jsonPath("$[3].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[3].teachers[0].fullName", 
						is("???????????????? ??.??.")))
				.andExpect(jsonPath("$[3].teachers[1].fullName", 
						is("???????????? ??.??.")))
				.andExpect(jsonPath("$[3].teachers[2].fullName", 
						is("???????????????? ??.??.")))
				.andExpect(jsonPath("$[3].teachers[3].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(jsonPath("$[3].note", is("")))
				.andExpect(jsonPath("$[2].id", is(comissions.get(1).getId())))
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
				.andExpect(jsonPath("$[2].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[2].teachers[0].fullName", 
						is("???????????? ??.??.")))
				.andExpect(jsonPath("$[2].teachers[1].fullName", 
						is("???????????????? ??.??.")))
				.andExpect(jsonPath("$[2].teachers[2].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(jsonPath("$[2].note", is("")))
				
				.andExpect(jsonPath("$[1].id", is(comissions.get(2).getId())))
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
				.andExpect(jsonPath("$[1].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[1].teachers[0].fullName", 
						is("???????????? ??.??.")))
				.andExpect(jsonPath("$[1].teachers[1].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(jsonPath("$[1].note", is("")))
				.andExpect(jsonPath("$[0].id", is(comissions.get(3).getId())))
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
				.andExpect(jsonPath("$[0].location", is("?????????????????? ???7")))
				.andExpect(jsonPath("$[0].teachers[0].fullName", 
						is("?????????????? ??.??.")))
				.andExpect(jsonPath("$[0].note", is("")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}
	
	@Test
	void getCurrentComissionOfStudent_StudentDontRegistered() 
			throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[1][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[1][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][SIZE - 1], 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				"other login", 
				pswd);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
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
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/current/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("??lient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForStudent_InvalidPassword() 
			throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[0][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/actual/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("??lient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
	
	@Test
	void getActualComissionsListForTeacher_InvalidPassword() 
			throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				logins[1][SIZE - 1], 
				"other password");
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/actual/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("??lient is not authenticated")))
				.andExpect(r -> assertInstanceOf(
						FailedAuthenticationException.class, 
						r.getResolvedException()));
	}
}
