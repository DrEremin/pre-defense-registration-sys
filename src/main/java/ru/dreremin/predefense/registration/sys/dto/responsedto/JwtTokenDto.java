package ru.dreremin.predefense.registration.sys.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtTokenDto {
	
	@JsonProperty("jwtToken")
	private final String jwtToken;
}
