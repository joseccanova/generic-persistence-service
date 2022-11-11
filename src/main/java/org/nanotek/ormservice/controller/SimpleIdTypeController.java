package org.nanotek.ormservice.controller;

import org.nanotek.ormservice.api.base.SimpleIdType;
import org.nanotek.ormservice.service.SimpleIdTypeService;
import org.nanotek.ormservice.service.impl.SimpleIdTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/simple_id_type")
public class SimpleIdTypeController 
extends EntityController<SimpleIdType , Long, SimpleIdTypeService> {

	public SimpleIdTypeController(@Autowired SimpleIdTypeService serv) {
		super(serv);
	}
	
}
