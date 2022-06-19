package com.ultradev.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ultradev.dao.RecoverAuditDBService;
import com.ultradev.dao.ServiceAuditEntity;
import com.ultradev.util.JavaMailService;

@Service
public class SayHelloService {

	org.slf4j.Logger log = LoggerFactory.getLogger(SayHelloService.class);


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

		} catch (Exception e) {
			log.error("File Parsing has failed with Error :{} ", e);
			javaMailService.sendHtmlMessage("Failure:File Parsing is Failed" + new Date(),
					"<h1>Batch File Parsing Failed , Will be Re-attempted by Retry Job </h1>");
			publishJobAuditStatus();
		}
	}

	private void publishJobAuditStatus() {
		if (recoverAuditDBService.doesFallBackEntryExist()) {
			log.info("Audit Entry does not Exist creating Audit Entry ");
			ServiceAuditEntity serviceAuditEntity = new ServiceAuditEntity();
			serviceAuditEntity.setJobId(jobId);
			serviceAuditEntity.setTimeStamp(new Date());
			serviceAuditEntity.setStatus(false);
			recoverAuditDBService.saveFallbackAuditEntry(serviceAuditEntity);
			log.info("Successfully Created Retry Entry for failed Job ");
		}

	}

	public void processHello() {
		complexBatchProcessing();
		javaMailService.sendHtmlMessage("Success: File Parsing ",
				"<h1>Batch File Parsing Completed Successfully </h1>");
	}

	/**
	 * this is mocking a very complex batch processing logic
	 */
	private void complexBatchProcessing() {

		// this is a dummy implementation for file processing , replace with whatever
		// logic user want to implement for scheduler
		String fileName = "D:\\source-code\\data\\sample.txt";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(System.out::println);
		} catch (IOException e) {
			throw new IllegalStateException("File Processing has failed :{} ", e);
		}
	}

}
