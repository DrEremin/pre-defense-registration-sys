package ru.dreremin.predefense.registration.sys.services.note;

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
import ru.dreremin.predefense.registration.sys.dto.request.CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.NoteRequestDto;
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.repositories.NoteRepository;
import ru.dreremin.predefense.registration.sys.services.commission.CreateCommissionService;
import ru.dreremin.predefense.registration.sys.services.note.CreateNoteService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateNoteServiceTest {

	@Autowired private CreateNoteService noteService;

	@Autowired private CreateCommissionService comissionService;
	
	@Autowired private NoteRepository noteRepo;
	
	@Autowired private CommissionRepository comissionRepo;
	
	private Instant time;
	
	private int comissionId;
	
	private String str;
	
	@BeforeAll
	void beforeAll() {
		str = "placeholder";
		comissionService.createComission(new CommissionRequestDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
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
		assertDoesNotThrow(() -> noteService.createNote(
				comissionId, new NoteRequestDto(str)));
		assertTrue(noteRepo.count() == 1);
	}
	
	@Test
	void createNote_ComissionIdDoesNotExists() {
		try {
			noteService.createNote(comissionId + 10, new NoteRequestDto(str));
		} catch (EntityNotFoundException e) {
			assertInstanceOf(EntityNotFoundException.class, e);
			assertEquals(
					"Commission with this ID does not exists", 
					e.getMessage());
		}
		assertTrue(noteRepo.count() == 0);
	}
}
