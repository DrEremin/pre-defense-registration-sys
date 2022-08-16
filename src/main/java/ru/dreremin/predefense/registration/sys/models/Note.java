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
	private int comissionId;
	
	@Column(name = "note_content")
	private String noteContent;
	
	public Note() {}
	
	public Note(int comissionId, String noteContent) {
		this.comissionId = comissionId;
		this.noteContent = noteContent;
	}
}
