package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "teacher_commission")
public class TeacherCommission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tc")
	private long id;
	
	@Column(name = "id_t_tcfk")
	private int teacherId;
	
	@Column(name = "id_c_tcfk")
	private int commissionId;
	
	public TeacherCommission() {}
	
	public TeacherCommission(int teacherId, int commissionId) {
		this.teacherId = teacherId;
		this.commissionId = commissionId;
	}
}
