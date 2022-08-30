package ru.dreremin.predefense.registration.sys.services.notes;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.NoteDto;
import ru.dreremin.predefense.registration.sys.repositories
		 .ComissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateComissionService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateNoteServiceTest {
	
	@Autowired private CreateNoteService noteService;

	@Autowired private CreateComissionService comissionService;
	
	@Autowired private NoteRepository noteRepo;
	
	@Autowired private ComissionRepository comissionRepo;
	
	private Instant time;
	
	private int comissionId;
	
	private String str;
	
	@BeforeAll
	void beforeAll() {
		str = "placeholder";
		comissionService.createComission(new ComissionDto(
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
	void beforeEach() { time = Instant.now(); }
	
	@AfterEach
	void afterEach() {
		noteRepo.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	void afterAll() { comissionRepo.deleteAll(); }
	
	@Test
	void createNote_Success() {
		assertDoesNotThrow(
				() -> noteService.createNote(new NoteDto(comissionId, str)));
		assertTrue(noteRepo.count() == 1);
	}
	
	@Test
	void createNote_ComissionIdDoesNotExists() {
		try {
			noteService.createNote(new NoteDto(comissionId + 10, str));
		} catch (EntityNotFoundException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals(
					"There is not exists comission with this Id", 
					e.getMessage());
		}
		assertTrue(noteRepo.count() == 0);
	}
}
