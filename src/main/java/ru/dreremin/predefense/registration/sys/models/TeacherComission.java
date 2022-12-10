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
@Table(name = "teacher_comission")
public class TeacherComission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tc")
	private long id;
	
	@Column(name = "id_t_tcfk")
	private int teacherId;
	
	@Column(name = "id_c_tcfk")
	private int comissionId;
	
	public TeacherComission() {}
	
	public TeacherComission(int teacherId, int comissionId) {
		this.teacherId = teacherId;
		this.comissionId = comissionId;
	}
}
