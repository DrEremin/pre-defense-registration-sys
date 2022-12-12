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
import ru.dreremin.predefense.registration.sys.dto.requestdto.NoteDto;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateCommissionService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateNoteControllerTest {
	
	@Autowired private MockMvc mockMvc;
	
	@Autowired private CreateCommissionService comissionService;
	
	@Autowired private NoteRepository noteRepo;
	
	@Autowired private CommissionRepository comissionRepo;
	
	@Autowired private ObjectMapper mapper;

	private Instant time;
	
	private int comissionId;
	
	private String str;
	
	@BeforeAll
	void beforeAll() throws Exception {
		str = "placeholder";
		comissionService.createComission(new CommissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				str,
				str,
				(short)2));
		comissionId = comissionRepo.findAll().get(0).getId();
	}
	
	@BeforeEach
    void beforeEach() { this.time = Instant.now(); }
	
	@AfterEach
    void afterEach() {
		noteRepo.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	void afterAll() { comissionRepo.deleteAll(); }
	
	@Test
	void createNote_Success() throws Exception {
		mockMvc.perform(put("/create-note")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(new NoteDto(comissionId, str)))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("Ok")))
				.andExpect(r -> assertNull(r.getResolvedException()));
		assertTrue(noteRepo.count() == 1);
	}
	
	@Test
	void createNote_RequestBodyIsMissingField() throws Exception {
		
		String json = mapper.writeValueAsString(new NoteDto(comissionId, str))
				.replace("\"comissionId\":" + comissionId + ",", "");
		
		mockMvc.perform(put("/create-note")
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
		assertTrue(noteRepo.count() == 0);
	}

	@Test
	void createNote_invalidRequestBodySyntax() throws Exception {
		
		String json = mapper.writeValueAsString(new NoteDto(comissionId, str))
				.replaceFirst(",", "");
		
		mockMvc.perform(put("/create-note")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.message", 
						is("Failed to read request body")))
				.andExpect(r -> assertInstanceOf(
						HttpMessageNotReadableException.class, 
						r.getResolvedException()));
		assertTrue(noteRepo.count() == 0);
	}
	
	@Test
	void createNote_ComissionDoesNotExist() throws Exception {
		
		mockMvc.perform(put("/create-note")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(
								new NoteDto(comissionId + 10, str)))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(409)))
				.andExpect(jsonPath("$.message", 
						is("There is not exists comission with this Id")))
				.andExpect(r -> assertInstanceOf(
						EntityNotFoundException.class, 
						r.getResolvedException()));
		assertTrue(noteRepo.count() == 0);
	}
}
