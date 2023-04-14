package ru.dreremin.predefense.registration.sys.services.teacher;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.dreremin.predefense.registration.sys.dto.request.TeacherRequestDto;
import ru.dreremin.predefense.registration.sys.repositories.TeacherRepository;

@RequiredArgsConstructor
@Service
public class UpdateTeacherService {

	private final TeacherRepository teacherRepository;
	
	public void updateTeacher(TeacherRequestDto dto) {
		
	}
}
