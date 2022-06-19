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

@Service

public class SayHelloRecoveryService {
	org.slf4j.Logger log = LoggerFactory.getLogger(SayHelloRecoveryService.class);
	@Value("${application.introduce.error}")
	boolean isApplicationError;
	@Value("${application.recovery.crontab}")
	String jobId;
	@Value("${application.retryMaxCount}")
	int retryMaxOunt;

	@Autowired
	RecoverAuditDBService recoverAuditDBService;
	@Autowired
	SayHelloService sayHelloService;

	@Autowired
	JavaMailService javaMailService;

	@Scheduled(cron = "${application.recovery.crontab}") //
	public void sayHelloRecoveryService() {

		int retryCount = 0;
		try {
			ServiceAuditEntity fallBackEntry = recoverAuditDBService.getFallBackEntry();
			/* if (!ObjectUtils.isEmpty(fallBackEntry)) { */
			retryCount = fallBackEntry.getRetryCount();
			if (retryCount > retryMaxOunt) {
				log.debug("Retry Count has exceeded max limit :{} please trigger job  manually", retryCount);
				return;
			}
			retryCount++;
			fallBackEntry.setRetryCount(retryCount++);
			recoverAuditDBService.saveFallbackAuditEntry(fallBackEntry);
			log.info("Submitted Job for retrigger ");
			sayHelloService.processHello();
			log.info("Retrigger completed Successfully ");

		} catch (Exception e) {
			retryFailed(retryCount);
		}

	}

	private void retryFailed(int retryCount) {
		javaMailService.sendHtmlMessage("Failure:File Parsing Retry Attempt  Failed" + new Date(),
				String.format("<h1>Batch File retry Parsing Failed , Retry Attempt left :%d </h1>",
						(retryMaxOunt - retryCount)),
				null);
	}
}
