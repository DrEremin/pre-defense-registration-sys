package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class RegistrationDto {

	@JsonProperty(value = "comissionId")
	@NotNull
	@Min(1)
	@Max(Integer.MAX_VALUE)
	private final Integer comissionId;
	
	@JsonCreator
	public RegistrationDto(Integer comissionId) {
		this.comissionId = comissionId;
	}
}
