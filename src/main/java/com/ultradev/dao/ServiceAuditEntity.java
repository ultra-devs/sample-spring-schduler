package com.ultradev.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
//@Table(name = "SERVICE_AUDIT_ENTITY")
@Data
public class ServiceAuditEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	@Column(name = "JOB_ID")
	String jobId;
	@Column(name = "TIME_STAMP")
	java.util.Date timeStamp;
	@Column(name = "RETRY_COUNT")
	int retryCount;

}
