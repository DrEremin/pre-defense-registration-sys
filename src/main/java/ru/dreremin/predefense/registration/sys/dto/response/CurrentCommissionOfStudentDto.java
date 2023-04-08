package ru.dreremin.predefense.registration.sys.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.StudentEntry;

@Getter
@RequiredArgsConstructor
public class CurrentCommissionOfStudentDto implements Serializable {

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
	
	public CurrentCommissionOfStudentDto(Commission commission,
								  List<StudentEntry> students) {
		this.studyDirection = commission.getStudyDirection();
		this.date = commission.getStartDateTime().toLocalDate();
		this.startTime = commission.getStartDateTime().toLocalTime();
		this.endTime = commission.getEndDateTime().toLocalTime();
		this.location = commission.getLocation();
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
		CurrentCommissionOfStudentDto o = (CurrentCommissionOfStudentDto) other;
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
