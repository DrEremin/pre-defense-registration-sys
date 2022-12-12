package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;

@RequiredArgsConstructor
public class ActualCommissionForStudentDto implements Serializable {
	
	@JsonProperty(value = "id")
	protected final int id;
	
	@JsonProperty(value = "date")
	protected final LocalDate date;
	
	@JsonProperty(value = "startTime")
	protected final LocalTime startTime;
	
	@JsonProperty(value = "endTime")
	protected final LocalTime endTime;
	
	@JsonProperty(value = "studyDirection")
	protected final String studyDirection;
	
	@JsonProperty(value = "location")
	protected final String location;
	
	@JsonProperty(value = "teachers")
	protected final List<TeacherEntry> teachers;
	
	public ActualCommissionForStudentDto(Commission commission,
			  							List<TeacherEntry> teachers) {
		this.id = commission.getId();
		this.date = commission.getStartDateTime().toLocalDate();
		this.startTime = commission.getStartDateTime().toLocalTime();
		this.endTime = commission.getEndDateTime().toLocalTime();
		this.studyDirection = commission.getStudyDirection();
		this.location = commission.getLocation();
		this.teachers = teachers;
	}
	
	
	public int getId() { return id; }
	
	public LocalDate getDate() { return date; }
	
	public LocalTime getStartTime() { return startTime; }
	
	public LocalTime getEndTime() { return endTime; }
	
	public String getStudyDirection() { return studyDirection; }
	
	public String getLocation() { return location; }
	
	public List<TeacherEntry> getTeachers() { return List.copyOf(teachers); }
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) { 
			return false;
		}
		ActualCommissionForStudentDto o = (ActualCommissionForStudentDto) other;
		return this.studyDirection.equals(o.getStudyDirection()) 
				&& this.date.equals(o.getDate())
				&& this.startTime.equals(o.getStartTime())
				&& this.endTime.equals(o.getEndTime())
				&& this.location.equals(o.getLocation())
				&& this.teachers.equals(o.getTeachers())
				&& this.id == o.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id,
							studyDirection, 
							date, 
							startTime, 
							endTime, 
							location, 
							teachers);
	}
}
