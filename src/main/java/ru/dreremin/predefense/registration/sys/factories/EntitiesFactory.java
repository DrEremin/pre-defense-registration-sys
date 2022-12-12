package ru.dreremin.predefense.registration.sys.factories;

import ru.dreremin.predefense.registration.sys.dto.requestdto.CommissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;

public class EntitiesFactory {
	
	public static Person createPerson(PersonDto dto, long actorId) {
		return new Person(dto.getLastName(), 
						  dto.getFirstName(), 
						  dto.getPatronymic(),
						  actorId);
	}
	
	public static Student createStudent(StudentDto dto, long personId) {
		return new Student(personId, 
						   dto.getGroupNumber(), 
						   dto.getStudyDirection(), 
						   dto.getStudyType());
	}
	
	public static Commission createComission(CommissionDto dto) {
		return new Commission(dto.getStartTimestamp(), 
							 dto.getEndTimestamp(), 
							 dto.getPresenceFormat(), 
							 dto.getStudyDirection(), 
							 dto.getLocation(),
							 dto.getStudentLimit());
	}
}
