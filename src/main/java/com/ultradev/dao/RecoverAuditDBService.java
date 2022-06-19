package com.ultradev.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service

public class RecoverAuditDBService {

	@Autowired
	ServiceAuditRepository serviceAuditRepository;

	public boolean doesFallBackEntryExist() {
		/*
		 * System.out.println("getFallBackEntry() "+getFallBackEntry());
		 * System.out.println("getFallBackEntry() 2"+ObjectUtils.isEmpty(
		 * getFallBackEntry()));
		 */
		System.out.println("getFallBackEntry() "+getFallBackEntry());
		return ObjectUtils.isEmpty(getFallBackEntry());

	}

	public ServiceAuditEntity getFallBackEntry() {
		List<ServiceAuditEntity> serviceAuditEntities = serviceAuditRepository.findAll();
		if (!CollectionUtils.isEmpty(serviceAuditEntities))
			return CollectionUtils.firstElement(serviceAuditEntities);

		return null;
	}

	public void saveFallbackAuditEntry(ServiceAuditEntity serviceAuditEntity) {
		serviceAuditRepository.save(serviceAuditEntity);
	}
}
