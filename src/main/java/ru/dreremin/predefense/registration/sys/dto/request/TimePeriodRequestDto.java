package ru.dreremin.predefense.registration.sys.dto.request;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.dreremin.predefense.registration.sys.exceptions.NegativeTimePeriodException;

@Getter
@Setter
@AllArgsConstructor
public class TimePeriodRequestDto {
	
	@JsonProperty(value = "startDateTime")
	@NotNull
	private ZonedDateTime startDateTime;
	
	@JsonProperty(value = "endDateTime")
	@NotNull
	private ZonedDateTime endDateTime;
	
	public void periodValidation() throws NegativeTimePeriodException {
		if (this.endDateTime.toLocalDateTime().compareTo(
				this.startDateTime.toLocalDateTime()) < 0) {
			throw new NegativeTimePeriodException(
					"The end date-time is earlier than start date-time");
		}
		
	}
}
