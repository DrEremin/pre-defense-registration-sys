package ru.dreremin.predefense.registration.sys.models;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "commission")
public class Commission implements Comparable<Commission>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_c")
	private int id;
	
	@Column(name = "start_datetime_c")
	private ZonedDateTime startDateTime;
	
	@Column(name = "end_datetime_c")
	private ZonedDateTime endDateTime;
	
	@Column(name = "study_direction_c")
	private String studyDirection;
	
	@Column(name = "location_c")
	private String location;
	
	@Column(name = "student_limit")
	private short studentLimit;
	
	public Commission() {}
	
	public Commission(ZonedDateTime startDateTime,
					 ZonedDateTime endDateTime,
					 String studyDirection,
					 String location,
					 Short studentLimit) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.studyDirection = studyDirection;
		this.location = location;
		this.studentLimit = studentLimit;
	}
	
	public Commission(
			int id,
			ZonedDateTime startDateTime,
			ZonedDateTime endDateTime,
			String studyDirection,
			String location,
			Short studentLimit) {
		this.id = id;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.studyDirection = studyDirection;
		this.location = location;
		this.studentLimit = studentLimit;
	}
	
	@Override
	public int compareTo(Commission other) {
		return this.startDateTime.toLocalDateTime().compareTo(
				other.getStartDateTime().toLocalDateTime());
	}
}
