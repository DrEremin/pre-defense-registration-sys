package ru.dreremin.predefense.registration.sys.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperForListResponseDto<T> {
	
	@JsonProperty(value = "listResponseDto")
	private final List<T> listResponseDto;
	
	public WrapperForListResponseDto(List<T> listResponseDto) {
		this.listResponseDto = listResponseDto;
	}
}
