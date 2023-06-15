package ru.dreremin.predefense.registration.sys.services.commission;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import ru.dreremin.predefense.registration.sys.repositories
		 .CommissionRepository;
import ru.dreremin.predefense.registration.sys.services.commission.CreateCommissionService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateCommissionServiceTest {

	@Autowired
	private CreateCommissionService service;
	
	@Autowired
	private CommissionRepository repository;
	
	private CommissionRequestDto dto;
	
	private Instant time;

	@BeforeAll
	void beforeAll() {
		dto = new CommissionRequestDto(
				ZonedDateTime.parse("2022-08-03T10:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				ZonedDateTime.parse("2022-08-03T12:15:30+03:00[Europe/Moscow]", 
						DateTimeFormatter.ISO_ZONED_DATE_TIME),
				"ПИ",
				"Аудитория №7",
				(short)2);
	}
	
	@BeforeEach
	void beforeEach() { time = Instant.now(); }
	
	@AfterEach
	void afterEach() {
		repository.deleteAll();
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@Test
	void createComission_Success() {
		assertDoesNotThrow(() -> service.createComission(dto));
		assertTrue(repository.findAll().size() == 1);
	}
}
