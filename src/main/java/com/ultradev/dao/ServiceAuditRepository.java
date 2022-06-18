package com.ultradev.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author shashank
 *
 */
@Repository
public interface ServiceAuditRepository extends JpaRepository<ServiceAuditEntity, Long> {
	
	
	/*
	 * @Query("select count(*) from ServiceAuditEntity where TIME_STAMP > CURRENT_DATE()"
	 * ) int findByTimeStamp();
	 */
	  @Query("select id,jobId,timeStamp,retryCount from ServiceAuditEntity where TIME_STAMP > CURRENT_DATE() and rownum<2 ")
	  List<ServiceAuditEntity> findByTimeStamp();
	 
	
	//List<ServiceAuditEntity> findTodaysFailedJob();

}
