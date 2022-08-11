package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatusDto implements Serializable {
	
	private final int status;
	private final String message;
}
