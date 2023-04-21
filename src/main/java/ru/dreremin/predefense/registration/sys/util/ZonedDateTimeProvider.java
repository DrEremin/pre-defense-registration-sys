package ru.dreremin.predefense.registration.sys.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.dreremin.predefense.registration.sys.exceptions
		 .NegativeTimePeriodException;

@Component
public class ZonedDateTimeProvider {

	@Value("${spring.zone}")
	private String zone;
	
	public ZonedDateTime changeTimeZone(ZonedDateTime zonedDateTime) {
		return ZonedDateTime.of(
				zonedDateTime.toLocalDateTime(), 
				ZoneId.of(zone));
	}
	
	public void periodValidation(
			ZonedDateTime startDateTime, 
			ZonedDateTime endDateTime) throws NegativeTimePeriodException {
		if (endDateTime.toLocalDateTime().compareTo(
				startDateTime.toLocalDateTime()) <= 0) {
			throw new NegativeTimePeriodException(
					"The end date-time is earlier than start date-time");
		}
	} 
	
	public String convertToString(ZonedDateTime zonedDateTime) {
		
		String stringZonedDateTime = ZonedDateTime.of(
				zonedDateTime.toLocalDateTime(), 
				ZoneId.of("UTC+00:00"))
						.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		return stringZonedDateTime.substring(
				0, stringZonedDateTime.length() - 1) + "+00:00";
	}
	
	
	
	public ZonedDateTime parseFromString(String zonedDateTime) {
		return ZonedDateTime.parse(
				zonedDateTime, 
				DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}
}
