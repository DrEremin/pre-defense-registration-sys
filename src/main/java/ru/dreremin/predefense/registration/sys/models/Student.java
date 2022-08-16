package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "student")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_s")
	private long id;
	
	@Column(name = "id_p_sfk")
	private long personId;
	
	@Column(name = "group_num", length = 10)
	private String groupNumber;
	
	@Column(name = "study_direction_s")
	private String studyDirection;
	
	@Column(name = "study_type", length = 20)
	private String studyType;
	
	@Column
	private short grade;
	
	public Student() {}
	
	public Student(long personId, 
				   String groupNumber, 
				   String studyDirection, 
				   String studyType) {
		this.personId = personId;
		this.groupNumber = groupNumber;
		this.studyDirection = studyDirection;
		this.studyType = studyType;
		this.grade = 1;
	}
}
