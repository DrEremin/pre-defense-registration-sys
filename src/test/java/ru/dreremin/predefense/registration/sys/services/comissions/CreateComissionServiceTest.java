package ru.dreremin.predefense.registration.sys.services.comissions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.ComissionDto;
import ru.dreremin.predefense.registration.sys.repositories.ComissionRepository;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateComissionServiceTest {
	
	@Autowired
	private CreateComissionService service;
	
	@Autowired
	private ComissionRepository repository;
	
	private ComissionDto dto;
	
	private Instant time;

	@BeforeAll
	void beforeAll() {
		this.dto = new ComissionDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				true,
				"ПИ",
				"Аудитория №7");
	}
	
	@BeforeEach
	void beforeEach() {
		time = Instant.now();  
	}
	
	@AfterEach
	void afterEach() {
		repository.deleteAll();
		log.info("run time: " + Duration.between(this.time, Instant.now()));
	}
	
	@Test
	void createComission_Success() {
		assertDoesNotThrow(() -> this.service.createComission(this.dto));
		assertTrue(repository.findAll().size() == 1);
	}
}
