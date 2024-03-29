package ru.dreremin.predefense.registration.sys.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.response.StatusResponseDto;

@RequiredArgsConstructor
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
	
	private final ObjectMapper mapper;
	
	@Override
	public void handle(
			HttpServletRequest request, 
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) 
					throws IOException, ServletException {
		
	response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		StatusResponseDto dto = new StatusResponseDto(
				403, 
				"User is not authorized");
		response.getWriter().print(mapper.writeValueAsString(dto));
		
	}
	
}
