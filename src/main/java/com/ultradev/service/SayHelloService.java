package com.ultradev.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ultradev.dao.ServiceAuditEntity;
import com.ultradev.dao.ServiceAuditRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SayHelloService {
	
	@Value("${application.introduce.error}")
	boolean isApplicationError;
	
	
	@Value("${application.jobid}")
	String jobId;

	
	@Autowired
	ServiceAuditRepository serviceAuditRepository;
	

	
	@Scheduled(cron = "${application.crontab}") //
	public void sayHello()
	{
		try {
		if(isApplicationError)// this is simulating an error condition
		{
			throw new IllegalStateException("Schedule Job Has Failed ");
		}
		log.info("This is hello service running ");
		}
		catch (Exception e) {
			log.error("Exception has Occured ",e);
			publishJobAuditStatus();
		}
	}
	
	
	private void publishJobAuditStatus()
	{
		List<ServiceAuditEntity> existingAuditList = serviceAuditRepository.findByTimeStamp();
		
		if(CollectionUtils.isEmpty(existingAuditList))
		{
		ServiceAuditEntity serviceAuditEntity= new ServiceAuditEntity();
		serviceAuditEntity.setJobId(jobId);
		serviceAuditEntity.setTimeStamp(new Date());
		serviceAuditRepository.save(serviceAuditEntity);
		log.info("Successfully Submitted Audit Job ");
		}
		
		
		
	}

}
