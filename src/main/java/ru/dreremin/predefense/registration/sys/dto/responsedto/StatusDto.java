package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatusDto implements Serializable {
	
	@JsonProperty(value = "status")
	private final int status;
	
	@JsonProperty(value = "message")
	private final String message;
}
