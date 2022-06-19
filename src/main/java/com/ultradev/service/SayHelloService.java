package com.ultradev.service;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ultradev.dao.RecoverAuditDBService;
import com.ultradev.dao.ServiceAuditEntity;
import com.ultradev.util.JavaMailService;

import lombok.extern.slf4j.Slf4j;

@Service
public class SayHelloService {

	org.slf4j.Logger log = LoggerFactory.getLogger(SayHelloService.class);

	@Value("${application.introduce.error}")
	boolean isApplicationError;

	@Value("${application.jobid}")
	String jobId;

	@Autowired
	RecoverAuditDBService recoverAuditDBService;
	@Autowired
	JavaMailService javaMailService;

	@Scheduled(cron = "${application.crontab}") //
	public void sayHello() {
		try {
			processHello();
			javaMailService.sendHtmlMessage("Success: File Parsing ",
					"<h1>Batch File Parsing Completed Successfully </h1>", null);

		} catch (Exception e) {
			log.error("File Parsing has failed with Error :{} ", e);
			javaMailService.sendHtmlMessage("Failure:File Parsing is Failed" + new Date(),
					"<h1>Batch File Parsing Failed , Will be reattempted by Retry Job </h1>", null);
			publishJobAuditStatus();
		}
	}

	private void publishJobAuditStatus() {
		if (!recoverAuditDBService.doesFallBackEntryExist()) {
			ServiceAuditEntity serviceAuditEntity = new ServiceAuditEntity();
			serviceAuditEntity.setJobId(jobId);
			serviceAuditEntity.setTimeStamp(new Date());
			recoverAuditDBService.saveFallbackAuditEntry(serviceAuditEntity);
			// log.info("Successfully Submitted Audit Job ");
		}

	}

	public void processHello() {
		String x = null;
		x.charAt(0);
		if (recoverAuditDBService.doesFallBackEntryExist())
			log.info("This is hello services running");
		System.out.println("This is hello services running");
	}

}
