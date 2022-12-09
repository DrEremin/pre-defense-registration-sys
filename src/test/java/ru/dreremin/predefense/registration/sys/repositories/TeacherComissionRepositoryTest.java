package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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
import ru.dreremin.predefense.registration.sys.dto.requestdto.ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.TeacherDto;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Teacher;
import ru.dreremin.predefense.registration.sys.models.TeacherComission;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateComissionService;
import ru.dreremin.predefense.registration.sys.services.teachers.CreateTeacherService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherComissionRepositoryTest {
	
	@Autowired TeacherComissionRepository teacherComissionRepo;
	
	@Autowired private CreateComissionService comissionService;
	
	@Autowired private CreateTeacherService teacherService;
	
	@Autowired private EmailRepository emailRepo;
	
	@Autowired private ActorRepository authorizationRepo;
	
	@Autowired private ComissionRepository comissionRepo;
	
	@Autowired private PersonRepository personRepo;
	
	@Autowired private TeacherRepository teacherRepo;
	
	private List<Comission> comissions;
	
	private List<Teacher> teachers;
	
	private Instant time;
	
	private String s = "placeholder";
	
	private String[] emails = {"1@mail.ru", "2@mail.ru", "3@mail.ru"};
	
	private String[] logins = {"1", "2", "3"};
	
	@BeforeAll
	public void beforeAll() throws Exception {
		
		ComissionDto comissionDto = new ComissionDto(
				ZonedDateTime.parse(
						"2022-08-03T10:15:30+03:00[Europe/Moscow]"),
				ZonedDateTime.parse(
						"2022-08-03T12:15:30+03:00[Europe/Moscow]"),
				true,
				s,
				s,
				(short)2);
		
		for (int i = 0; i < 2; i++) {
			teacherService.createTeacher(new TeacherDto(
					s, s, s, emails[i], logins[i], s, s));
			comissionService.createComission(comissionDto);
		}
		
		teachers = teacherRepo.findAll();
		comissions = comissionRepo.findAll();
		
		for(Teacher teacher : teachers) {
			teacherComissionRepo.save(
					new TeacherComission(
							teacher.getId(), comissions.get(0).getId()));
		}
		
		teacherService.createTeacher(new TeacherDto(
				s, s, s, emails[2], logins[2], s, s));
		teachers = teacherRepo.findAll();
		for (int i = 0; i < 2; i++) {
			teacherComissionRepo.save(new TeacherComission(
					teachers.get(2).getId(), comissions.get(i).getId()));
		}
	}

	@BeforeEach
	public void beforeEach() { time = Instant.now(); }
	
	@AfterEach
	public void afterEach() {
		log.info("testing time: " + Duration.between(time, Instant.now()));
	}
	
	@AfterAll
	public void afterAll() {
		emailRepo.deleteAll();
        authorizationRepo.deleteAll();
		personRepo.deleteAll();
		comissionRepo.deleteAll();
		teacherRepo.deleteAll();
		teacherComissionRepo.deleteAll();
	}

	
	@Test
	void findByComissionId_Success() {
		assertTrue(teacherComissionRepo.findByComissionId(
				comissions.get(0).getId()).size() == 3);
	}
	
	@Test
	void findByComissionId_IdDoesNotExists() {
		assertTrue(teacherComissionRepo.findByComissionId(
				comissions.get(0).getId() + 2).size() == 0);
	}
	
	@Test
	void findByTeacherId_Success() {
		assertTrue(teacherComissionRepo.findByTeacherId(
				teachers.get(0).getId()).size() == 1);
		assertTrue(teacherComissionRepo.findByTeacherId(
				teachers.get(2).getId()).size() == 2);
	}
	
	@Test
	void findByTeacherId_IdDoesNotExists() {
		assertTrue(teacherComissionRepo.findByTeacherId(
				teachers.get(0).getId() + 10).size() == 0);
	}
	
	@Test
	void findByTeacherIdAndComissionId_Success() {
		Optional<TeacherComission> opt = 
				teacherComissionRepo.findByTeacherIdAndComissionId(
						teachers.get(2).getId(), 
						comissions.get(0).getId());
		assertTrue(opt.isPresent());
		assertEquals(teachers.get(2).getId(), opt.get().getTeacherId());
		assertEquals(comissions.get(0).getId(), opt.get().getComissionId());
	}
	
	@Test
	void findByTeacherIdAndComissionId_TeacherIdDoesNotExists() {
		Optional<TeacherComission> opt = 
				teacherComissionRepo.findByTeacherIdAndComissionId(
						teachers.get(2).getId() + 10, 
						comissions.get(0).getId());
		assertTrue(opt.isEmpty());
	}
	
	@Test
	void findByTeacherIdAndComissionId_ComissionIdDoesNotExists() {
		Optional<TeacherComission> opt = 
				teacherComissionRepo.findByTeacherIdAndComissionId(
						teachers.get(2).getId(), 
						comissions.get(0).getId() + 10);
		assertTrue(opt.isEmpty());
	}
}
