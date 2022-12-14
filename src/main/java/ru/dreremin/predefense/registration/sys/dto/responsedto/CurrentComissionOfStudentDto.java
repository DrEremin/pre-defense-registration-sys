package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;

@RequiredArgsConstructor
public class CurrentComissionOfStudentDto implements Serializable {

	@JsonProperty(value = "studyDirection")
	private final String studyDirection;
	
	@JsonProperty(value = "date")
	private final LocalDate date;
	
	@JsonProperty(value = "startTime")
	private final LocalTime startTime;
	
	@JsonProperty(value = "endTime")
	private final LocalTime endTime;
	
	@JsonProperty(value = "location")
	private final String location;
	
	@JsonProperty(value = "students")
	private final List<StudentEntry> students;
	
	public CurrentComissionOfStudentDto(Comission comission,
								  List<StudentEntry> students) {
		this.studyDirection = comission.getStudyDirection();
		this.date = comission.getStartDateTime().toLocalDate();
		this.startTime = comission.getStartDateTime().toLocalTime();
		this.endTime = comission.getEndDateTime().toLocalTime();
		this.location = comission.getLocation();
		this.students = students;
	}
	
	public String getStudyDirection() { return studyDirection; }
	
	public LocalDate getDate() { return date; }
	
	public LocalTime getStartTime() { return startTime; }
	
	public LocalTime getEndTime() { return endTime; }
	
	public String getLocation() { return location; }
	
	public List<StudentEntry> getStudents() { return List.copyOf(students); }
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) { 
			return false;
		}
		CurrentComissionOfStudentDto o = (CurrentComissionOfStudentDto) other;
		return this.studyDirection.equals(o.getStudyDirection()) 
				&& this.date.equals(o.getDate())
				&& this.startTime.equals(o.getStartTime())
				&& this.endTime.equals(o.getEndTime())
				&& this.location.equals(o.getLocation())
				&& this.students.equals(o.getStudents());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(studyDirection, 
							date, 
							startTime, 
							endTime, 
							location, 
							students);
	}
}
