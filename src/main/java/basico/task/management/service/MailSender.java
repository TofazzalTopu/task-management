package basico.task.management.service;

import basico.task.management.dto.EmailDTO;
import basico.task.management.dto.MailDto;
import basico.task.management.model.primary.Task;

import javax.mail.MessagingException;

public interface MailSender {
	
	 void sendSimpleMessage(String to, String subject, String text);
	 void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);
	 void sendFormattedMail(MailDto mailDto);
	 void sendTaskMail(Task task, String toEmail, String subject);
	 void sendEmail(EmailDTO email) throws MessagingException;


}
