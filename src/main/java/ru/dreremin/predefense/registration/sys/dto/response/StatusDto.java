package ru.dreremin.predefense.registration.sys.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatusDto implements Serializable {
	
	@JsonProperty(value = "status")
	protected final int status;
	
	@JsonProperty(value = "message")
	protected final String message;
}
