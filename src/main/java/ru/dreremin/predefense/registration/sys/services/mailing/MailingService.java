package ru.dreremin.predefense.registration.sys.services.mailing;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.dto.request.MailingRequestDto;
import ru.dreremin.predefense.registration.sys.dto.response.MailingResponseDto;
import ru.dreremin.predefense.registration.sys.dto.response
		 .WrapperForListResponseDto;
import ru.dreremin.predefense.registration.sys.models.Email;
import ru.dreremin.predefense.registration.sys.repositories.EmailRepository;

@RequiredArgsConstructor
@Service
public class MailingService {

	private final JavaMailSender emailSender;
	
	private final EmailRepository emailRepo;
	
	@Value("${spring.mail.sender.email}")
	private String fromAddress;
	
	private MailingResponseDto sendEmail(
			String toAddress, 
			String subject, 
			String content) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		int status = 0;	
		String message = null;
		
		simpleMailMessage.setFrom(fromAddress);
		simpleMailMessage.setTo(toAddress);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		try {
			emailSender.send(simpleMailMessage);
			status = 200;
			message = "Ok";
		} catch (MailAuthenticationException ea) {
			status = 500;
			message = "Unsuccessful authentication of the "
					+ "sender in the еmail account";
		} catch (MailSendException es) {
			status = 500;
			message = "Failed to send email to current address";
		} catch (MailParseException ep) {
			status = 500;
			message = "Failed to parse message";
		} finally {
			return new MailingResponseDto(status, message, toAddress);
		}
	}
	
	public WrapperForListResponseDto<MailingResponseDto> sendMailsToStudents(
			MailingRequestDto dto) {
		
		List<Email> addresses = emailRepo.findAllByStudent();
		List<MailingResponseDto> responseDto = 
				new ArrayList<>(addresses.size());
		
		for (Email email : addresses) {
			responseDto.add(sendEmail(
					email.getAddress(), 
					dto.getSubject(), 
					dto.getContent()));
		}
		return new WrapperForListResponseDto<>(responseDto);
	}
	
	public WrapperForListResponseDto<MailingResponseDto> sendMailsToTeachers(
			MailingRequestDto dto) {
		
		List<Email> emails = emailRepo.findAllByTeacher();
		List<MailingResponseDto> responseDto = new ArrayList<>(emails.size());
		
		for (Email email : emails) {
			responseDto.add(sendEmail(
					email.getAddress(), 
					dto.getSubject(), 
					dto.getContent()));
		}
		return new WrapperForListResponseDto<>(responseDto);
	}
}



