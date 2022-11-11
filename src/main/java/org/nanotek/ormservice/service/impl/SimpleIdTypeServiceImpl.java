package org.nanotek.ormservice.service.impl;

import org.nanotek.ormservice.api.base.SimpleIdType;
import org.nanotek.ormservice.repository.SimpleIdTypeRepository;
import org.nanotek.ormservice.service.SimpleIdTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleIdTypeServiceImpl 
extends BaseServiceImpl<SimpleIdType, Long, SimpleIdTypeRepository> 
implements SimpleIdTypeService{

	public SimpleIdTypeServiceImpl(@Autowired SimpleIdTypeRepository repository) {
		super(repository);
	}
}
