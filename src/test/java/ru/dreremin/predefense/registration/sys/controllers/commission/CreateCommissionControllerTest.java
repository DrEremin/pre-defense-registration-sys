package ru.dreremin.predefense.registration.sys.controllers.commission;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.dto.request.AdministratorRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.AuthenticationRequestDto;
import ru.dreremin.predefense.registration.sys.exceptions.NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.repositories.AdministratorRepository;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.services.admin.CreateAdministratorService;
import ru.dreremin.predefense.registration.sys.services.admin.DeleteAdministratorService;
import ru.dreremin.predefense.registration.sys.services.auth.AuthenticationService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateCommissionControllerTest {

	@Autowired private CommissionRepository commissionRepository;
	
	@Autowired private CreateAdministratorService createAdministratorService;
	
	@Autowired private DeleteAdministratorService deleteAdministratorService;
	
	@Autowired private AuthenticationService authenticationService;
	
	@Autowired private AdministratorRepository administratorRepository;
	
	@Autowired private MockMvc mockMvc;
	
	private String startDateTimeString;
	
	private String endDateTimeString;
	
	private String presenceFormatString;
	
	private String studyDirectionString;
	
	private String locationString;
	
	private String json;
	
	private String token;
	
	private Instant time;
	
	@BeforeAll void beforeAll() throws Exception {
		startDateTimeString = "\t\"startDateTime\" : "
				+ "\"2022-08-03T10:15:30+03:00[Europe/Moscow]\",\n";
		endDateTimeString = "\t\"endDateTime\" : "
				+ "\"2022-08-03T12:15:30+03:00[Europe/Moscow]\",\n";
		presenceFormatString = "\t\"presenceFormat\" : true,\n";
		studyDirectionString = "\t\"studyDirection\" : \"ПИ\",\n";
		locationString = "\t\"location\" : \"Аудитория №7\",\n";
		createAdministratorService.createAdmin(
				new AdministratorRequestDto("admin", "123"));
		token = "Bearer_" + authenticationService.getToken(
				new AuthenticationRequestDto("admin", "123"));
	}
	
	private String createJson() {
		return new StringBuilder("{\n")
				.append(startDateTimeString)
				.append(endDateTimeString)
				.append(presenceFormatString)
				.append(studyDirectionString)
				.append(locationString)
				.append("\t\"studentLimit\" : 10\n")
				.append("}")
				.toString();
	}
	
	@BeforeEach void beforeEach() throws Exception {
		json = createJson();
		time = Instant.now();
	}
	
	@AfterEach
	void afterEach() {
		commissionRepository.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	void afterAll() {
		deleteAdministratorService.deleteAdminById(
				administratorRepository.findAll().get(0).getActorId());
	}
	
	@Test
	void CreateComission_Success() throws Exception {
		mockMvc.perform(post("/commission")
						.header("Authorization", token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Ok")))
				.andExpect(r -> assertNull(r.getResolvedException()));
	}
	
	@Test
	void CreateComission_InvalidFormatRequestBodyField() throws Exception {
		json = json.replace(endDateTimeString, "");
		mockMvc.perform(post("/commission")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.message", 
							is("Invalid format request body field")))
		.andExpect(r -> assertInstanceOf(
				MethodArgumentNotValidException.class, 
				r.getResolvedException()));
	}
	 
	@Test
	void CreateComission_InvalidFormatRequestBody() throws Exception {
		json = json.substring(0, json.length() - 2);
		mockMvc.perform(post("/commission")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.message", is("Failed to read request body")))
		.andExpect(r -> assertInstanceOf(
				HttpMessageNotReadableException.class, 
				r.getResolvedException()));
	}
	
	@Test
	void CreateComission_NegativeTimePeriod() throws Exception {
		json = json.replace("endDateTime", "startDateTime");
		json = json.replaceFirst("startDateTime", 
										   "endDateTime");
		mockMvc.perform(post("/commission")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.message", 
				is("The end date-time is earlier than start date-time")))
		.andExpect(r -> assertInstanceOf(
				NegativeTimePeriodException.class, 
				r.getResolvedException()));
	}
}
