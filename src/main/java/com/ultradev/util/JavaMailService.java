package com.ultradev.util;

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

	public void sendHtmlMessage(String subject, String textMessage, String attachmentFile)
			{

		try {
		MimeMessage msg = javaMailSender.createMimeMessage();
		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo(mailReceiver);
		helper.setSubject(subject);
		helper.setText(textMessage, true);
		if (org.springframework.util.StringUtils.hasText(attachmentFile))
			helper.addAttachment("my_photo.png", new ClassPathResource(attachmentFile));
		javaMailSender.send(msg);
		}
		catch (Exception e) {
			throw new IllegalStateException("Mail service failed with Exception: "+e.getMessage());
		}

	}

}
