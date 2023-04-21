package ru.dreremin.predefense.registration.sys.dto.response;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperForPageResponseDto<T, E> {

	@JsonProperty(value = "items")
	private final List<E> items;
	
	@JsonProperty(value = "page")
	private final int pageNumber;
	
	@JsonProperty(value = "numberOfItemsTotal")
	private final long numberOfItemsTotal;
	
	@JsonProperty(value = "numberOfItemsPerPage")
	private final int numberOfItemsPerPage;
	
	public WrapperForPageResponseDto(Map.Entry<Page<T>, List<E>> entry) {
		items = entry.getValue();
		pageNumber = entry.getKey().getNumber();
		numberOfItemsTotal = entry.getKey().getTotalElements();
		numberOfItemsPerPage = entry.getKey().getNumberOfElements();
	}
}
