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

import lombok.extern.slf4j.Slf4j;
import ru.dreremin.predefense.registration.sys.exceptions.NegativeTimePeriodException;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateComissionControllerTest {
	
	@Autowired private ComissionRepository repository;
	
	@Autowired private MockMvc mockMvc;
	
	private String startDateTimeString;
	
	private String endDateTimeString;
	
	private String presenceFormatString;
	
	private String studyDirectionString;
	
	private String locationString;
	
	private String json;
	
	private Instant time;
	
	@BeforeAll void beforeAll() {
		this.startDateTimeString = "\t\"startDateTime\" : "
				+ "\"2022-08-03T10:15:30+03:00[Europe/Moscow]\",\n";
		this.endDateTimeString = "\t\"endDateTime\" : "
				+ "\"2022-08-03T12:15:30+03:00[Europe/Moscow]\",\n";
		this.presenceFormatString = "\t\"presenceFormat\" : true,\n";
		this.studyDirectionString = "\t\"studyDirection\" : \"ПИ\",\n";
		this.locationString = "\t\"location\" : \"Аудитория №7\",\n";
	}
	
	private String createJson() {
		return new StringBuilder("{\n")
				.append(this.startDateTimeString)
				.append(this.endDateTimeString)
				.append(this.presenceFormatString)
				.append(this.studyDirectionString)
				.append(this.locationString)
				.append("\t\"studentLimit\" : 10\n")
				.append("}")
				.toString();
	}
	
	@BeforeEach void beforeEach() throws Exception {
		this.json = createJson();
		this.time = Instant.now();
	}
	
	@AfterEach
	void afterEach() {
		repository.deleteAll();
		log.info("run time: " + Duration.between(this.time, Instant.now()));
	}
	
	@Test
	void CreateComission_Success() throws Exception {
		mockMvc.perform(put("/comission-create")
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
		this.json = this.json.replace(this.endDateTimeString, "");
		this.mockMvc.perform(put("/comission-create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json)
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
		this.json = this.json.substring(0, json.length() - 2);
		this.mockMvc.perform(put("/comission-create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json)
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
		this.json = this.json.replace("endDateTime", "startDateTime");
		this.json = this.json.replaceFirst("startDateTime", 
										   "endDateTime");
		this.mockMvc.perform(put("/comission-create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json)
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
