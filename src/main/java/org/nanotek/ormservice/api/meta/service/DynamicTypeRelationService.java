package org.nanotek.ormservice.api.meta.service;

import java.util.Map;

import org.nanotek.ormservice.api.meta.MetaClass;
import org.nanotek.ormservice.api.meta.builder.MetaClassRelationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class DynamicTypeRelationService {

	@Autowired
	DefaultListableBeanFactory beanFactory;
	
	@Autowired
	Map<String, MetaClass> classCache;
	
	@Autowired
	MetaClassRelationsBuilder relationBuilder;
	
	public void processRelationClasses() {
		classCache.entrySet().forEach(e -> processRelationIfAny(e.getValue()));
	}

	private void processRelationIfAny(MetaClass metaClass) {
		metaClass
		.hasRelations()
		.ifPresent(r -> 
			relationBuilder.build(beanFactory.getBeanClassLoader(), metaClass));
	}
	
}
