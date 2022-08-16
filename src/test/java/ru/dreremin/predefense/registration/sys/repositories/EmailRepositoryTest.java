package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;

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

import ru.dreremin.predefense.registration.sys.models.Email;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailRepositoryTest {

	@Autowired
	private EmailRepository repo;
	private String box;
	private long personId;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		box = "box@mail";
		personId = 1;
		repo.save(new Email(box, personId));
	}
	@BeforeEach
    void beforeEach() { time = Instant.now(); }

    @AfterEach
    void afterEach() {
        log.info("testing time: " + Duration.between(time, Instant.now()));
    }
    
    @AfterAll
    void afterAll() { repo.deleteById(box); }
    
	@Test
	void findByBox_Success() {
		assertTrue(repo.findByBox(box).isPresent());
	}
	
	@Test
	void findByBox_DoesNotExists() {
		assertFalse(repo.findByBox(box + box).isPresent());
	}
}
