package ru.dreremin.predefense.registration.sys.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Getter
public class TeacherEntry implements Comparable<TeacherEntry>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long teacherComissionId;
	
	@Column
	private String fullName;
	
	public TeacherEntry() {}
	
	public TeacherEntry(long id, 
						String lastName, 
						String firstName, 
						String patronymic) {
		this.teacherComissionId = id;
		this.fullName = new StringBuilder(lastName)
				.append(" ")
				.append(firstName.substring(0, 1))
				.append(".")
				.append(patronymic.substring(0, 1))
				.append(".")
				.toString();
	}
	
	@Override
	public int compareTo(TeacherEntry other) {
		return this.fullName.compareTo(other.fullName);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("{")
				.append(teacherComissionId)
				.append(" ")
				.append(fullName)
				.append("}")
				.toString(); 
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		TeacherEntry o = (TeacherEntry) other;
		return this.teacherComissionId == o.getTeacherComissionId() 
				&& this.fullName.equals(o.getFullName());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(teacherComissionId, fullName);
	}
}
