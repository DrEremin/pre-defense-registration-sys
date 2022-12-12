package ru.dreremin.predefense.registration.sys.controllers.create;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request
				 .MockMvcRequestBuilders.put;
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
import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.RegistrationDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.exceptions
		 .EntitiesMismatchException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .FailedAuthenticationException;
import ru.dreremin.predefense.registration.sys.exceptions.OverLimitException;
import ru.dreremin.predefense.registration.sys.exceptions
		 .UniquenessViolationException;
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
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateCommissionService;
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
class CreateRegistrationControllerTest {
	
	@Autowired private CreateRegistrationService registrationService;

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
	
	private StudentDto studentDto;
	
	private TeacherDto teacherDto;
	
	private CommissionDto commissionDto;
	
	private Commission commission;
	
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
		for (int i = 0; i < LENGTH; i++) {
			studentDto = new StudentDto(
					s, s, s, emails[i], logins[i], s, s, s, s);
			teacherDto = new TeacherDto(
					s, s, s, emails[i + 3], logins[i + 3], s, s);
			studentService.createStudent(studentDto);
			teacherService.createTeacher(teacherDto);
		}
		commissionDto = new CommissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				s,
				"Аудитория №7",
				(short)2);
		comissionService.createComission(commissionDto);
		commission = comissionRepo.findAll().get(0);
	}
	
	@BeforeEach
    void beforeEach() { this.time = Instant.now(); }
	
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
	void studentRegistration_Success() throws Exception {
		
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId());
		
		assertDoesNotThrow(() -> 
			mockMvc.perform(put("/registration/student")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(200)))
					.andExpect(jsonPath("$.message", is("Ok"))));
		assertTrue(studentComissionRepo.count() == 1);
	}
	
	@Test
	void teacherRegistration_Success() throws Exception {
		
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId());
		
		assertDoesNotThrow(() -> 
			mockMvc.perform(put("/registration/teacher")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(dto))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status", is(200)))
					.andExpect(jsonPath("$.message", is("Ok"))));
		assertTrue(teacherComissionRepo.count() == 1);
	}
	
	@Test
	void studentRegistration_RequestBodyIsMissingField() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId());
		String json = objectMapper.writeValueAsString(dto)
				.replace("\"personLogin\":\"" + logins[0] + "\",", "");
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacherRegistration_RequestBodyIsMissingField() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId());
		String json = objectMapper.writeValueAsString(dto)
				.replace("\"personLogin\":\"" + logins[3] + "\",", "");
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void studentRegistration_invalidRequestBodySyntax() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId());
		String json = objectMapper.writeValueAsString(dto)
				.replaceFirst("\\{", "");
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacherRegistration_invalidRequestBodySyntax() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId());
		String json = objectMapper.writeValueAsString(dto)
				.replaceFirst("\\{", "");
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}

	@Test
	void studentRegistration_PersonDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				"non-existent login", s, commission.getId());
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacherRegistration_PersonDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				"non-existent login", s, commission.getId());
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void studentRegistration_PasswordDoesNotMatchLogin() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], "other password", commission.getId());
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacerRegistration_PasswordDoesNotMatchLogin() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], "other password", commission.getId());
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void studentRegistration_StudentDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId());
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacherRegistration_TeacherDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId());
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void studentRegistration_ComissionDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId() + 100);
		
		mockMvc.perform(put("/registration/student")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void teacherRegistration_ComissionDoesNotExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId() + 100);
		
		mockMvc.perform(put("/registration/teacher")
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
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
	}
	
	@Test
	void studentRegistration_MismatchedStudyDirections() throws Exception {
	
		CommissionDto otherComissionDto = new CommissionDto(
				ZonedDateTime.parse("2022-08-03T10:45:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:45:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				"other study direction",
				"Аудитория №7",
				(short)2);
		comissionService.createComission(otherComissionDto);
		Commission otherComission = comissionRepo.findAll().get(1);
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, otherComission.getId());
		
		mockMvc.perform(put("/registration/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("The study direction of the commission and the"
								+ " student do not correspond to each other")))
				.andExpect(r -> assertInstanceOf(
						EntitiesMismatchException.class, 
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 0);
		comissionRepo.delete(otherComission);
	}
	
	@Test
	void studentRegistration_RecordingOverLimit() throws Exception {
	
		RegistrationDto dto;
		
		for (int i = 0; i < 2; i++) {
			dto =  new RegistrationDto(logins[i], s, commission.getId());
			registrationService.createStudentRegistration(dto);
		}
		dto = new RegistrationDto(
				logins[2], s, commission.getId());
		mockMvc.perform(put("/registration/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("The limit of the allowed number of students"
								+ " in this commission has been reached")))
				.andExpect(r -> assertInstanceOf(
						OverLimitException.class, 
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 2);
	}
	
	@Test
	void studentRegistration_SuchRegistrationAlreadyExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[0], s, commission.getId());
		registrationService.createStudentRegistration(dto);
		mockMvc.perform(put("/registration/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("Such a student is already"
								+ " registered for this commission")))
				.andExpect(r -> assertInstanceOf(
						UniquenessViolationException.class, 
						r.getResolvedException()));
		assertTrue(studentComissionRepo.count() == 1);
	}
	
	@Test
	void teacherRegistration_SuchRegistrationAlreadyExist() throws Exception {
	
		RegistrationDto dto = new RegistrationDto(
				logins[3], s, commission.getId());
		registrationService.createTeacherRegistration(dto);
		mockMvc.perform(put("/registration/teacher")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(
							MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("Such a teacher is already"
								+ " registered for this commission")))
				.andExpect(r -> assertInstanceOf(
						UniquenessViolationException.class, 
						r.getResolvedException()));
		assertTrue(teacherComissionRepo.count() == 1);
	}
}
