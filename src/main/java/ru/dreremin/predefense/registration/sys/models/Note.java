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
@Table(name = "note")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_n")
	private long id;
	
	@Column(name = "id_c_nfk")
	private int commissionId;
	
	@Column(name = "note_content")
	private String noteContent;
	
	public Note() {}
	
	public Note(int commissionId, String noteContent) {
		this.commissionId = commissionId;
		this.noteContent = noteContent;
	}
}
