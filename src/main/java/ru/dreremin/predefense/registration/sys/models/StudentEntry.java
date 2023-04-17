package ru.dreremin.predefense.registration.sys.models;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class StudentEntry implements Comparable<StudentEntry> {
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long studentComissionId;
	
	@JsonProperty(value = "fullName")
	@Column
	private String fullName;
	
	@JsonProperty(value = "groupNumber")
	@Column
	private String groupNumber;
	
	public StudentEntry() {}
	
	public StudentEntry(long id, 
						String lastName, 
						String firstName, 
						String patronymic, 
						String groupNumber) {
		this.studentComissionId = id;
		this.fullName = new StringBuilder(lastName)
				.append(" ")
				.append(firstName)
				.append(" ")
				.append(patronymic)
				.toString();
		this.groupNumber = groupNumber;
	}
	
	@Override
	public int compareTo(StudentEntry other) {
		return this.fullName.compareTo(other.fullName);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("{")
				.append(studentComissionId)
				.append(" ")
				.append(fullName)
				.append(" ")
				.append(groupNumber)
				.append("}")
				.toString(); 
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		StudentEntry o = (StudentEntry) other;
		return this.studentComissionId == o.getStudentComissionId() 
				&& this.fullName.equals(o.getFullName())
				&& this.groupNumber.equals(o.getGroupNumber());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(studentComissionId, fullName, groupNumber);
	}
}
