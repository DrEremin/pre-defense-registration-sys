package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc
		  .AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc
		  .AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.predefense.registration.sys.models.Comission;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComissionRepositoryTest {

	@Autowired
	private ComissionRepository repository;
	
	private final int SIZE = 5;
	
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		
		ZonedDateTime timestamp;
		String s = "placeholder";
		Short l = Short.valueOf((short) 2);
		
		for (int i = 0; i < SIZE; i++) {
			timestamp = (i % 2 == 0) 
					? ZonedDateTime.now().plusMonths(1) 
					: ZonedDateTime.now().minusMonths(1);
				
			repository.save(new Comission(timestamp, 
										  timestamp.plusHours(1), 
										  true, 
										  s, 
										  s, 
										  l));
		}
	}
	
	@BeforeEach
	void beforeEach() {
		time = Instant.now();
	}
	
	@AfterEach
	void afterEach() {}
	
	@AfterAll
	void afterAll() {
		repository.deleteAll();
	}
	
	@Test
	void findByStartDateTimeGreaterThan_Success() {
		assertTrue(repository.count() == SIZE);
		assertDoesNotThrow(() -> repository.findByStartDateTimeGreaterThan(
				ZonedDateTime.now()));
		assertTrue(repository.findByStartDateTimeGreaterThan(
				ZonedDateTime.now()).size() == SIZE / 2 + SIZE % 2);
	}
}
