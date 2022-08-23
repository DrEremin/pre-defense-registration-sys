package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudentDto implements Serializable, Comparable<StudentDto> {

	private final String lastName;
	
	private final String firstName;
	
	private final String patronymic;
	
	private final String groupNumber;
	
	@Override public int compareTo(StudentDto other) {
		return createFullName(lastName, firstName, patronymic)
				.compareTo(createFullName(other.getLastName(), 
						   other.getFirstName(), other.getPatronymic()));
	}
	
	private String createFullName(String s1, String s2, String s3) {
		return new StringBuilder(s1)
				.append(" ")
				.append(s2)
				.append(" ")
				.append(s3)
				.toString();
	}
	
	@Override public boolean equals(Object other) {
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		StudentDto o = (StudentDto) other;
		return lastName.equals(o.getLastName()) 
				&& firstName.equals(o.getFirstName()) 
				&& patronymic.equals(o.getPatronymic()) 
				&& groupNumber.equals(o.getGroupNumber());
	}
	
	@Override public int hashCode() {
		return Objects.hash(lastName, firstName, patronymic, groupNumber);
	}
}
