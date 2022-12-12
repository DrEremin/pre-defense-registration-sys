package ru.dreremin.predefense.registration.sys.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

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
import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.StudentCommission;
import ru.dreremin.predefense.registration.sys.services.comissions
		 .CreateCommissionService;
import ru.dreremin.predefense.registration.sys.services.students
		 .CreateStudentService;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentCommissionRepositoryTest {

	@Autowired
	private StudentCommissionRepository studentComissionRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private CommissionRepository comissionRepo;
	
	@Autowired
	private EmailRepository emailRepo;
	
	@Autowired
	private ActorRepository authorizationRepo;
	
	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private CreateStudentService studentService;
	
	@Autowired
	private CreateCommissionService comissionService;
	
	private StudentDto studentDto;
	
	private CommissionDto commissionDto;
	
	private Commission commission;
	
	private Instant time;
	
	/** 
	 * Creation of three students and one commission 
	 */
	
	@BeforeAll
	public void beforeAll() throws Exception{
		
		String s = "xxx";
		String[] emails = {"1@mail.ru", "2@mail.ru", "3@mail.ru"};
		String[] logins = {"1", "2", "3"};
		
		for (int i = 0; i < 3; i++) {
			studentDto = new StudentDto(
					s, s, s, emails[i], logins[i], s, s, s, s);
			studentService.createStudent(studentDto);
		}
		
		List<Student> students = studentRepo.findAll();
		
		commissionDto = new CommissionDto(
				ZonedDateTime.parse(
						"2022-08-03T10:15:30+03:00[Europe/Moscow]"),
				ZonedDateTime.parse(
						"2022-08-03T12:15:30+03:00[Europe/Moscow]"),
				true,
				s,
				s,
				(short)10);
		comissionService.createComission(commissionDto);
		
		commission = comissionRepo.findAll().get(0);
		
		for(Student student : students) {
			studentComissionRepo.save(
					new StudentCommission(
							student.getId(), commission.getId()));
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
		studentRepo.deleteAll();
		studentComissionRepo.deleteAll();
	}
	
	@Test
	void findByComissionId_Success() {
		assertTrue(studentComissionRepo.findByComissionId(
				commission.getId()).size() == 3);
	}
	
	@Test
	void findByComissionId_IdDoesNotExists() {
		assertTrue(studentComissionRepo.findByComissionId(
				commission.getId() + 1).size() == 0);
	}
}
