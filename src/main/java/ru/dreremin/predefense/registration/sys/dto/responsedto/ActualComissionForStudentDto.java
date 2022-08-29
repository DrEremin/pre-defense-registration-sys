package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.models.Comission;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;

@RequiredArgsConstructor
public class ActualComissionForStudentDto implements Serializable {
	
	protected final LocalDate date;
	
	protected final LocalTime startTime;
	
	protected final LocalTime endTime;
	
	protected final String studyDirection;
	
	protected final String location;
	
	protected final List<TeacherEntry> teachers;
	
	public ActualComissionForStudentDto(Comission comission,
			  							List<TeacherEntry> teachers) {
		this.date = comission.getStartDateTime().toLocalDate();
		this.startTime = comission.getStartDateTime().toLocalTime();
		this.endTime = comission.getEndDateTime().toLocalTime();
		this.studyDirection = comission.getStudyDirection();
		this.location = comission.getLocation();
		this.teachers = teachers;
	}
	
	public String getStudyDirection() { return studyDirection; }
	
	public LocalDate getDate() { return date; }
	
	public LocalTime getStartTime() { return startTime; }
	
	public LocalTime getEndTime() { return endTime; }
	
	public String getLocation() { return location; }
	
	public List<TeacherEntry> getTeachers() { return List.copyOf(teachers); }
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) { 
			return false;
		}
		ActualComissionForStudentDto o = (ActualComissionForStudentDto) other;
		return this.studyDirection.equals(o.getStudyDirection()) 
				&& this.date.equals(o.getDate())
				&& this.startTime.equals(o.getStartTime())
				&& this.endTime.equals(o.getEndTime())
				&& this.location.equals(o.getLocation())
				&& this.teachers.equals(o.getTeachers());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(studyDirection, 
							date, 
							startTime, 
							endTime, 
							location, 
							teachers);
	}
}
