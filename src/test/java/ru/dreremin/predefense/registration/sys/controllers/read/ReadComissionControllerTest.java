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
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateComissionService;
import ru.dreremin.predefense.registration.sys.services.registrations
		 .CreateRegistrationService;
import ru.dreremin.predefense.registration.sys.services.students
		 .CreateStudentService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadComissionControllerTest {
	
	@Autowired private CreateStudentService createStudentService; 
	
	@Autowired private CreateComissionService createComissionService; 
	
	@Autowired private CreateRegistrationService createRegistrationService;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private StudentComissionRepository studentComissionRepo;
	
	@Autowired private StudentRepository studentRepo;
	
	@Autowired private AuthenticationRepository authenticationRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private MockMvc mockMvc;
	
	@Autowired private ObjectMapper mapper;
	
	private Instant time;
	
	private String placeholder;
	
	private String[] lastNames = 
		{ "Казаков", "Иванов", "Игнатьев", "Бурлаков" };
	
	private String[] firstNames = 
		{ "Всеволод", "Сергей", "Андрей", "Илья" };
	
	private String[] patronymics = 
		{ "Михайлович", "Александрович", "Петрович", "Сергеевич" };
	
	private String[] placeholders;
	
	private List<Comission> comissions;
	
	private static final int SIZE;
	
	static {
		SIZE = 4;
	}
	
	@BeforeAll
	void beforeAll() throws Exception {
		
		placeholder = "placeholder";
		placeholders = new String[SIZE];
		ComissionDto dto = new ComissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				placeholder,
				"Аудитория №7",
				(short)10);
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < SIZE; i++) {
			placeholders[i] = builder.append(placeholder).append(i).toString();
			createStudentService.createStudent(new StudentDto(
					lastNames[i],
					firstNames[i],
					patronymics[i],
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
		comissionRepo.deleteAll();
		studentRepo.deleteAll();
		authenticationRepo.deleteAll();
		emailRepo.deleteAll();
		personRepo.deleteAll();
	}

	@Test
	void getComissionForStudent_Success() throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				placeholders[1], 
				placeholders[1]);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.studyDirection", is("placeholder")))
				.andExpect(jsonPath("$.date", is("2022-08-03")))
				.andExpect(jsonPath("$.startTime", is("10:15:30")))
				.andExpect(jsonPath("$.endTime", is("12:15:30")))
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
	void getComissionForStudent_StudentDontRegistered() throws Exception {
		
		AuthenticationDto authenticationDto = new AuthenticationDto(
				placeholders[SIZE - 1], 
				placeholders[SIZE - 1]);
		String requestBody = mapper.writeValueAsString(authenticationDto);
		
		mockMvc.perform(post("/comissions-read/student")
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
}
