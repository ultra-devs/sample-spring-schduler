package com.ultradev.service;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
	int retryMaxCount;

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
			if (ObjectUtils.isEmpty(fallBackEntry)) {
				log.info("No Entry found for fall back , retrigger is not required ");
				return;
			}
			retryCount = fallBackEntry.getRetryCount();
			if (maxCountPreFlightCheck(retryCount) && jobStatusCheck(fallBackEntry)) {
				retryCount++;
				fallBackEntry.setRetryCount(retryCount);
				recoverAuditDBService.saveFallbackAuditEntry(fallBackEntry);
				sayHelloService.processHello();
				// set success Status to true
				fallBackEntry.setStatus(true);
				recoverAuditDBService.saveFallbackAuditEntry(fallBackEntry);
				log.info("Re-trigger completed Successfully ");
			}

		} catch (Exception e) {
			log.error("Batch File Retry job failed ", e);
			e.printStackTrace();
			retryFailed(retryCount);
		}

	}

	private boolean jobStatusCheck(ServiceAuditEntity fallBackEntry) {
		return !fallBackEntry.isStatus();

	}

	private boolean maxCountPreFlightCheck(int count) {
		if (count > retryMaxCount) {
			log.info("Retry Count has exceeded max limit :{} please trigger job  manually", retryMaxCount);
			javaMailService.sendHtmlMessage("Failure:File Parsing Retry Attempt  Failed" + new Date(),
					"<h1>Retry Count has exceeded max limit :{} please trigger job  manually .. curl http://localhost:9091/batch/run</h1>");
			return false;
		}
		return true;

	}

	private void retryFailed(int retryCount) {
		javaMailService.sendHtmlMessage("Failure:File Parsing Retry Attempt  Failed" + new Date(), String.format(
				"<h1>Batch File retry Parsing Failed , Retry Attempt left :%d </h1>", (retryMaxCount - retryCount)));
	}
}
