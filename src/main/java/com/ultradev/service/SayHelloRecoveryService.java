package com.ultradev.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ultradev.dao.ServiceAuditEntity;
import com.ultradev.dao.ServiceAuditRepository;

import ch.qos.logback.classic.Logger;
import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SayHelloRecoveryService {

	@Value("${application.introduce.error}")
	boolean isApplicationError;
	@Value("${application.recovery.crontab}")
	String jobId;
	@Value("${application.retryMaxCount}")
	int retryMaxOunt;
	@Autowired
	ServiceAuditRepository serviceAuditRepository;
	@Autowired
	SayHelloService sayHelloService;

	@Scheduled(cron = "${application.recovery.crontab}") //
	public void sayHelloRecoveryService() {
		try {

			log.info("SayHelloRecoveryService is getting called " + serviceAuditRepository.findAll());
			List<ServiceAuditEntity> serviceAuditEntity = serviceAuditRepository.findAll();
			if (!isEmpty(serviceAuditEntity)) {
				ServiceAuditEntity auditEntry = CollectionUtils.firstElement(serviceAuditEntity);
				int retryCount = auditEntry.getRetryCount();
				if (retryCount > retryMaxOunt) {
					log.debug("Retry Count has exceeded max limit :{} please trigger job manually", retryCount);
					return;
				}
				// increased retry count
				retryCount++;

				auditEntry.setRetryCount(retryCount++);

				serviceAuditRepository.save(auditEntry);
				log.info("Submitted  Job for retrigger ");
				sayHelloService.sayHello();
				log.info("Retrigger completed  Successfully ");
			}
		} catch (Exception e) {
			log.error("Retry job has Failed ", e);
		}

	}
}
