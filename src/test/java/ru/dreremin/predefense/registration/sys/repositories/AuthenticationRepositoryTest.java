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

import ru.dreremin.predefense.registration.sys.models.Authentication;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationRepositoryTest {

	@Autowired
	private AuthenticationRepository repo;
	private String login, password;
	private long personId;
	private Instant time;
	
	@BeforeAll
	void beforeAll() {
		login = "login";
		password = "password";
		personId = 1;
		repo.save(new Authentication(login, password, personId));
	}
	
	@BeforeEach
    void beforeEach() { time = Instant.now(); }

	@AfterAll
	void afterAll() { repo.deleteById(login); }
	
    @AfterEach
    void afterEach() {
        log.info("testing time: " + Duration.between(time, Instant.now()));
    }
    
	@Test
	void findByLogin_Success() {
		assertTrue(repo.findByLogin(login).isPresent());
	}
	
	@Test
	void findByLogin_DoesNotExists() {
		assertFalse(repo.findByLogin("123").isPresent());
	}
}

