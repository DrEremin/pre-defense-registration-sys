package ru.dreremin.predefense.registration.sys.util;

import ru.dreremin.predefense.registration.sys.dto.request.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.request.PersonDto;
import ru.dreremin.predefense.registration.sys.dto.request.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;

public class EntitiesFactory {
	
	public static Person createPerson(PersonDto dto, long actorId) {
		return new Person(
				dto.getLastName(), 
				dto.getFirstName(), 
				dto.getPatronymic(),
				actorId);
	}
	
	public static Student createStudent(StudentDto dto, long personId) {
		return new Student(
				personId, 
				dto.getGroupNumber(), 
				dto.getStudyDirection(), 
				dto.getStudyType());
	}
	
	public static Commission createCommission(CommissionDto dto) {
		return new Commission(
							 dto.getId(),
							 dto.getStartDateTime(), 
							 dto.getEndDateTime(), 
							 dto.getPresenceFormat(), 
							 dto.getStudyDirection(), 
							 dto.getLocation(),
							 dto.getStudentLimit());
	}
}
