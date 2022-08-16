package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "_authorization")
public class Authorization {
	
	@Id
	@Column(length = 20)
	private String login;
	
	@Column(name = "pswrd", length = 20)
	private String password;
	
	@Column(name = "id_p_aufk", length = 20)
	private long personId;
	
	public Authorization() {}
}
 