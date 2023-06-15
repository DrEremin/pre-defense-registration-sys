package ru.dreremin.predefense.registration.sys.controllers.registration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request
				 .MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result
				 .MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result
				 .MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result
				 .MockMvcResultMatchers.status;

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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.CommissionRequestDto;
//import ru.dreremin.predefense.registration.sys.dto.request.RegistrationRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
//import ru.dreremin.predefense.registration.sys.dto.request.impl.AuthenticationDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.models.Commission;
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
import ru.dreremin.predefense.registration.sys.services.registration.CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.student.CreateStudentService;
import ru.dreremin.predefense.registration.sys.services.teacher.CreateTeacherService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteRegistrationControllerTest {
/*
	@Autowired private CreateRegistrationService createRegistrationService;

	@Autowired private MockMvc mockMvc;
	
	@Autowired private CreateTeacherService teacherService;
	
	@Autowired private CreateStudentService studentService;
	
	@Autowired private CreateCommissionService comissionService;
	
	@Autowired private CommissionRepository comissionRepo;
	
	@Autowired private ActorRepository authorizationRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired StudentCommissionRepository studentComissionRepo;
	
	@Autowired TeacherCommissionRepository teacherComissionRepo;
	
	@Autowired private ObjectMapper objectMapper;
	
	private Instant time;
	
	private StudentRequestDto studentRequestDto;
	
	private TeacherRequestDto teacherRequestDto;
	
	private CommissionRequestDto commissionRequestDto;
	
	private List<Commission> commissions;
	
	private String s;
	
	private String[] emails, logins;
	
	@BeforeAll
	void beforeAll() throws Exception {
		
		final int LENGTH = 3;
		StringBuilder builder = new StringBuilder();
		
		s = "plholder";
		emails = new String[LENGTH << 1];
		logins = new String[LENGTH << 1];
		for (int i = 0; i < LENGTH << 1; i++) {
			logins[i] = builder.append("person").append(i + 1).toString();
			emails[i] = builder.append("@mail.ru").toString();
			builder = builder.delete(0, builder.length());
		}
		commissionRequestDto = new CommissionRequestDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				s,
				"Аудитория №7",
				(short)2);
		for (int i = 0; i < LENGTH; i++) {
			studentRequestDto = new StudentRequestDto(
					s, s, s, emails[i], logins[i], s, s, s, s);
			teacherRequestDto = new TeacherRequestDto(
					s, s, s, emails[i + 3], logins[i + 3], s, s);
			studentService.createStudent(studentRequestDto);
			teacherService.createTeacher(teacherRequestDto);
			comissionService.createComission(commissionRequestDto);
		}
		commissions = comissionRepo.findAll();
	}
	
	@BeforeEach
    void beforeEach() throws Exception {  
		time = Instant.now();
		for (int i = 0; i < 3; i++) {
			createRegistrationService.createStudentRegistration(
					new RegistrationRequestDto(
							logins[i], s, commissions.get(i).getId()));
			createRegistrationService.createTeacherRegistration(
					new RegistrationRequestDto(
							logins[i + 3], s, commissions.get(i).getId()));
		} 
	}
	
	@AfterEach
    void afterEach() {
		studentComissionRepo.deleteAll();
		teacherComissionRepo.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	void afterAll() {
		authorizationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
		teacherRepo.deleteAll();
		studentRepo.deleteAll();
		comissionRepo.deleteAll();
	}
	
	@Test
	void deleteStudentRegistration_Success() throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto(logins[0], s);
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(200)))
					.andExpect(jsonPath("$.message", is("Ok"))));
		assertTrue(studentComissionRepo.count() == 2);
	}
	
	@Test
	void deleteTeacherRegistration_Success() throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], s, commissions.get(0).getId());
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(200)))
					.andExpect(jsonPath("$.message", is("Ok"))));
		assertTrue(teacherComissionRepo.count() == 2);
	}
	
	@Test
	void deleteStudentRegistration_RequestBodyIsMissingField() 
			throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto(logins[0], s);
		String json = objectMapper.writeValueAsString(dto)
				.replace("\"personLogin\":\"" + logins[0] + "\",", "");
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(400)))
					.andExpect(jsonPath("$.message", 
							is("Invalid format request body field")))
					.andExpect(r -> assertInstanceOf(
							MethodArgumentNotValidException.class, 
							r.getResolvedException())));
		assertTrue(studentComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_RequestBodyIsMissingField() 
			throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], s, commissions.get(0).getId());
		String json = objectMapper.writeValueAsString(dto)
				.replace("\"personLogin\":\"" + logins[3] + "\",", "");
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(400)))
					.andExpect(jsonPath("$.message", 
							is("Invalid format request body field")))
					.andExpect(r -> assertInstanceOf(
							MethodArgumentNotValidException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteStudentRegistration_InvalidRequestBodySyntax() 
			throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto(logins[0], s);
		String json = objectMapper.writeValueAsString(dto)
				.replaceFirst("\\{", "");
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(400)))
					.andExpect(jsonPath("$.message", 
							is("Failed to read request body")))
					.andExpect(r -> assertInstanceOf(
							HttpMessageNotReadableException.class, 
							r.getResolvedException())));
		assertTrue(studentComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_InvalidRequestBodySyntax() 
			throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], s, commissions.get(0).getId());
		String json = objectMapper.writeValueAsString(dto)
				.replaceFirst("\\{", "");
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(400)))
					.andExpect(jsonPath("$.message", 
							is("Failed to read request body")))
					.andExpect(r -> assertInstanceOf(
							HttpMessageNotReadableException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteStudentRegistration_PersonDoesNotExists() throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto("non-existent login", s);
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("There is not exists person with this login")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(studentComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_PersonDoesNotExists() throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				"non-existent login", s, commissions.get(0).getId());
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("There is not exists person with this login")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteStudentRegistration_PasswordDoesNotMatchLogin() 
			throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto(logins[0], 
													"other password");
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("Сlient is not authenticated")))
					.andExpect(r -> assertInstanceOf(
							FailedAuthenticationException.class, 
							r.getResolvedException())));
		assertTrue(studentComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_PasswordDoesNotMatchLogin() 
			throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], "other password", commissions.get(0).getId());
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("Сlient is not authenticated")))
					.andExpect(r -> assertInstanceOf(
							FailedAuthenticationException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteStudentRegistration_StudentDoesNotExist() throws Exception {
		
		AuthenticationRequestDto dto = new AuthenticationRequestDto(logins[3], s);
		
		assertTrue(studentComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("There is not exists student with this login")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(studentComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_TeacherDoesNotExist() throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[0], s, commissions.get(0).getId());
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("There is not exists teacher with this login")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_ComissionDoesNotExist() throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], s, commissions.get(0).getId() + 10);
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("There is not exists comission with this Id")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}
	
	@Test
	void deleteTeacherRegistration_DoesNotSuchRegistration() throws Exception {
		
		RegistrationRequestDto dto = new RegistrationRequestDto(
				logins[3], s, commissions.get(1).getId());
		
		assertTrue(teacherComissionRepo.count() == 3);
		assertDoesNotThrow(() -> 
			mockMvc.perform(delete("/registration-delete/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(409)))
					.andExpect(jsonPath("$.message", 
							is("The registration for this teacher "
									+ "for such comission does not exist")))
					.andExpect(r -> assertInstanceOf(
							EntityNotFoundException.class, 
							r.getResolvedException())));
		assertTrue(teacherComissionRepo.count() == 3);
	}*/
}
