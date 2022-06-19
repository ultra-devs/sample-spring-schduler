package com.ultradev.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class JavaMailService {

	@Value("${application.mail.receiver}")
	String mailReceiver;
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String subject, String textMessage) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(mailReceiver);
		msg.setSubject(subject);
		msg.setText(textMessage);
		javaMailSender.send(msg);

	}
	


	
	public void sendHtmlMessage(String subject, String textMessage) {

		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			// true = multipart message
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(mailReceiver);
			helper.setSubject(subject);
			helper.setText(textMessage, true);
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Mail service failed with Exception: " + e.getMessage());

		}

	}
	
	public void sendHtmlMessageWithAttachment(String subject, String textMessage,String attchmentFileName) {

		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			// true = multipart message
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(mailReceiver);
			helper.setSubject(subject);
			helper.setText(textMessage, true);
			updateMimeMessageWithAttachment(helper, attchmentFileName);
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Mail service failed with Exception: " + e.getMessage());

		}

	}
	
	private void updateMimeMessageWithAttachment(MimeMessageHelper helper,String attachmentFile) throws MessagingException
	{
		if (org.springframework.util.StringUtils.hasText(attachmentFile))
			helper.addAttachment(attachmentFile, new ClassPathResource(attachmentFile));
	}

}
