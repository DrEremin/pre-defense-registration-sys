package ru.dreremin.predefense.registration.sys.factories;

import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl
		 .ComissionDto;
import ru.dreremin.predefense.registration.sys.dto.requestdto.impl.StudentDto;
import ru.dreremin.predefense.registration.sys.models.Authentication;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.Person;
import ru.dreremin.predefense.registration.sys.models.Student;

public class EntitiesFactory {
	
	public static Person createPerson(PersonDto dto) {
		
		return new Person(dto.getLastName(), 
						  dto.getFirstName(), 
						  dto.getPatronymic());
	}
	
	public static Authentication createAuthorization(PersonDto dto, 
													long personId) {
		return new Authentication(dto.getLogin(), dto.getPassword(), personId);
	}
	
	public static Student createStudent(StudentDto dto, long personId) {
		
		return new Student(personId, 
						   dto.getGroupNumber(), 
						   dto.getStudyDirection(), 
						   dto.getStudyType());
	}
	
	public static Comission createComission(ComissionDto dto) {
		return new Comission(dto.getStartTimestamp(), 
							 dto.getEndTimestamp(), 
							 dto.getPresenceFormat(), 
							 dto.getStudyDirection(), 
							 dto.getLocation(),
							 dto.getStudentLimit());
	}
}
