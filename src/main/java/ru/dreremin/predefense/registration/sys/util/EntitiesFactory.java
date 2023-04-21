package ru.dreremin.predefense.registration.sys.util;

import ru.dreremin.predefense.registration.sys.dto.request
		 .CommissionRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.PersonRequestDto;
import ru.dreremin.predefense.registration.sys.dto.request.StudentRequestDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;

public class EntitiesFactory {
	
	public static Person createPerson(PersonRequestDto dto, long actorId) {
		return new Person(
				dto.getLastName(), 
				dto.getFirstName(), 
				dto.getPatronymic(),
				actorId);
	}
	
	public static Student createStudent(StudentRequestDto dto, long personId) {
		return new Student(
				personId, 
				dto.getGroup(), 
				dto.getStudyDirection(), 
				dto.getStudyType());
	}
	
	public static Commission createCommission(CommissionRequestDto dto) {
		return new Commission(
							 dto.getStartDateTime(), 
							 dto.getEndDateTime(), 
							 dto.getPresenceFormat(), 
							 dto.getStudyDirection(), 
							 dto.getLocation(),
							 dto.getStudentLimit());
	}
}
